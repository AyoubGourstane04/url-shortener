package com.ayoub.url_shortener.service;


import com.ayoub.url_shortener.dto.GenerateQrCodeRequestDTO;
import com.ayoub.url_shortener.dto.GenerateQrCodeResponseDTO;
import com.ayoub.url_shortener.dto.ShortenRequestDTO;
import com.ayoub.url_shortener.dto.ShortenResponseDTO;
import com.ayoub.url_shortener.entity.UrlInfo;
import com.ayoub.url_shortener.repository.MainRepository;
import com.ayoub.url_shortener.util.Base62;
import com.ayoub.url_shortener.util.CommandRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {
    private final MainRepository mainRepository;
    private static final String HOST_URL = "http://localhost:8080/api/v1/";

    public MainService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }


    public ShortenResponseDTO shorten(ShortenRequestDTO requestDTO) {
        String url = requestDTO.getUrl();

        Optional<UrlInfo> existingUrl = mainRepository.findByUrl(url);
        if (existingUrl.isPresent()) {
            String code = existingUrl.get().getShortCode();

            return ShortenResponseDTO.builder()
                    .shortCode(code)
                    .shortUrl(HOST_URL + code)
                    .build();
        }

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setUrl(url);

        UrlInfo savedUrl = mainRepository.save(urlInfo);


        String shortCode = Base62.encode(savedUrl.getId());

        savedUrl.setShortCode(shortCode);

        mainRepository.save(savedUrl);


        return ShortenResponseDTO.builder()
                .shortCode(shortCode)
                .shortUrl(HOST_URL + savedUrl.getShortCode())
                .build();
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
        mainRepository.truncateTable();
        return ResponseEntity.noContent().build();
    }
}
