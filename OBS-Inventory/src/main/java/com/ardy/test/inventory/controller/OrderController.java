package com.ardy.test.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ardy.test.inventory.model.request.OrderRequest;
import com.ardy.test.inventory.model.response.ApiResponse;
import com.ardy.test.inventory.model.response.OrderResponse;
import com.ardy.test.inventory.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
    private OrderService orderService;
	
	@GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> listOrders(Pageable pageable) {
        Page<OrderResponse> orders = orderService.listOrders(pageable);
        ApiResponse<Page<OrderResponse>> response = new ApiResponse<>("success", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        ApiResponse<OrderResponse> response = new ApiResponse<>("success", order);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest order) {
        OrderResponse savedOrder = orderService.saveOrder(order);
        ApiResponse<OrderResponse> response = new ApiResponse<>("success", savedOrder);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderDetails) {
        OrderResponse updatedOrder = orderService.updateOrder(id, orderDetails);
        ApiResponse<OrderResponse> response = new ApiResponse<>("success", updatedOrder);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        ApiResponse<String> response = new ApiResponse<>("success", "Success delete order");
        return ResponseEntity.ok(response);
    }
}
