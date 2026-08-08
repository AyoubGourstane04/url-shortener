package com.ayoub.url_shortener.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateQrCodeResponseDTO {
    private String url;
    private String shortUrl;
    private String qrCodePath;
}
