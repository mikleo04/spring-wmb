package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegisterResponse {

    private String email;

//    @Enumerated(EnumType.STRING)
    private List<UserRole> role;

}
