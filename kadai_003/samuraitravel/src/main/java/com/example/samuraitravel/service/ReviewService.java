package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewRepository, HouseRepository houseRepository,
			UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public void create(House house, User user, ReviewPostForm reviewPostForm) {
		Review review = new Review();

		review.setHouse(house);
		review.setUser(user);
		review.setReviewScore(reviewPostForm.getReviewScore());
		review.setReviewText(reviewPostForm.getReviewText());

		reviewRepository.save(review);
	}

	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

		review.setReviewScore(reviewEditForm.getReviewScore());
		review.setReviewText(reviewEditForm.getReviewText());

		reviewRepository.save(review);
	}
}

//formをセットをする
//リポジトリのｓｑｌを呼び出す