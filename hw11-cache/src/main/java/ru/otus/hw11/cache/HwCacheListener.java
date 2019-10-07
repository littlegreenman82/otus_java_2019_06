package ru.otus.hw11.cache;

@FunctionalInterface
public interface HwCacheListener<K, V> {
    void notify(K key, V value, String action);
}
