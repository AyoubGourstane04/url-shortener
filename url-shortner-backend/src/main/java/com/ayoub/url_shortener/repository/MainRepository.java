package com.ayoub.url_shortener.repository;

import com.ayoub.url_shortener.entity.UrlInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MainRepository extends JpaRepository<UrlInfo, Long> {
    Optional<UrlInfo> findByUrl(String url);
    Optional<UrlInfo> findByShortCode(String shortCode);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE urls RESTART IDENTITY", nativeQuery = true)
    void truncateTable();
}
