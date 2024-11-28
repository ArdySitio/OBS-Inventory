package com.ardy.test.inventory.presistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardy.test.inventory.presistence.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
