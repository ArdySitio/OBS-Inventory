package com.ardy.test.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ardy.test.inventory.model.request.InventoryRequest;
import com.ardy.test.inventory.model.response.InventoryResponse;
import com.ardy.test.inventory.presistence.entity.Inventory;

import jakarta.validation.Valid;

public interface InventoryService {
	
	Page<InventoryResponse> listInventories(Pageable pageable);

	InventoryResponse getInventoryById(Long id);
	
	Inventory getEntityById(Long id);
	
	InventoryResponse saveInventory(@Valid InventoryRequest inventory);

	InventoryResponse updateInventory(Long id, InventoryRequest inventoryDetails);

	void deleteInventory(Long id);

}
