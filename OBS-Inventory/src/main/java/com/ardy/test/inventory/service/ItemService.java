package com.ardy.test.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ardy.test.inventory.model.request.ItemRequest;
import com.ardy.test.inventory.model.response.ItemResponse;
import com.ardy.test.inventory.presistence.entity.Item;

import jakarta.validation.Valid;

public interface ItemService {

	Page<ItemResponse> listItems(Pageable pageable);
	
	ItemResponse getItemById(Long id);
	
	Item getEntityById(Long id);
	
	ItemResponse saveItem(@Valid ItemRequest item);
	
    ItemResponse updateItem(Long id, ItemRequest itemDetails);
    
    void updateStockItem (Item item);
    
    void deleteItem(Long id);
    
    Integer getRemainingStock(Long id);
}
