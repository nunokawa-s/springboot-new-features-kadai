package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.event.SignupEventPublisher;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.service.UserService;
import com.example.samuraitravel.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final UserService userService;
    private final SignupEventPublisher signupEventPublisher;
    private final VerificationTokenService verificationTokenService;

    // コンストラクタインジェクションで必要なサービスを注入
    public AuthController(UserService userService, SignupEventPublisher signupEventPublisher,
                          VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }

    // ログインページを表示する
    @GetMapping("/login")
    public String login() {
        // ログイン画面のテンプレートを返す
        return "auth/login";  // "auth/login"というテンプレートを返す
    }

    // サインアップフォームを表示する
    @GetMapping("/signup")
    public String signup(Model model) {
        // 新しいSignupFormオブジェクトを作成し、モデルに追加してサインアップフォームを表示
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";  // "auth/signup"というサインアップページを返す
    }

    // サインアップ処理を実行する
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        
        // メールアドレスがすでに登録されている場合、エラーメッセージを追加
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // パスワードと確認用パスワードが一致しない場合、エラーメッセージを追加
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }

        // バリデーションエラーがある場合、サインアップフォームを再表示
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        // ユーザーを作成し、サインアップイベントを発行
        User createdUser = userService.create(signupForm);
        
        // 現在のリクエストURLを取得
        String requestUrl = new String(httpServletRequest.getRequestURL());
        
        // サインアップイベントを発行して認証メールを送信
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        
        // リダイレクト時にメッセージを表示
        redirectAttributes.addFlashAttribute("successMessage",
                "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        // トップページへリダイレクト
        return "redirect:/";  // トップページにリダイレクト
    }

    // 認証トークンを使って会員登録を完了させる
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        
        // トークンを使ってVerificationTokenを取得
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        // トークンが有効ならユーザーを有効化
        if (verificationToken != null) {
            User user = verificationToken.getUser();
            userService.enableUser(user);
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);
        } else {
            // トークンが無効ならエラーメッセージを表示
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }

        // 認証結果を表示するページを返す
        return "auth/verify";  // 認証結果を表示するページを返す
    }
}
