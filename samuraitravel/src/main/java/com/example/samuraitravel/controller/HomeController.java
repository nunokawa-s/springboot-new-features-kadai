package com.example.samuraitravel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;

@Controller
public class HomeController {
	private final HouseRepository houseRepository;

	// コンストラクタインジェクションでHouseRepositoryを注入
	public HomeController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	// トップページ（ホームページ）を表示する
	@GetMapping("/")
	public String index(Model model) {
		// 最新の10件の民宿情報を取得（作成日順）
		List<House> newHouses = houseRepository.findTop10ByOrderByCreatedAtDesc();

		// 取得した新しい民宿情報をモデルに追加
		model.addAttribute("newHouses", newHouses);

		// トップページに遷移する
		return "index"; // "index"という名前のビュー（テンプレート）を返す
	}
}
