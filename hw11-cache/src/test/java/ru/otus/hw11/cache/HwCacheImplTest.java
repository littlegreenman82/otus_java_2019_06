package ru.otus.hw11.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw11.entity.Address;
import ru.otus.hw11.entity.User;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Кэш должен ")
class HwCacheImplTest {
    private static final int TEST_ELEMENTS_COUNT = 30;
    private HwCacheImpl<String, User> cache;

    @BeforeEach
    void setUp() {
        this.cache = new HwCacheImpl<>();
    }

    @Test
    @DisplayName("корректно сохранять элементы")
    void put() {
        assertDoesNotThrow(this::fillCacheDummyData);
    }

    @Test
    @DisplayName("корретно удалять элементы")
    void remove() {
        fillCacheDummyData();

        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            cache.remove(String.valueOf(i));
        }

        assertEquals(0, cache.size());
    }

    @Test
    void get() {
        var user = new User("User#", new Address("Address"));
        cache.put(String.valueOf(1), user);

        assertEquals(user, cache.get(String.valueOf(1)));
    }

    @Test
    @DisplayName("очищаться по просьбе gc")
    void gcClearCache() throws InterruptedException {
        fillCacheDummyData();

        System.gc();
        Thread.sleep(500);

        var cachedValueCount = 0;
        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            var user = cache.get(String.valueOf(i));

            if (user != null) cachedValueCount++;
        }

        assertEquals(0, cachedValueCount);
    }

    private void fillCacheDummyData() {
        for (int i = 0; i < TEST_ELEMENTS_COUNT; i++) {
            var user = new User("User#" + i, new Address("Address"));
            cache.put(String.valueOf(i), user);
        }
    }
}