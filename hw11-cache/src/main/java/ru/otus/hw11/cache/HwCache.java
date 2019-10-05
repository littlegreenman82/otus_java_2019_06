package ru.otus.hw11.cache;

public interface HwCache<K, V> {
    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwCacheListener<K, V> listener);

    void removeListener(HwCacheListener<K, V> listener);
}
