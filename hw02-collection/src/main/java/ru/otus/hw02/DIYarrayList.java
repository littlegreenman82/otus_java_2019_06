package ru.otus.hw02;

import java.util.*;

@SuppressWarnings("unchecked")
public class DIYarrayList<E> implements List<E> {
    private              int      size             = 0;
    private static final int      DEFAULT_CAPACITY = 10;
    private              Object[] elements;

    public DIYarrayList() {
        this(0);
    }

    public DIYarrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elements = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elements = new Object[DEFAULT_CAPACITY];
        } else {
            throw new IllegalArgumentException("Illegal argument exception");
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > 0;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) grow();
        elements[size] = e;
        size += 1;

        return true;
    }

    private void grow() {
        elements = Arrays.copyOf(elements, DEFAULT_CAPACITY + elements.length);
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].equals(o)) {
                fastRemove(elements, i);
                break;
            }
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, elements.length);

        return (E) elements[index];
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, elements.length);

        return (E) (elements[index] = element);
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, elements.length);

        E oldValue = (E) elements[index];
        fastRemove(elements, index);

        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = elements.length - 1; i == 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = elements.length - 1; i == 0; i--) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void fastRemove(Object[] es, int i) {
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class DIYIterator implements ListIterator<E> {
        int nextIndex;
        int lastIndex = -1;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (nextIndex >= size)
                throw new NoSuchElementException();

            E el = (E) elements[nextIndex];
            lastIndex = nextIndex;
            nextIndex++;

            return el;
        }

        @Override
        public boolean hasPrevious() {
            return lastIndex >= 0;
        }

        @Override
        public E previous() {
            if (lastIndex <= 0)
                throw new NoSuchElementException();

            E el = (E) elements[lastIndex];
            nextIndex = lastIndex;
            lastIndex--;

            return el;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return lastIndex;
        }

        @Override
        public void remove() {
            if (lastIndex < 0)
                throw new NoSuchElementException();

            DIYarrayList.this.remove(lastIndex);
            nextIndex--;
            lastIndex--;
        }

        @Override
        public void set(E e) {
            DIYarrayList.this.set(lastIndex, e);
        }

        @Override
        public void add(E e) {
            DIYarrayList.this.add(e);
        }
    }
}
