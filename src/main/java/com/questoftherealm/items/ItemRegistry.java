package com.questoftherealm.items;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class ItemRegistry {
    private static List<Item> allItems;

    public static List<Item> getAllItems() {
        if (allItems == null) {
            try (InputStream is = ItemRegistry.class.getResourceAsStream("/items.json")) {
                if (is == null) {
                    throw new FileNotFoundException("items.json not found in resources");
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
        Item search = null;//here
        for (Item i:getAllItems()){
            if(i.getName().equals(name)){
                search=i;
            }
        }
        return search;
    }


}
