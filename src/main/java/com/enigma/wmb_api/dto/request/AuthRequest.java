package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthRequest {

    @Email( regexp = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$" ,message = "Invalid email")
    private String email;

    private String password;

}
