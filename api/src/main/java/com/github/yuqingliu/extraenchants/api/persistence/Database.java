package com.github.yuqingliu.extraenchants.api.persistence;

import java.io.File;

public interface Database {
    <T> void writeObject(File file, T object);
    <T> T readObject(File file, Class<T> clazz);
    void deleteObject(File file);
    <T> void writeAsyncObject(File file, T object);
    void deleteAsyncObject(File file);
}
