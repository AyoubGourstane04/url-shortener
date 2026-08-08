package com.ayoub.url_shortener.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateQrCodeRequestDTO {
    private String url;

}
