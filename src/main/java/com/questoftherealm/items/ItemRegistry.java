package com.questoftherealm.items;

import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class ItemRegistry {
    private static List<Item> allItems = getAllItems();

    public static List<Item> getAllItems() {
        if (allItems == null) {
            try (InputStream is = ItemRegistry.class.getResourceAsStream("/items.json")) {
                ObjectMapper mapper = new ObjectMapper();
                allItems = mapper.readValue(is, new TypeReference<List<Item>>(){});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allItems;
    }

}
