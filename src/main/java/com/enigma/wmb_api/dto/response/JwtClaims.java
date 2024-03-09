package com.enigma.wmb_api.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class JwtClaims {

    private String userAccountId;

    private List<String> roles;

}
