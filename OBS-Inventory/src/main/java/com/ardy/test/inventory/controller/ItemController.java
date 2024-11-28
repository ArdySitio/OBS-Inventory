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

import com.ardy.test.inventory.model.request.ItemRequest;
import com.ardy.test.inventory.model.response.ApiResponse;
import com.ardy.test.inventory.model.response.ItemResponse;
import com.ardy.test.inventory.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> listItems(Pageable pageable) {
        Page<ItemResponse> items = itemService.listItems(pageable);
        ApiResponse<Page<ItemResponse>> response = new ApiResponse<>("success", items);
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable Long id) {
        ItemResponse item = itemService.getItemById(id);
        ApiResponse<ItemResponse> response = new ApiResponse<>("success", item);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@Valid @RequestBody ItemRequest item) {
        ItemResponse savedItem = itemService.saveItem(item);
        ApiResponse<ItemResponse> response = new ApiResponse<>("success", "Success Add Item", savedItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest itemDetails) {
        ItemResponse updatedItem = itemService.updateItem(id, itemDetails);
        ApiResponse<ItemResponse> response = new ApiResponse<>("success", updatedItem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        ApiResponse<String> response = new ApiResponse<>("success", "Success Delete Item");
        return ResponseEntity.ok(response);
    }
}
