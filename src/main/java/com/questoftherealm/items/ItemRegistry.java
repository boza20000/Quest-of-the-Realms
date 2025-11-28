package com.questoftherealm.items;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.exceptions.ItemRegistryError;
import com.questoftherealm.localization.LocalizationService;

public class ItemRegistry {
    private List<Item> allItems;
    private final LocalizationService service;

    public ItemRegistry(LocalizationService messageBundle) {
        this.service = messageBundle;
        allItems = loadAllItems();
    }

   private List<Item> loadAllItems() {
        if (allItems == null) {
            try (InputStream is = ItemRegistry.class.getResourceAsStream("/items.json")) {
                if (is == null) {
                    throw new FileNotFoundException(service.getBundle().get("itemRegistry.fileNotFound"));
                }
                ObjectMapper mapper = new ObjectMapper();
                allItems = mapper.readValue(is, new TypeReference<>() {
                });
            } catch (Exception e) {
                allItems = Collections.emptyList();
                throw new ItemRegistryError(service.getBundle().get("itemRegistry.loadError", e.getMessage()));
            }
        }
        return allItems;
    }

    public Item getItem(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(service.getBundle().get("itemRegistry.nullName"));
        }
        for (Item i : getAllItems()) {
            if (i != null && name.equals(i.getName())) {
                return i;
            }
        }
        throw new ItemNotFound(service.getBundle().get("itemRegistry.notFound", name));
    }

    public synchronized List<Item> getAllItems(){
        return allItems;
    }
}
