package ua.nure.bain.Practice2;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyListImpl implements MyList, ListIterable {

    private final int INIT_SIZE = 10;
    private Object[] array = new Object[INIT_SIZE];
    private int size = 0;

    @Override
    public void add(Object e) {
        if (size == array.length - 1) {
            reSize(array.length * 2);
        }
        array[size++] = e;
    }

    @Override
    public void clear() {
        array = new Object[INIT_SIZE];
        size = 0;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < array.length; i++)
                if (array[i] == null) {
                    supportRemove(i);
                    size--;
                    return true;
                }
        } else {
            for (int i = 0; i < array.length; i++)
                if (o.equals(array[i])) {
                    supportRemove(i);
                    size--;
                    return true;
                }
        }
        return false;

    }

    private void supportRemove(int i) {
        int numMove = array.length - i - 1;
        if (numMove > 0) {
            System.arraycopy(array, i + 1, array, i,
                    numMove);
        }
        array[array.length - 1] = null;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        System.arraycopy(array, 0, result, 0, size());
        return result;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(Object o) {
        for (Object o1 : array) {
            if (o != null) {
                if (o.equals(o1)) {
                    return true;
                }
            } else {
                if (o1 == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(MyList list) {
        for (Object o : list) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    private void reSize(int newLength) {
        Object[] newArray = new Object[newLength];
        System.arraycopy(array, 0, newArray, 0, size());
        array = newArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (size() != 0) {
            sb.append('[');
        } else sb.append("[]");
        String[] arrayResult = Arrays.toString(array)
                .replace("[", "")
                .replace("]", "")
                .split(", ");
        for (int i = 0; i < size(); i++) {
            if (i != size() - 1) {
                sb.append(arrayResult[i]).append(", ");
            } else {
                sb.append(arrayResult[i]).append("]");
            }
        }
        return sb.toString();
    }

    private class IteratorImpl implements Iterator<Object> {
        int cursor;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != size;
        }

        public Object next() {
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            cursor++;
            return MyListImpl.this.array[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                cursor--;
                MyListImpl.this.remove(next());
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorImpl();
    }

    private class ListIteratorImpl extends IteratorImpl implements ListIterator {

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public Object previous() {
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            cursor--;
            return MyListImpl.this.array[lastRet = i];
        }

        @Override
        public void set(Object e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            MyListImpl.this.array[lastRet] = e;
        }
    }

    @Override
    public ListIterator listIterator() {
        return new ListIteratorImpl();
    }
}
