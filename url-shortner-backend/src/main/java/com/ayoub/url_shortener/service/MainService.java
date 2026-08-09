package com.ayoub.url_shortener.service;


import com.ayoub.url_shortener.dto.GenerateQrCodeRequestDTO;
import com.ayoub.url_shortener.dto.GenerateQrCodeResponseDTO;
import com.ayoub.url_shortener.dto.ShortenRequestDTO;
import com.ayoub.url_shortener.dto.ShortenResponseDTO;
import com.ayoub.url_shortener.entity.UrlInfo;
import com.ayoub.url_shortener.repository.MainRepository;
import com.ayoub.url_shortener.util.Base62;
import com.ayoub.url_shortener.util.CommandRunner;
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
    private final RedisTemplate<String, ShortenResponseDTO> redisTemplate;
    private static final String HOST_URL = "http://localhost:8080/api/v1/";

    public MainService(MainRepository mainRepository, RedisTemplate<String, ShortenResponseDTO> redisTemplate) {
        this.mainRepository = mainRepository;
        this.redisTemplate = redisTemplate;
    }


    public ShortenResponseDTO shorten(ShortenRequestDTO requestDTO) {
        long start = System.nanoTime();

        String url = requestDTO.getUrl();

        ShortenResponseDTO res = redisTemplate.opsForValue().get("Url: " + url);


        ShortenResponseDTO response;


        if(res == null){
            Optional<UrlInfo> existingUrl = mainRepository.findByUrl(url);

            if (existingUrl.isPresent()) {
                String code = existingUrl.get().getShortCode();


                response = ShortenResponseDTO.builder()
                            .shortCode(code)
                            .shortUrl(HOST_URL + code)
                            .originalUrl(url)
                            .build();

            }else{
                UrlInfo urlInfo = new UrlInfo();
                urlInfo.setUrl(url);

                UrlInfo savedUrl = mainRepository.save(urlInfo);

                String shortCode = Base62.encodeUUID(savedUrl.getId()).substring(0,7);

                savedUrl.setShortCode(shortCode);

                mainRepository.save(savedUrl);



                response = ShortenResponseDTO.builder()
                        .shortCode(shortCode)
                        .shortUrl(HOST_URL + savedUrl.getShortCode())
                        .originalUrl(url)
                        .build();

            }

//            redisTemplate.opsForValue().set("Url: " + url, response, Duration.ofMinutes(5));
            redisTemplate.opsForValue().set("Url: " + url, response, Duration.ofSeconds(50));

            long end = System.nanoTime();

            System.out.println(
                    "Response time: " + ((end - start) / 1_000_000.0) + " ms"
            );


            return response;
        }

        long end = System.nanoTime();

        System.out.println(
                "Response time: " + ((end - start) / 1_000_000.0) + " ms"
        );


        return res;

    }

    public ResponseEntity<Void> redirect(String shortCode) {
        Optional<UrlInfo> urlInfo = mainRepository.findByShortCode(shortCode);

        if(urlInfo.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlInfo.get().getUrl()))
                .build();
    }


    public GenerateQrCodeResponseDTO generateQrCode(GenerateQrCodeRequestDTO request){
        String shortUrl = shorten(ShortenRequestDTO.builder().url(request.getUrl()).build()).getShortUrl();

        //Optional<String> qrCodePath = CommandRunner.runPythonScript(shortUrl);
        Optional<String> qrCodePath = CommandRunner.runPythonScript(request.getUrl());

        String qrCodePathStr = "";

        if(qrCodePath.isPresent()){
            qrCodePathStr = qrCodePath.get();
        }

        if(qrCodePathStr.isEmpty()){
            return GenerateQrCodeResponseDTO.builder()
                    .url(request.getUrl())
                    .shortUrl(shortUrl)
                    .qrCodePath(null)
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

    public ResponseEntity<Void> truncateAllURLs() {
        long start = System.nanoTime();

        Set<String> keys = redisTemplate.keys("Url:*");

        long end = System.nanoTime();

        System.out.println(
                "Response time: " + ((end - start) / 1_000_000.0) + " ms"
        );

        if(!keys.isEmpty()){
            redisTemplate.delete(keys);
        }

        mainRepository.truncateTable();
        return ResponseEntity.noContent().build();
    }
}
