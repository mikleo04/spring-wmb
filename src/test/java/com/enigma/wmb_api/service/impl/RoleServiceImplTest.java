package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repository.RoleRepository;
import com.enigma.wmb_api.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void shouldReturnRoleWhenGet() {
        //Given
        UserRole role = UserRole.ROLE_CUSTOMER;

        Role returnRole = Role.builder()
                .id("Role-01")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        //stubbing
        Mockito.when(roleRepository.findByRole(role))
                .thenReturn(Optional.ofNullable(returnRole));

        //When
        Optional<Role> actualRole = roleRepository.findByRole(role);

        //Then
        assertEquals(role, actualRole.get().getRole());
        Mockito.verify(roleRepository, Mockito.times(1)).findByRole(role);
    }

    @Test
    void shouldReturnRoleWhenSave() {
        //Given
        UserRole role = UserRole.ROLE_CUSTOMER;

        Role returnRole = Role.builder()
                .id("Role-01")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        //stubbing
        Mockito.when(roleRepository.saveAndFlush(returnRole))
                .thenReturn(returnRole);

        //When
        Role actualRole = roleRepository.saveAndFlush(returnRole);

        //Then
        assertEquals(role, actualRole.getRole());
        Mockito.verify(roleRepository, Mockito.times(1)).saveAndFlush(returnRole);
    }

}