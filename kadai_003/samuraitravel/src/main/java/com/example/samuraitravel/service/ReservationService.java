package com.example.samuraitravel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReservationService {
    
    private final ReservationRepository reservationRepository;  // 予約リポジトリ
    private final HouseRepository houseRepository;  // 物件リポジトリ
    private final UserRepository userRepository;  // ユーザーリポジトリ
    
    // コンストラクタインジェクションによる依存関係の注入
    public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;  
        this.houseRepository = houseRepository;  
        this.userRepository = userRepository;  
    }    
    
    // 予約を作成するメソッド
    @Transactional  // トランザクション管理を有効にする
    public void create(Map<String, String> paymentIntentObject) {
        Reservation reservation = new Reservation();  // 新しい予約オブジェクトを作成
        
        // リクエストから必要な情報を抽出し、予約オブジェクトに設定
        Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));  // 物件ID
        Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));  // ユーザーID
        
        // 物件とユーザー情報をリポジトリから取得
        House house = houseRepository.getReferenceById(houseId);       
        User user = userRepository.getReferenceById(userId);
        
        // チェックイン日とチェックアウト日をLocalDateに変換
        LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
        LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
        
        // 宿泊人数と料金を取得
        Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));        
        Integer amount = Integer.valueOf(paymentIntentObject.get("amount")); 
        
        // 予約オブジェクトにデータをセット
        reservation.setHouse(house);
        reservation.setUser(user);
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setAmount(amount);
        
        // 予約をデータベースに保存
        reservationRepository.save(reservation);
    }    
    
    // 宿泊人数が定員内かどうかをチェックするメソッド
    // numberOfPeople: 宿泊人数、capacity: 物件の定員
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        // 宿泊人数が定員を超えないか確認し、trueまたはfalseを返す
        return numberOfPeople <= capacity;
    }
    
    // 宿泊料金を計算するメソッド
    // checkinDate: チェックイン日、checkoutDate: チェックアウト日、price: 1泊あたりの料金
    public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
        // チェックイン日とチェックアウト日の間の日数を計算
        long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        
        // 宿泊日数に1泊あたりの料金を掛けて宿泊料金を計算
        int amount = price * (int)numberOfNights;
        
        // 計算した金額を返す
        return amount;
    }    
}
