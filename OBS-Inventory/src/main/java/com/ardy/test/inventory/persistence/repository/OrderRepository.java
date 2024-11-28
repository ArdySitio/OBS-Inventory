package com.ardy.test.inventory.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardy.test.inventory.persistence.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
