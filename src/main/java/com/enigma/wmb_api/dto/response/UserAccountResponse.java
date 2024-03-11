package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAccountResponse {
    private String id;

    private String email;

    private String password;

    private List<UserRole> roles;

    private Boolean isEnable;

}
