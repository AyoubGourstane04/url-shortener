package com.ayoub.url_shortener.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenResponseDTO implements Serializable {
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
}
