package com.ardy.test.inventory.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ardy.test.inventory.constants.ErrorType;
import com.ardy.test.inventory.exception.AppException;
import com.ardy.test.inventory.model.request.ItemRequest;
import com.ardy.test.inventory.model.response.ItemResponse;
import com.ardy.test.inventory.persistence.entity.Item;
import com.ardy.test.inventory.persistence.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	private void isBkExists(String name) {
		if (itemRepository.existsByName(name)) {
			throw new AppException("Name of Item already Used. Please try with another name", ErrorType.INVALID_REQUEST);
		}
	}

	@Override
	public Page<ItemResponse> listItems(Pageable pageable) {
		Page<Item> items = itemRepository.findAll(pageable);
		return new PageImpl<>(
				items.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()), 
				pageable, 
				items.getTotalElements()
			);
	}
	
	@Override
	public ItemResponse getItemById(Long id) {
		Item item = getEntityById(id);
    	return mapToResponse(item);
	}

	@Override
	public Item getEntityById(Long id) {
		return itemRepository.findById(id)
                .orElseThrow(() -> new AppException("ITEM NOT FOUND", ErrorType.ITEM_NOT_FOUND));
	}

	@Override
	public ItemResponse saveItem(ItemRequest itemRequests) {
		isBkExists(itemRequests.getName());
		Item item = new Item();
		item.setName(itemRequests.getName());
		item.setPrice(itemRequests.getPrice());
		item.setStock(0);
		itemRepository.save(item);
		
		return mapToResponse(item);
	}

	@Override
	public ItemResponse updateItem(Long id, ItemRequest itemRequest) {
		isBkExists(itemRequest.getName());
    	Item item = getEntityById(id);
        item.setName(itemRequest.getName());
        item.setPrice(itemRequest.getPrice());
        itemRepository.save(item);
        
        return mapToResponse(item);
	}

	@Override
	public void updateStockItem(Item item) {
		itemRepository.save(item);
	}

	@Override
	public void deleteItem(Long id) {
		Item item = getEntityById(id);
        itemRepository.delete(item);
	}

	@Override
	public Integer getRemainingStock(Long id) {
		Item item = getEntityById(id);
        return item.getStock();
	}
	
	private ItemResponse mapToResponse(Item item) {
    	ItemResponse itemRes = new ItemResponse();
    	itemRes.setId(item.getId());
    	itemRes.setName(item.getName());
    	itemRes.setPrice(item.getPrice());
    	itemRes.setStock(item.getStock());
        return itemRes;
    }

}
