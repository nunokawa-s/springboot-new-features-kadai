package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.samuraitravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {
    private final StripeService stripeService;

    // Stripe APIキーを外部設定ファイルから取得
    @Value("${stripe.api-key}")
    private String stripeApiKey;

    // Webhookのシークレットキーを外部設定ファイルから取得
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    // コンストラクタインジェクションでStripeServiceを注入
    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    // Stripe Webhookのエンドポイントを受け取るメソッド
    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, // Webhookのペイロード（イベントデータ）
            @RequestHeader("Stripe-Signature") String sigHeader) { // Stripeから送られてくる署名ヘッダー
        // Stripe APIキーを設定
        Stripe.apiKey = stripeApiKey;
        Event event = null;

        try {
            // Webhookの署名を検証してイベントデータを構築
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            // 署名検証に失敗した場合、400 Bad Requestを返す
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 受け取ったイベントが "checkout.session.completed" なら、セッション完了処理を行う
        if ("checkout.session.completed".equals(event.getType())) {
            // セッション完了イベントを処理
            stripeService.processSessionCompleted(event);
        }

        // Webhookの処理が成功した場合、200 OKを返す
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
