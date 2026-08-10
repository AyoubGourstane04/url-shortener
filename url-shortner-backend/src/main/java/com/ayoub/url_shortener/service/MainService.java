package com.ayoub.url_shortener.service;


import com.ayoub.url_shortener.aop.TrackExecutionTime;
import com.ayoub.url_shortener.dto.GenerateQrCodeRequestDTO;
import com.ayoub.url_shortener.dto.GenerateQrCodeResponseDTO;
import com.ayoub.url_shortener.dto.ShortenRequestDTO;
import com.ayoub.url_shortener.dto.ShortenResponseDTO;
import com.ayoub.url_shortener.entity.UrlInfo;
import com.ayoub.url_shortener.repository.MainRepository;
import com.ayoub.url_shortener.util.Base62;
import com.ayoub.url_shortener.util.CommandRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MainService {
    private final MainRepository mainRepository;
    private static final String HOST_URL = "http://localhost:8080/api/v1/";

    public MainService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Cacheable(value = "urls", key = "#requestDTO.url")
    @TrackExecutionTime
    public ShortenResponseDTO shorten(ShortenRequestDTO requestDTO) {
        String url = requestDTO.getUrl();
        String shortCode;

        Optional<UrlInfo> existingUrl = mainRepository.findByUrl(url);

        if (existingUrl.isPresent()) {
             shortCode = existingUrl.get().getShortCode();
        }else{
            UrlInfo urlInfo = new UrlInfo();
            urlInfo.setUrl(url);

            UrlInfo savedUrl = mainRepository.save(urlInfo);

            shortCode = Base62.encodeUUID(savedUrl.getId()).substring(0,7);

            savedUrl.setShortCode(shortCode);

            mainRepository.save(savedUrl);

        }

        return ShortenResponseDTO.builder()
                .shortCode(shortCode)
                .shortUrl(HOST_URL + shortCode)
                .originalUrl(url)
                .build();
    }


    @Cacheable(value = "redirects", key = "#shortCode")
    @TrackExecutionTime
    public String getURL(String shortCode){
        return mainRepository.findByShortCode(shortCode)
                .map(UrlInfo::getUrl)
                .orElse(null);
    }



    public GenerateQrCodeResponseDTO generateQrCode(GenerateQrCodeRequestDTO request){
        String shortUrl = shorten(ShortenRequestDTO.builder().url(request.getUrl()).build()).getShortUrl();

        Optional<String> qrCodePath = CommandRunner.runPythonScript(shortUrl);

        String qrCodePathStr = "";

        if(qrCodePath.isPresent()){
            qrCodePathStr = qrCodePath.get();
        }

        if(qrCodePathStr.isEmpty()){
            return GenerateQrCodeResponseDTO.builder()
                    .url(request.getUrl())
                    .shortUrl(shortUrl)
                    .build();
        }

        return GenerateQrCodeResponseDTO.builder()
                .url(request.getUrl())
                .shortUrl(shortUrl)
                .qrCodePath(qrCodePathStr)
                .build();
    }

    public List<UrlInfo> getAllURLs() {
        return mainRepository.findAll();
    }

    @CacheEvict(value = "urls", allEntries = true)
    @TrackExecutionTime
    public ResponseEntity<Void> truncateAllURLs() {
        mainRepository.truncateTable();
        return ResponseEntity.noContent().build();
    }
}
