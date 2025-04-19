package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

public interface HouseRepository extends JpaRepository<House, Integer> {

    /**
     * 指定されたキーワードに部分一致する物件をページネーションで検索する。
     *
     * @param keyword  検索キーワード
     * @param pageable ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByNameLike(String keyword, Pageable pageable);

    /**
     * 指定された名前または住所のキーワードに部分一致する物件を、作成日の降順でページネーションで検索する。
     *
     * @param nameKeyword    名前の検索キーワード
     * @param addressKeyword 住所の検索キーワード
     * @param pageable       ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword,
            Pageable pageable);

    /**
     * 指定された名前または住所のキーワードに部分一致する物件を、価格の昇順でページネーションで検索する。
     *
     * @param nameKeyword    名前の検索キーワード
     * @param addressKeyword 住所の検索キーワード
     * @param pageable       ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword,
            Pageable pageable);

    /**
     * 指定された住所のキーワードに部分一致する物件を、作成日の降順でページネーションで検索する。
     *
     * @param area     住所の検索キーワード
     * @param pageable ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);

    /**
     * 指定された住所のキーワードに部分一致する物件を、価格の昇順でページネーションで検索する。
     *
     * @param area     住所の検索キーワード
     * @param pageable ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);

    /**
     * 指定された価格以下の物件を、作成日の降順でページネーションで検索する。
     *
     * @param price    上限価格
     * @param pageable ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

    /**
     * 指定された価格以下の物件を、価格の昇順でページネーションで検索する。
     *
     * @param price    上限価格
     * @param pageable ページネーション情報
     * @return 該当する物件のページ
     */
    public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

    /**
     * 全ての物件を作成日の降順でページネーションで取得する。
     *
     * @param pageable ページネーション情報
     * @return 全ての物件のページ
     */
    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 全ての物件を価格の昇順でページネーションで取得する。
     *
     * @param pageable ページネーション情報
     * @return 全ての物件のページ
     */
    public Page<House> findAllByOrderByPriceAsc(Pageable pageable);

    /**
     * 作成日の降順で上位10件の物件を取得する。
     *
     * @return 上位10件の物件リスト
     */
    public List<House> findTop10ByOrderByCreatedAtDesc();
}