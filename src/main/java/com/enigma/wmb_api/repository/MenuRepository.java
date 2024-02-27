package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Menu;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    @Modifying
    @Query(value = "UPDATE m_menu SET status = :status WHERE id = :id", nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") Boolean status);

}
