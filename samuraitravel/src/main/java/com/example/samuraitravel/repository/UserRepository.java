package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * メールアドレスでユーザーを検索するメソッド。
     * 
     * このメソッドは、指定されたメールアドレスに一致するユーザー（`User`）をデータベースから検索します。
     * 見つかったユーザーを返し、存在しない場合は `null` を返します。
     * 
     * @param email 検索するメールアドレス
     * @return User 指定されたメールアドレスを持つユーザーオブジェクト
     */
    // メールアドレスに基づいてユーザーを検索するメソッド
    public User findByEmail(String email);

    /**
     * 名前またはふりがなでユーザーを検索するメソッド。
     * 
     * このメソッドは、名前（`name`）またはふりがな（`furigana`）に部分一致するユーザーをデータベースから検索します。
     * 名前またはふりがなに一致するユーザーを、ページ単位で返します。
     * 
     * @param nameKeyword 名前に一致するキーワード
     * @param furiganaKeyword ふりがなに一致するキーワード
     * @param pageable ページネーション情報（ページ番号、ページサイズなど）
     * @return Page<User> 指定されたキーワードに一致するユーザーをページ単位で返す
     */
    // 名前またはふりがなに部分一致するユーザーを検索し、ページネーションを適用するメソッド
    public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
}
