package com.ardy.test.inventory.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ardy.test.inventory.constants.ErrorType;
import com.ardy.test.inventory.constants.TypeEnum;
import com.ardy.test.inventory.exception.AppException;
import com.ardy.test.inventory.model.request.InventoryRequest;
import com.ardy.test.inventory.model.response.InventoryResponse;
import com.ardy.test.inventory.presistence.entity.Inventory;
import com.ardy.test.inventory.presistence.entity.Item;
import com.ardy.test.inventory.presistence.repository.InventoryRepository;

import jakarta.validation.Valid;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
    private ItemService itemService;

    @Autowired
    private InventoryRepository inventoryRepository;
	
	private InventoryResponse mapToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setItemId(inventory.getItem().getId());
        response.setQuantity(inventory.getQuantity());
        response.setType(inventory.getType().name());
        return response;
    }
	
	@Override
	public Page<InventoryResponse> listInventories(Pageable pageable) {
		Page<Inventory> inventories = inventoryRepository.findAll(pageable);
        return new PageImpl<>(
                inventories.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()),
                pageable,
                inventories.getTotalElements()
        );
	}

	@Override
	public InventoryResponse getInventoryById(Long id) {
		Inventory inventory =  getEntityById(id);
        return mapToResponse(inventory);
	}

	@Override
	public Inventory getEntityById(Long id) {
		return inventoryRepository.findById(id)
                .orElseThrow(() -> new AppException("INVENTORY NOT FOUND : " + id, ErrorType.INVENTORY_NOT_FOUND));
	}

	@Override
	public InventoryResponse saveInventory(@Valid InventoryRequest inventoryRequest) {
		if (!TypeEnum.isValidType(inventoryRequest.getType())) {
	        throw new AppException("Type must be either 'T' (Top Up) or 'W' (Withdraw)", ErrorType.INVALID_TYPE);
	    }
	
	    Item item = itemService.getEntityById(inventoryRequest.getItemId());
	
	    Inventory inventory = new Inventory();
	    inventory.setItem(item);
	    inventory.setQuantity(inventoryRequest.getQuantity());
	    inventory.setType(inventoryRequest.getType().equals("T") ? TypeEnum.T : TypeEnum.W);
	
	    if (inventory.getType() == TypeEnum.T) {
	        item.setStock(item.getStock() + inventory.getQuantity());
	    } else if (inventory.getType() == TypeEnum.W) {
	        if (item.getStock() < inventory.getQuantity()) {
	            throw new AppException("Insufficient Stock for Withdrawal", ErrorType.INSUFFICIENT_STOCK);
	        }
	        item.setStock(item.getStock() - inventory.getQuantity());
	    }
	
	    itemService.updateStockItem(item);
	    Inventory savedInventory = inventoryRepository.save(inventory);
	    return mapToResponse(savedInventory);
	}

	@Override
	public InventoryResponse updateInventory(Long id, InventoryRequest inventoryRequest) {
		if (!TypeEnum.isValidType(inventoryRequest.getType())) {
	        throw new AppException("Type must be either 'T' (Top Up) or 'W' (Withdraw)", ErrorType.INVALID_TYPE);
	    }
		
		Inventory inventory =  getEntityById(id);

        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setType(inventoryRequest.getType().equals("T") ? TypeEnum.T : TypeEnum.W);

        Item item = inventory.getItem();
        if (inventory.getType() == TypeEnum.T) {
            item.setStock(item.getStock() + inventory.getQuantity());
        } else if (inventory.getType() == TypeEnum.W) {
            if (item.getStock() < inventory.getQuantity()) {
                throw new AppException("Insufficient Stock for Withdrawal", ErrorType.INSUFFICIENT_STOCK);
            }
            item.setStock(item.getStock() - inventory.getQuantity());
        }
        itemService.updateStockItem(item);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToResponse(updatedInventory);
	}

	@Override
	public void deleteInventory(Long id) {
		Inventory inventory = getEntityById(id);
        inventoryRepository.delete(inventory);
		
	}

}
