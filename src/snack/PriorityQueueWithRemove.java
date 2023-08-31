package snack;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityQueueWithRemove<E> extends PriorityQueue<E> {

    protected final List<E> elementsList = new ArrayList<>();

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added) {
            elementsList.add(e);
        }
        return added;
    }

    public boolean removeAt(int position) {
        if (position < 0 || position >= size()) {
            return false;
        }

        E element = elementsList.remove(position);
        super.remove(element);
        return true;
    }

}
