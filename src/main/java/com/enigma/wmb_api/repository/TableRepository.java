package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<DiningTable, String> {
}
