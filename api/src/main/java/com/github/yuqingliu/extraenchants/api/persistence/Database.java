package com.github.yuqingliu.extraenchants.api.persistence;

import java.io.File;

public interface Database {
    <T> void writeObject(File file, T object);
    <T> T readObject(File file, Class<T> clazz);
    void deleteObject(File file);
}
