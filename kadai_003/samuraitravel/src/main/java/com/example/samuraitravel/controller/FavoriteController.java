package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteRepository favoriteRepository, HouseRepository houseRepository,
			FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
		this.favoriteService = favoriteService;
	}

	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		model.addAttribute("favoritePage", favoritePage);

		return "favorites/index";
	}

	@PostMapping("/houses/{houseId}/favorites/create")
	public String create(@PathVariable(name = "houseId") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model) {
<<<<<<< HEAD

		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		Favorite favorite = favoriteRepository.findByHouseAndUser(house, user);

		if (favorite == null) {
			// まだ登録されていないので登録処理
			Favorite newFavorite = new Favorite();
			newFavorite.setHouse(house);
			newFavorite.setUser(user);
			favoriteRepository.save(newFavorite);
			redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
		} else {
			// すでに登録済み
			redirectAttributes.addFlashAttribute("errorMessage", "すでにお気に入りに追加済みです。");
		}
=======
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();

		favoriteService.create(house, user);
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
>>>>>>> origin/main

		return "redirect:/houses/{houseId}";
	}

	@PostMapping("/houses/{houseId}/favorites/{favoriteId}/delete")
	public String delete(@PathVariable(name = "favoriteId") Integer favoriteId, RedirectAttributes redirectAttributes) {
<<<<<<< HEAD
	    if (favoriteRepository.existsById(favoriteId)) {

		favoriteRepository.deleteById(favoriteId);

		redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "指定されたお気に入りは存在しませんでした。");
	    }
=======
		favoriteRepository.deleteById(favoriteId);

		redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");

>>>>>>> origin/main
		return "redirect:/houses/{houseId}";
	}
}
