package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String > {
}
