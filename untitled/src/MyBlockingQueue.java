import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue implements Queue<String> {
    LinkedList list;
    int capacity;
    Condition notEmpty;
    Condition notFull;
    Lock lock;

    MyBlockingQueue(int _capacity) {
        list = new LinkedList();
        capacity = _capacity;
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean add(String o) {
        lock.lock();
        try {
            while (list.size() >= capacity) {
                notFull.await();
            }
            list.add(o);
            notEmpty.signal();
        } catch (InterruptedException e) {
            ;//
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public boolean offer(String o) {
        return add(o);
    }

    @Override
    public String remove() {
        lock.lock();
        String item = null;
        try {
            while(isEmpty()) {
                notEmpty.await();
            }
            item = (String) list.remove();
            notFull.signalAll();
        } catch (InterruptedException e) {
            ;//
        } finally {
            lock.unlock();
            return item;
        }
    }

    @Override
    public String poll() { return null; }


    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override public void clear() { }

    @Override public boolean retainAll(Collection c) { return false; }

    @Override public boolean containsAll(Collection c) { return false; }
    @Override public boolean remove(Object o) { return false; }

    @Override public String element() { return null; }

    @Override public String peek() { return null; }
    @Override public boolean contains(Object o) { return false; }

    @Override public Iterator iterator() { return null; }

    @Override public Object[] toArray() { return new Object[0]; }

    @Override public Object[] toArray(Object[] a) { return new Object[0]; }
}
