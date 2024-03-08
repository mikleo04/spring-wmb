package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.UserRole;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginResponse {

    private String email;

    private String token;

    private List<UserRole> role;

}
