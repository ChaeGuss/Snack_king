package snack;

import java.util.Arrays;

public class PizzaQueue {

    public final int size;
    private final PizzaCustomer[] queue;

    public PizzaQueue(int length) {
        size = length;
        queue = new PizzaCustomer[size];
        Arrays.fill(queue, null);
    }

    public PizzaCustomer getCustomerAt(int position) {
        if (position < 0 || position > size - 1) {
            return null;
        }
        return queue[position];
    }


}
