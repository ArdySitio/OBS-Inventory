package com.ardy.test.inventory.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ardy.test.inventory.constants.ErrorType;
import com.ardy.test.inventory.exception.AppException;
import com.ardy.test.inventory.model.request.OrderRequest;
import com.ardy.test.inventory.model.response.OrderResponse;
import com.ardy.test.inventory.presistence.entity.Item;
import com.ardy.test.inventory.presistence.entity.Order;
import com.ardy.test.inventory.presistence.repository.OrderRepository;

import jakarta.validation.Valid;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ItemService itemService;
	
	private OrderResponse mapToResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setId(order.getId());
		response.setItemId(order.getItem().getId());
		response.setQuantity(order.getQuantity());
		response.setPrice(order.getPrice());
		return response;
	}
	
	@Override
	public Page<OrderResponse> listOrders(Pageable pageable) {
		Page<Order> orders = orderRepository.findAll(pageable);
		return new PageImpl<>(orders.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()),
				pageable, orders.getTotalElements());
	}

	@Override
	public OrderResponse getOrderById(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new AppException("ORDER NOT FOUND", ErrorType.ORDER_NOT_FOUND));
		return mapToResponse(order);
	}

	@Override
	public OrderResponse saveOrder(@Valid OrderRequest orderRequest) {
		Item item = itemService.getEntityById(orderRequest.getItemId());

		if (item.getStock() < orderRequest.getQuantity()) {
			throw new AppException("Insufficient Stock", ErrorType.INSUFFICIENT_STOCK);
		}

		item.setStock(item.getStock() - orderRequest.getQuantity());
		itemService.updateStockItem(item);

		Order order = new Order();
		order.setItem(item);
		order.setQuantity(orderRequest.getQuantity());
		order.setPrice(orderRequest.getPrice());
		Order savedOrder = orderRepository.save(order);
		return mapToResponse(savedOrder);
	}

	@Override
	public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
		Order existingOrder = orderRepository.findById(id)
	            .orElseThrow(() -> new AppException("ORDER NOT FOUND", ErrorType.ORDER_NOT_FOUND));

	    Item item = existingOrder.getItem();

	    int qtyDif = orderRequest.getQuantity() - existingOrder.getQuantity();
	    if (qtyDif > 0) { 
	        if (item.getStock() < qtyDif) {
	            throw new AppException("Insufficient Stock", ErrorType.INSUFFICIENT_STOCK);
	        }
	        item.setStock(item.getStock() - qtyDif); 
	    } else if (qtyDif < 0) { 
	        item.setStock(item.getStock() + Math.abs(qtyDif)); 
	    }

	    itemService.updateStockItem(item);

	    existingOrder.setQuantity(orderRequest.getQuantity());
	    existingOrder.setPrice(orderRequest.getPrice());
	    Order updatedOrder = orderRepository.save(existingOrder);

	    return mapToResponse(updatedOrder);
	}

	@Override
	public void deleteOrder(Long id) {
		Order order = getEntityById(id);
		orderRepository.delete(order);
	}

	@Override
	public Order getEntityById(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new AppException("ORDER NOT FOUND", ErrorType.ORDER_NOT_FOUND));
	}

}
