package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email( regexp = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$" ,message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 character longs")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must be at least 6 character and contains one uppercase and lowercase letter, one number, and one special character")
    private String password;

}
