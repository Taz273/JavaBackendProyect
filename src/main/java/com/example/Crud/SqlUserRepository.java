package com.example.Crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlUserRepository extends JpaRepository<SqlUser, Long> {
    @Query("SELECT u FROM SqlUser u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SqlUser> searchByNameOrEmail(@Param("keyword") String keyword);
}