package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;

	// コンストラクタ: UserRepository と UserService のインスタンスを受け取って初期化
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	// ユーザー情報の表示
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		// ログインユーザー情報を取得
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

		// ユーザー情報をビューに渡す
		model.addAttribute("user", user);

		// "user/index"というビューを返す
		return "user/index";
	}

	// ユーザー情報の編集画面表示
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		// ログインユーザー情報を取得
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

		// ユーザー情報をフォーム用に変換（UserEditForm へ変換）
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(),
				user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());

		// 編集フォームをビューに渡す
		model.addAttribute("userEditForm", userEditForm);

		// "user/edit" というビューを返す
		return "user/edit";
	}

	// ユーザー情報の更新処理
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// メールアドレスが変更されており、かつそのメールアドレスがすでに登録済みのものであればエラーを追加
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
			// メールアドレスの重複エラーを BindingResult に追加
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}

		// バリデーションエラーがある場合は、編集画面に戻る
		if (bindingResult.hasErrors()) {
			return "user/edit";
		}

		// ユーザー情報の更新処理を呼び出し
		userService.update(userEditForm);

		// 更新成功のメッセージをリダイレクト先にフラッシュ属性として渡す
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		// ユーザー情報画面にリダイレクト
		return "redirect:/user";
	}
}
