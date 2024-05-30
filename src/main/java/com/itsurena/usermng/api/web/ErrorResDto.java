package com.itsurena.usermng.api.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ErrorResDto {
    private int statusCode;
    private String message;
}
