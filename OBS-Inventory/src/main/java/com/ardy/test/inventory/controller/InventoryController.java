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

import com.ardy.test.inventory.model.request.InventoryRequest;
import com.ardy.test.inventory.model.response.ApiResponse;
import com.ardy.test.inventory.model.response.InventoryResponse;
import com.ardy.test.inventory.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> listInventories(Pageable pageable) {
        Page<InventoryResponse> inventories = inventoryService.listInventories(pageable);
        ApiResponse<Page<InventoryResponse>> response = new ApiResponse<>("success", inventories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(@PathVariable Long id) {
        InventoryResponse inventory = inventoryService.getInventoryById(id);
        ApiResponse<InventoryResponse> response = new ApiResponse<>("success", inventory);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(@Valid @RequestBody InventoryRequest inventory) {
        InventoryResponse savedInventory = inventoryService.saveInventory(inventory);
        ApiResponse<InventoryResponse> response = new ApiResponse<>("success", savedInventory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryRequest inventoryDetails) {
        InventoryResponse updatedInventory = inventoryService.updateInventory(id, inventoryDetails);
        ApiResponse<InventoryResponse> response = new ApiResponse<>("success", updatedInventory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        ApiResponse<String> response = new ApiResponse<>("success", "Success delete inventory");
        return ResponseEntity.ok(response);
    }
}
