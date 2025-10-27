package com.questoftherealm.items;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.questoftherealm.exceptions.ItemNotFound;


public class ItemRegistry {
    private static List<Item> allItems;

    public static List<Item> getAllItems() {
        if (allItems == null) {
            try (InputStream is = ItemRegistry.class.getResourceAsStream("/items.json")) {
                if (is == null) {
                    throw new FileNotFoundException("items.json not found in a");
                }
                ObjectMapper mapper = new ObjectMapper();
                allItems = mapper.readValue(is, new TypeReference<List<Item>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
                allItems = Collections.emptyList();
            }
        }
        return allItems;
    }

    //Null pointer except
    public static Item getItem(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be null or blank");
        }
        for (Item i : getAllItems()) {
            if (i != null && name.equals(i.getName())) {
                return i;
            }
        }
        throw new ItemNotFound("Item '" + name + "' not found");
    }


}
