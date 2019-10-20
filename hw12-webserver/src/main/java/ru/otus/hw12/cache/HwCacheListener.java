package ru.otus.hw12.cache;

@FunctionalInterface
public interface HwCacheListener<K, V> {
    void notify(K key, V value, String action);
}
