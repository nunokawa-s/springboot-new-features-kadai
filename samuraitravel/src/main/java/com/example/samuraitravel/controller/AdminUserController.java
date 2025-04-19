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

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
	private final UserRepository userRepository; // ユーザーリポジトリ

	// コンストラクタインジェクションでUserRepositoryを受け取る
	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// ユーザー側の一覧ページを表示するメソッド
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {

		Page<User> userPage;

		// 検索キーワードが指定されている場合、ユーザー名やフリガナを検索
		if (keyword != null && !keyword.isEmpty()) {
			userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword + "%", "%" + keyword + "%", pageable);
		} else {
			// キーワードが指定されていない場合、すべてのユーザーをページネーションで表示
			userPage = userRepository.findAll(pageable);
		}

		// モデルにユーザーのページ情報と検索キーワードを追加
		model.addAttribute("userPage", userPage);
		model.addAttribute("keyword", keyword);

		return "admin/users/index"; // ユーザー一覧ページを返す
	}

	// ユーザーの詳細ページを表示するメソッド
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		User user = userRepository.getReferenceById(id); // 指定されたIDのユーザーを取得

		// モデルにユーザー情報を追加
		model.addAttribute("user", user);

		return "admin/users/show"; // ユーザー詳細ページを返す
	}
}
