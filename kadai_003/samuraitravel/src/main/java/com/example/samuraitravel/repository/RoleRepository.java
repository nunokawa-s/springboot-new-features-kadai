package com.example.samuraitravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * 名前でロールを検索するメソッド。
     * 
     * このメソッドは、指定された名前に一致するロール（`Role`）をデータベースから取得します。
     * もし一致するロールが見つかれば、それを返します。存在しない場合は `null` が返されます。
     * 
     * @param name ロールの名前
     * @return Role 指定された名前を持つロールオブジェクト
     */
    // 名前に基づいてロールを検索するメソッド
    public Role findByName(String name);
}
