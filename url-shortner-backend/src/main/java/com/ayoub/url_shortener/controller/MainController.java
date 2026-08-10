package com.ayoub.url_shortener.controller;

import com.ayoub.url_shortener.dto.GenerateQrCodeRequestDTO;
import com.ayoub.url_shortener.dto.GenerateQrCodeResponseDTO;
import com.ayoub.url_shortener.dto.ShortenRequestDTO;
import com.ayoub.url_shortener.dto.ShortenResponseDTO;
import com.ayoub.url_shortener.entity.UrlInfo;
import com.ayoub.url_shortener.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = {"http://localhost:5173/", "http://127.0.0.1:5173/"})
public class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping("/shorten")
    public ShortenResponseDTO shorten(@RequestBody ShortenRequestDTO requestDTO){
        return mainService.shorten(requestDTO);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String url = mainService.getURL(shortCode);

        if(url == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/qr")
    public GenerateQrCodeResponseDTO generateQrCode(@RequestBody GenerateQrCodeRequestDTO request){
        return mainService.generateQrCode(request);
    }

    @GetMapping
    public List<UrlInfo> getAllURLs(){
        if (mainService.getAllURLs().isEmpty()){
            return List.of();
        }

        return mainService.getAllURLs();

    }

    @DeleteMapping("/truncate")
    public ResponseEntity<Void> truncateAllURLs(){
        return mainService.truncateAllURLs();
    }






}
