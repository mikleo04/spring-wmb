package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateUserAccountRequest {

    private String userAccountId;

    @NotBlank(message = "email is required")
    @Email( regexp = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$" ,message = "Invalid email")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must be at least 6 character and contains one uppercase and lowercase letter, one number, and one special character")
    private String password;
}
