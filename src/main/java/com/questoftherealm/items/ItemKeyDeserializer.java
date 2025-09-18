package com.questoftherealm.items;

import com.fasterxml.jackson.databind.KeyDeserializer;

public class ItemKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, com.fasterxml.jackson.databind.DeserializationContext ctxt) {
        return ItemRegistry.getItem(key); // lookup Item by name
    }
}
