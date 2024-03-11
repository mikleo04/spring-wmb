package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String > {

    Optional<UserAccount> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE m_user_account SET is_enable = :isEnable WHERE id = :id", nativeQuery = true)
    void updateIsEnable(@Param("id") String id, @Param("isEnable") Boolean isEnable);

}
