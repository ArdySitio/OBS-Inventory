package com.ardy.test.inventory.presistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardy.test.inventory.presistence.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	boolean existsByName(String name);
}
