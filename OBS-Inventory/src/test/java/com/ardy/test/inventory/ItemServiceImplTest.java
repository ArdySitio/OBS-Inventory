package com.ardy.test.inventory;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ardy.test.inventory.constants.ErrorType;
import com.ardy.test.inventory.exception.AppException;
import com.ardy.test.inventory.model.request.ItemRequest;
import com.ardy.test.inventory.model.response.ItemResponse;
import com.ardy.test.inventory.presistence.repository.ItemRepository;
import com.ardy.test.inventory.service.ItemService;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemServiceImplTest {

	@Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;
    
    @BeforeEach
    public void setup() {
    	  itemRepository.deleteAll(); 
    }

    @Test
    public void testSaveItemSuccessfully() {
       
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setName("Test Item");
            itemRequest.setPrice(100);

            ItemResponse savedItem = itemService.saveItem(itemRequest);
            assertNotNull(savedItem.getId());
            assertEquals("Test Item", savedItem.getName());
            assertEquals(100, savedItem.getPrice());
            assertEquals(0, savedItem.getStock()); 
        
    }


    @Test
    public void testSaveItemWithNullName() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName(null); 
        itemRequest.setPrice(100);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            itemService.saveItem(itemRequest);
        });

        String expectedMessage = "Name is mandatory";
        assertEquals(expectedMessage, exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    public void testSaveItemWithNullPrice() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Test Item");
        itemRequest.setPrice(null); 

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            itemService.saveItem(itemRequest);
        });

        String expectedMessage = "Price is mandatory";
        assertEquals(expectedMessage, exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    public void testGetItemById() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Test Item");
        itemRequest.setPrice(100);

        ItemResponse savedItem = itemService.saveItem(itemRequest);
        ItemResponse fetchedItem = itemService.getItemById(savedItem.getId());

        assertNotNull(fetchedItem);
        assertEquals(savedItem.getId(), fetchedItem.getId());
        assertEquals("Test Item", fetchedItem.getName());
        assertEquals(100, fetchedItem.getPrice());
    }

    @Test
    public void testGetNonExistingItemById() {
        AppException exception = assertThrows(AppException.class, () -> {
            itemService.getItemById(999L); 
        });

        assertEquals(ErrorType.ITEM_NOT_FOUND, exception.getErrorType());
    }

    @Test
    public void testUpdateItemSuccessfully() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Old Item");
        itemRequest.setPrice(100);

        ItemResponse savedItem = itemService.saveItem(itemRequest);

        ItemRequest updateRequest = new ItemRequest();
        updateRequest.setName("Updated Item");
        updateRequest.setPrice(150);

        ItemResponse updatedItem = itemService.updateItem(savedItem.getId(), updateRequest);
        assertEquals(savedItem.getId(), updatedItem.getId());
        assertEquals("Updated Item", updatedItem.getName());
        assertEquals(150, updatedItem.getPrice());
    }

    @Test
    public void testDeleteItem() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Item to Delete");
        itemRequest.setPrice(50);

        ItemResponse savedItem = itemService.saveItem(itemRequest);
        itemService.deleteItem(savedItem.getId());

        AppException exception = assertThrows(AppException.class, () -> {
            itemService.getItemById(savedItem.getId());
        });

        assertEquals(ErrorType.ITEM_NOT_FOUND, exception.getErrorType());
    }
}
