package com.questoftherealm.items;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;

public class ItemKeyDeserializer extends KeyDeserializer implements ContextualKeyDeserializer {

    private final ItemRegistry itemRegistry;

    public ItemKeyDeserializer() {
        this.itemRegistry = null;
    }

    private ItemKeyDeserializer(ItemRegistry registry) {
        this.itemRegistry = registry;
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctx) {
        if (itemRegistry == null) {
            throw new IllegalStateException("ItemRegistry not initialized in ItemKeyDeserializer");
        }
        return itemRegistry.getItem(key);
    }

    @Override
    public KeyDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        ItemRegistry registry = (ItemRegistry) ctxt.findInjectableValue(ItemRegistry.class.getName(), property, null);
        return new ItemKeyDeserializer(registry);
    }
}
