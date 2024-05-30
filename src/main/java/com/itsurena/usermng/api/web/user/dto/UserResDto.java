package com.itsurena.usermng.api.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResDto {

    private String username;

    private String firstName;

    private String lastName;

}
