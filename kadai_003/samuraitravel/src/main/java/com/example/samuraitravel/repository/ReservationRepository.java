package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * ユーザーごとの予約を作成日順に降順で取得するメソッド。
     * 
     * このメソッドは、特定のユーザーに関連する予約を、作成日（`createdAt`）の降順でページネーションを考慮して取得します。
     * 
     * @param user 特定のユーザー
     * @param pageable ページネーション情報（ページ番号やサイズなど）
     * @return Page<Reservation> 指定されたユーザーの予約をページ単位で返す
     */
    public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
