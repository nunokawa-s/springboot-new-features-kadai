package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;

@Controller
@RequestMapping("/houses") // /houses URLにアクセスした場合にこのコントローラーが処理を担当
public class HouseController {

    private final HouseRepository houseRepository; // HouseRepositoryのインジェクション
    private final ReviewRepository reviewRepository;
    
    // コンストラクタインジェクション
    public HouseController(HouseRepository houseRepository, ReviewRepository reviewRepository) {
        this.houseRepository = houseRepository; 
        this.reviewRepository = reviewRepository;
    }

    // 物件一覧を表示するためのメソッド
    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword, // 検索キーワード（物件名や住所）
            @RequestParam(name = "area", required = false) String area, // エリアによる絞り込み
            @RequestParam(name = "price", required = false) Integer price, // 価格の絞り込み
            @RequestParam(name = "order", required = false) String order, // ソート順（価格順など）
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, // ページング設定
            Model model) {

        // ページ情報を格納するためのPage<House>オブジェクト
        Page<House> housePage;

        // 検索キーワードが入力されている場合の処理
        if (keyword != null && !keyword.isEmpty()) {
            // ソート順が"priceAsc"なら価格昇順で検索
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
                        "%" + keyword + "%", pageable);
            } else {
                // デフォルトは新しいものから順に並べる
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%",
                        "%" + keyword + "%", pageable);
            }
        } else if (area != null && !area.isEmpty()) {
            // エリアで絞り込みがある場合
            if (order != null && order.equals("priceAsc")) {
                // 価格昇順で並べる
                housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
            } else {
                // 新しいものから順に並べる
                housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
            }
        } else if (price != null) {
            // 価格による絞り込みがある場合
            if (order != null && order.equals("priceAsc")) {
                // 価格昇順で並べる
                housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                // 新しいものから順に並べる
                housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        } else {
            // 特に条件がない場合はデフォルトで全件を取得
            if (order != null && order.equals("priceAsc")) {
                // 価格昇順で並べる
                housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                // 新しいものから順に並べる
                housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        // モデルに情報をセット
        model.addAttribute("housePage", housePage); // ページングされた物件リスト
        model.addAttribute("keyword", keyword); // 検索キーワード
        model.addAttribute("area", area); // エリア
        model.addAttribute("price", price); // 価格
        model.addAttribute("order", order); // ソート順

        // 物件一覧ページ（houses/index）を表示
        return "houses/index";
    }

    // 物件の詳細を表示するメソッド
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, @PageableDefault(page = 0, size = 6, sort = "id") Pageable pageable, Model model) {
        // 物件IDに基づいて物件情報を取得
        House house = houseRepository.getReferenceById(id);
        Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);       

        // モデルに物件データをセット
        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", new ReservationInputForm()); // 予約フォームを表示するための準備
        model.addAttribute("reviewPage", reviewPage);                
        
        // 物件詳細ページ（houses/show）を表示
        return "houses/show";
        
        
    }
}