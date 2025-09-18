package com.questoftherealm.items;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ItemKeySerializer extends JsonSerializer<Item> {
    @Override
    public void serialize(Item item, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(item.getName());
    }
}

