package com.github.yuqingliu.extraenchants.persistence;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yuqingliu.extraenchants.api.persistence.Database;

public abstract class AbstractDatabase implements Database {
    protected final File rootDirectory;
    protected final ObjectMapper objectMapper;

    public AbstractDatabase(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.objectMapper = new ObjectMapper();
    }

    public <T> void writeObjects(File file, List<T> objects) {
        try {
            objectMapper.writeValue(file, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> readObjects(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> void addObject(File file, T object, Class<T> clazz) {
        List<T> objects = readObjects(file, clazz);
        if (objects == null) {
            objects = Arrays.asList(object);
            writeObjects(file, objects);
        } else {
            objects.add(object);
            writeObjects(file, objects);
        }
    }

    public <T> void removeObject(File file, T object, Class<T> clazz) {
        List<T> objects = readObjects(file, clazz);
        if (objects == null || objects.isEmpty()) {
            return;
        }
        boolean removed = objects.remove(object);
        if (removed) {
            writeObjects(file, objects);
        }
    }

    public <T> void writeObject(File file, T object) {
        try {
            objectMapper.writeValue(file, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T readObject(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteObject(File file) {
        if (file.exists()) {
            file.delete();
        } 
    }

    @Override
    public abstract void initialize();
}
