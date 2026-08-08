package com.ayoub.url_shortener.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortenRequestDTO {
    private String url;
}
