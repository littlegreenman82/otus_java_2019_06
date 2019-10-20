package ru.otus.hw12.cache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class HwCacheImpl<K, V> implements HwCache<K, V> {
    private static final String PUT_ACTION = "put";
    private static final String REMOVE_ACTION = "remove";

    private final WeakHashMap<K, V> cache;
    private List<WeakReference<HwCacheListener<K, V>>> listenerWeakRefList;

    public HwCacheImpl() {
        this.cache = new WeakHashMap<>();
        this.listenerWeakRefList = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listenerWeakRefList.forEach(weakReference -> {
            var listener = weakReference.get();
            if (listener != null)
                listener.notify(key, value, PUT_ACTION);
        });
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        listenerWeakRefList.forEach(weakReference -> {
            var listener = weakReference.get();
            if (listener != null)
                listener.notify(key, null, REMOVE_ACTION);
        });
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwCacheListener<K, V> listener) {
        if (listener != null) {
            var weakReference = new WeakReference<>(listener);
            listenerWeakRefList.add(weakReference);
        }
    }

    @Override
    public void removeListener(HwCacheListener<K, V> listener) {
        this.listenerWeakRefList = listenerWeakRefList.stream()
                .filter(weakReference -> weakReference.get() != listener)
                .collect(Collectors.toList());
    }

    public int size() {
        return cache.size();
    }
}
