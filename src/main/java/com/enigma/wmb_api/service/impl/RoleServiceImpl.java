package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repository.RoleRepository;
import com.enigma.wmb_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public Role getOrSave(UserRole role) {
        return repository.findByRole(role)
                .orElseGet(() -> repository.saveAndFlush(
                        Role.builder().role(role).build()
                ));
    }
}
