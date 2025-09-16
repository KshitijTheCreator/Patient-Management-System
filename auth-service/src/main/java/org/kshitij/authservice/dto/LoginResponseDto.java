package org.kshitij.authservice.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private final String token;
    //could have used the setter (From lombok) but this is the cleaner as one prop only here
    public LoginResponseDto(String token) {
        this.token = token;
    }


}
