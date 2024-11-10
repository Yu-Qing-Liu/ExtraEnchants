package com.github.yuqingliu.extraenchants.persistence;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.persistence.Database;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public abstract class AbstractDatabase implements Database {
    protected final File rootDirectory;
    protected final ObjectMapper objectMapper;

    public class ComponentSerializer extends JsonSerializer<Component> {
        @Override
        public void serialize(Component component, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String jsonString = GsonComponentSerializer.gson().serialize(component);
            gen.writeString(jsonString);
        }
    }

    public class ComponentDeserializer extends JsonDeserializer<Component> {
        @Override
        public Component deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String jsonString = p.getText();
            return GsonComponentSerializer.gson().deserialize(jsonString);
        }
    }
    
    public class TextColorSerializer extends JsonSerializer<TextColor> {
        @Override
        public void serialize(TextColor value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.asHexString());
        }
    }

    public class TextColorDeserializer extends JsonDeserializer<TextColor> {
        @Override
        public TextColor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String colorString = p.getValueAsString();
            if (colorString.startsWith("#")) {
                return TextColor.fromHexString(colorString);
            }
            return NamedTextColor.WHITE;
        }
    }

    public AbstractDatabase(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Component.class, new ComponentSerializer());
        module.addDeserializer(Component.class, new ComponentDeserializer());
        module.addSerializer(TextColor.class, new TextColorSerializer());
        module.addDeserializer(TextColor.class, new TextColorDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    public <T> void writeObject(File file, T object) {
        try {
            objectMapper.writeValue(file, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public <T> T readObject(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void deleteObject(File file) {
        if (file.exists()) {
            file.delete();
        } 
    }

    @Override
    public <T> void writeAsyncObject(File file, T object) {
        Scheduler.runAsync(t -> {
            try {
                objectMapper.writeValue(file, object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    @Override
    public void deleteAsyncObject(File file) {
        Scheduler.runAsync(t -> {
            if (file.exists()) {
                file.delete();
            } 
        });
    }
}
