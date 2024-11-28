package com.ardy.test.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ardy.test.inventory.model.request.OrderRequest;
import com.ardy.test.inventory.model.response.OrderResponse;
import com.ardy.test.inventory.persistence.entity.Order;

import jakarta.validation.Valid;

public interface OrderService {

	Page<OrderResponse> listOrders(Pageable pageable);
	
	OrderResponse getOrderById(Long id);
	
	OrderResponse saveOrder(@Valid OrderRequest orderRequest);
	
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
    
    void deleteOrder(Long id);
    
    Order getEntityById(Long id);
}
