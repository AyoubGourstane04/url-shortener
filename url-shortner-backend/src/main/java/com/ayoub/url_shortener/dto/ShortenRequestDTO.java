package com.ayoub.url_shortener.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortenRequestDTO {
    @NotBlank
    @Size(max = 2048)
    @Pattern(
            regexp = "^(https?://)([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(:[0-9]{1,5})?(/[^\\s]*)?$",
            message = "Invalid URL"
    )
    private String url;
}
