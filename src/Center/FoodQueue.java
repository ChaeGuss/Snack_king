package Center;

public class FoodQueue {

    private char[] queue;

    private final Customer[] waitingCustomers = new Customer[5];

    private int customerWaitingSlot = 0;

    private int countWaitingCustomers = 0;

    public FoodQueue(int size) {
        queue = new char[size];
        for (int i = 0; i < queue.length; i++) {
            queue[i] = 'X';
        }
    }

    public char[] getQueue() {
        return queue;
    }

    public int getMinQueueIndex() {
        int minQueueIndex = 0;
        int minQueueLength = queue.length;

        for (int i = 1; i < queue.length; i++) {
            if (queue[i] < minQueueLength) {
                minQueueIndex = i;
                minQueueLength = queue[i];
            }
        }

        return minQueueIndex;
    }

    public static void printInvalid() {
        System.out.println("Invalid");
    }

    public boolean addToWaitingList(Customer customer) {
        if (countWaitingCustomers < waitingCustomers.length) {
            waitingCustomers[countWaitingCustomers] = customer;
            customerWaitingSlot = (customerWaitingSlot + 1) % waitingCustomers.length;
            countWaitingCustomers++;
            System.out.println(customer.getFullName() + " added to waiting list.");
            return true;
        } else {
            System.out.println("Waiting list is currently full.");
            return false;
        }

    }



}
