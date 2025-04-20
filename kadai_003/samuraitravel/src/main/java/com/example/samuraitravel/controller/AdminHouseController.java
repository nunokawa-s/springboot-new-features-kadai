package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
	private final HouseRepository houseRepository; // 物件リポジトリ
	private final HouseService houseService; // 物件サービス

	// コンストラクタインジェクションで依存関係を注入
	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
		this.houseRepository = houseRepository;
		this.houseService = houseService;
	}

	// 物件一覧ページを表示するメソッド
	@GetMapping
	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {

		Page<House> housePage;

		// 検索キーワードが指定された場合、キーワードに一致する物件を検索
		if (keyword != null && !keyword.isEmpty()) {
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			// キーワードが指定されていない場合、全ての物件をページネーション付きで表示
			housePage = houseRepository.findAll(pageable);
		}

		model.addAttribute("housePage", housePage); // 物件のページデータをモデルに追加
		model.addAttribute("keyword", keyword); // 検索キーワードをモデルに追加

		return "admin/houses/index"; // 物件一覧ページに遷移
	}

	// 物件詳細ページを表示するメソッド
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id); // 指定されたIDの物件を取得

		model.addAttribute("house", house); // 物件情報をモデルに追加

		return "admin/houses/show"; // 物件詳細ページに遷移
	}

	// 物件登録フォームを表示するメソッド
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm()); // 新しい物件登録フォームを作成

		return "admin/houses/register"; // 物件登録フォームページに遷移
	}

	// 新しい物件を登録するメソッド
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// 入力内容にエラーがある場合、物件登録フォームに戻る
		if (bindingResult.hasErrors()) {
			return "admin/houses/register";
		}

		// サービスを通じて物件を登録
		houseService.create(houseRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。"); // 登録成功メッセージをフラッシュリダイレクト

		return "redirect:/admin/houses"; // 物件一覧ページにリダイレクト
	}

	// 物件編集フォームを表示するメソッド
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id); // 指定されたIDの物件を取得
		String imageName = house.getImageName(); // 物件画像の名前を取得

		// 編集フォームに必要な情報をセット
		HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(),
				house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(),
				house.getPhoneNumber());

		model.addAttribute("imageName", imageName); // 画像名をモデルに追加
		model.addAttribute("houseEditForm", houseEditForm); // 編集フォームをモデルに追加

		return "admin/houses/edit"; // 物件編集フォームページに遷移
	}

	// 物件情報を更新するメソッド
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// 入力内容にエラーがある場合、物件編集フォームに戻る
		if (bindingResult.hasErrors()) {
			return "admin/houses/edit";
		}

		// サービスを通じて物件情報を更新
		houseService.update(houseEditForm);
		// 更新成功メッセージをフラッシュリダイレクト
		redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。"); 
		// 物件一覧ページにリダイレクト
		return "redirect:/admin/houses"; 
	}

	// 物件を削除するメソッド
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		houseRepository.deleteById(id); // 指定されたIDの物件を削除

		redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。"); // 削除成功メッセージをフラッシュリダイレクト

		return "redirect:/admin/houses"; // 物件一覧ページにリダイレクト
	}
}
