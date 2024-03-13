package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.SearchUSerAccountResquest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    Authentication authentication;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userAccountRepository, passwordEncoder);
        Authentication authenticationSet = new UsernamePasswordAuthenticationToken(
                "username@gmail.com",
                "password"
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationSet);
        authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    void shouldReturnUserDetailsWhenLoadByUsername() {
        //Given
        String username = "username@gmail.com";
        List<Role> roles = List.of(
                Role.builder()
                        .id("Role-01")
                        .role(UserRole.ROLE_CUSTOMER)
                        .build(),
                Role.builder()
                        .id("Role-02")
                        .role(UserRole.ROLE_ADMIN)
                        .build()
        );

        UserAccount returnUserAccount = UserAccount.builder()
                .id("Username-01")
                .email("username@gmail.com")
                .password("password")
                .isEnable(true)
                .role(roles)
                .build();

        //Stubbing
        Mockito.when(userAccountRepository.findByEmail(username))
                .thenReturn(Optional.ofNullable(returnUserAccount));

        //When
        UserDetails actualUserDetails = userService.loadUserByUsername(username);

        //Then
        assertEquals(username, actualUserDetails.getUsername());
    }

    @Test
    void shouldThrowUsernameNotFoundWhenNotFound() {
        //Given
        String username = "username@gmail.com";

        //Stubbing
        Mockito.when(userAccountRepository.findByEmail(username))
                .thenThrow(UsernameNotFoundException.class);

        //When//Then
       assertThrows(UsernameNotFoundException.class, () -> {
           userService.loadUserByUsername(username);
       });
    }

    @Test
    void getByuserId() {
        //Given
        String id = "UserAccount-01";
        List<Role> roles = List.of(
                Role.builder()
                        .id("Role-01")
                        .role(UserRole.ROLE_CUSTOMER)
                        .build(),
                Role.builder()
                        .id("Role-02")
                        .role(UserRole.ROLE_ADMIN)
                        .build()
        );

        UserAccount returnUserAccount = UserAccount.builder()
                .id("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .isEnable(true)
                .role(roles)
                .build();

        //Stubbing
        Mockito.when(userAccountRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnUserAccount));

        //When
        UserAccount actualUserAccount = userService.getByuserId(id);

        //Then
        assertEquals(id, actualUserAccount.getId());

    }

    @Test
    void shouldThrowResponseStatusExceptionWhenNotFound() {
        //Given
        String id = "UserAccount-01";

        //Stubbing
        Mockito.when(userAccountRepository.findById(id))
                .thenThrow(ResponseStatusException.class);

        //When //Then
        assertThrows(ResponseStatusException.class, () -> {
            userService.getByuserId(id);
        });
        assertThrows(ResponseStatusException.class, () -> {
            userService.getOneById(id);
        });

    }

    @Test
    void shouldReturnUserAccountResponseWhenGetOneById() {
        //Given
        String id = "UserAccount-01";
        List<Role> roles = List.of(
                Role.builder()
                        .id("Role-01")
                        .role(UserRole.ROLE_CUSTOMER)
                        .build(),
                Role.builder()
                        .id("Role-02")
                        .role(UserRole.ROLE_ADMIN)
                        .build()
        );

        UserAccount returnUserAccount = UserAccount.builder()
                .id("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .isEnable(true)
                .role(roles)
                .build();

        //Stubbing
        Mockito.when(userAccountRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnUserAccount));

        //When
        UserAccountResponse actualUserAccountResponse = userService.getOneById(id);

        //Then
        assertEquals(id, actualUserAccountResponse.getId());
    }

    @Test
    void shouldReturnLisUserAccountResponseWhenGetAll() {
        //Given
        SearchUSerAccountResquest accountResquest = SearchUSerAccountResquest.builder()
                .direction("ASC")
                .page(1)
                .size(10)
                .sortBy("email")
                .build();

        List<UserAccount> userAccountList = List.of(
                UserAccount.builder()
                        .id("UserAccount-01")
                        .email("user@gmail.com")
                        .password("password")
                        .isEnable(true)
                        .role(List.of(Role.builder().id("Role-01").role(UserRole.ROLE_CUSTOMER).build()))
                        .build(),
                UserAccount.builder()
                        .id("UserAccount-02")
                        .email("user@gmail.com")
                        .password("password")
                        .isEnable(true)
                        .role(List.of(Role.builder().id("Role-01").role(UserRole.ROLE_CUSTOMER).build()))
                        .build()
        );

        Page<UserAccount> userAccounts = new PageImpl<>(userAccountList);

        //Stubbing
        Mockito.when(userAccountRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(userAccounts);

        //When
        Page<UserAccountResponse> actualUserAccountResponses = userService.getAll(accountResquest);

        //Then
        assertEquals(userAccounts.getSize(), actualUserAccountResponses.getSize());

    }

    @Test
    void shouldReturnUserAccountWhenGetByContext() {
        //Given
        UserAccount returnUserAccount = UserAccount.builder()
                .id("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .isEnable(true)
                .role(List.of(Role.builder().id("Role-01").role(UserRole.ROLE_CUSTOMER).build()))
                .build();

        Mockito.when(userAccountRepository.findByEmail(authentication.getPrincipal().toString()))
                .thenReturn(Optional.ofNullable(returnUserAccount));

        //When
        UserAccount actualUserAccount = userService.getByContext();

        //Then
        assertEquals(returnUserAccount.getId(), actualUserAccount.getId());
        assertEquals(returnUserAccount.getEmail(), actualUserAccount.getEmail());
    }

    @Test
    void shouldCallSaveAndFlushOnceWhenUpdateEmailOrPassword() {
        //Given
        UpdateUserAccountRequest userAccountRequest = UpdateUserAccountRequest.builder()
                .userAccountId("UserAccount-01")
                .email("newusername@gamil.com")
                .password("newpassword")
                .build();

        UserAccount userAccount = UserAccount.builder()
                .id("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .role(List.of(Role.builder().id("Role-01").role(UserRole.ROLE_CUSTOMER).build()))
                .isEnable(true)
                .build();

        Mockito.when(userAccountRepository.findById(userAccountRequest.getUserAccountId()))
                .thenReturn(Optional.ofNullable(userAccount));

        assert userAccount != null;
        if (userAccountRequest.getEmail() != null) {
            userAccount.setEmail(userAccountRequest.getEmail());
        }
        if (userAccountRequest.getPassword() != null) {
            userAccount.setPassword(userAccountRequest.getPassword());
        }

        Mockito.when(userAccountRepository.saveAndFlush(userAccount))
                .thenReturn(userAccount);

        //When
        userService.updateEmailOrPassword(userAccountRequest);

        //Then
        Mockito.verify(userAccountRepository, Mockito.times(1)).saveAndFlush(userAccount);
        assertEquals(userAccountRequest.getEmail(), userAccount.getEmail());
    }

    @Test
    void updateIsEnable() {
        //Given
        String id = "UserAccount-01";
        Boolean isEnable = false;

        UserAccount returnUserAccount = UserAccount.builder()
                .id("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .role(List.of(Role.builder().id("Role-01").role(UserRole.ROLE_CUSTOMER).build()))
                .isEnable(true)
                .build();

        Mockito.when(userAccountRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnUserAccount));
        Mockito.doNothing().when(userAccountRepository).updateIsEnable(id, isEnable);

        //When
       userService.updateIsEnable(id, isEnable);

        //Then
        Mockito.verify(userAccountRepository, Mockito.times(1)).updateIsEnable(id, isEnable);

    }
}