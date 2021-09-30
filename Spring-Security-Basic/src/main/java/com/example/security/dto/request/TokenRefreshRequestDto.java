package com.example.security.dto.request;

import com.example.security.model.RefreshToken;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequestDto {
    @NotBlank
    private String refreshToken;

}
