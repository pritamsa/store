package com.store.services.webapi.datastore;

public class SingletonClass {
    public int val;
    private SingletonClass() {}
    private static class SingletonClassHolder {
        static final SingletonClass SINGLE_INSTANCE = new SingletonClass();
    }
    public static SingletonClass getInstance() {
        return SingletonClassHolder.SINGLE_INSTANCE;
    }
}
