package Center;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.in;

public class SnackKingQueueManagementSystem {

    private static final char[][] arrays = new char[3][];

    private static final int pizzaStockCapacity = 100;

    private static int currentPizzaStock = pizzaStockCapacity;

    private static final int priceOfAPizza = 1350;


    private static final int stockWarning = 20;


    private static final Customer[] storeCustomers = new Customer[10];

    private static final FoodQueue[] newArrays = new FoodQueue[3];

    private static int countCustomers = 0;

    private static final Customer[] waitingCustomers = new Customer[5];
    private static int waitingCount = 0;


    public static void main(String[] args) {
        newArrays[0] = new FoodQueue(2);
        newArrays[1] = new FoodQueue(3);
        newArrays[2] = new FoodQueue(5);

        Scanner scanner = new Scanner(in);

        boolean exit = false;
        while (!exit) {
            mainMenu();
            String option = scanner.nextLine();

            if (option.equals("100") || option.equals("VFQ")) {
                viewAllQueues();
            } else if (option.equals("101") || option.equals("VEQ")) {
                viewAllEmptyQueues();
            } else if (option.equals("102") || option.equals("ACQ")) {
                addCustomer();
            } else if (option.equals("103") || option.equals("RCQ")) {
                removeCustomer();
            } else if (option.equals("104") || option.equals("PCQ")) {
                removeServedCustomer();
            } else if (option.equals("105") || option.equals("VCS")) {
                viewCustomersSortedAlphabetically();
            } else if (option.equals("106") || option.equals("SPD")) {
                storeProgramDataToFile();
            } else if (option.equals("107") || option.equals("LPD")) {
                loadProgramDataFromFile();
            } else if (option.equals("108") || option.equals("STK")) {
                viewRemainingPizzaStock();
            } else if (option.equals("109") || option.equals("AFS")) {
                addPizzaToStock();
            } else if (option.equals("110") || option.equals("IFQ")) {
                viewIncomeOfEachQueue();
            } else if (option.equals("999") || option.equals("EXT")) {
                System.out.println("Exiting Snack King Food Center");
                exit = true;
            } else {
                FoodQueue.printInvalid();
            }
        }

    }

    private static void mainMenu() {
        System.out.println("100 or VFQ: View all Queues.");
        System.out.println("101 or VEQ: View all Empty Queues.");
        System.out.println("102 or ACQ: Add customer to a Queue.");
        System.out.println("103 or RCQ: Remove a customer from a Queue.");
        System.out.println("104 or PCQ: Remove a served customer.");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
        System.out.println("106 or SPD: Store Program Data into file.");
        System.out.println("107 or LPD: Load Program Data from file.");
        System.out.println("108 or STK: View Remaining Pizza Stock.");
        System.out.println("109 or AFS: Add Pizza to Stock.");
        System.out.println("999 or EXT: Exit the Program.");
        System.out.print("Enter your choice: ");
    }



    public static void viewAllQueues() {
        System.out.println("Snack King Food Center");
        System.out.println("********************");
        System.out.println("*     Cashiers     *");
        System.out.println("********************");
        for (int i = 0; i < newArrays.length; i++) {
            System.out.print("Cashier " + i + ": ");
            char[] queue = newArrays[i].getQueue();
            for (char element : queue) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public static void viewAllEmptyQueues() {
        System.out.println("Empty Slots" + "\n");
        for (int i = 0; i < newArrays.length; i++) {
            System.out.print("Queue " + i + " : ");
            for (int j = 0; j < newArrays[i].getQueue().length; j++) {
                if (newArrays[i].getQueue()[j] == 'X') {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }


    public static Customer getDetails(Scanner scanner) {
        System.out.println("Enter Customer's First Name: ");
        String fName = scanner.nextLine();

        System.out.println("Enter Customer's Second Name: ");
        String sName = scanner.nextLine();

        int cId = 0;

        boolean isIdValid = false;
        while (!isIdValid) {
            System.out.println("Enter Customer's ID: ");
            cId = scanner.nextInt();

            boolean isInStoreCustomers = false;
            for (int i = 0; i < countCustomers; i++) {
                if (storeCustomers[i].getcId() == cId) {
                    isInStoreCustomers = true;
                    System.out.println("This ID already exists. Try again.");
                    break;
                }
            }

            if (!isInStoreCustomers) {
                isIdValid = true;
            }
        }

        System.out.println("Enter The Number Of Pizza: ");
        int numOfPizza = scanner.nextInt();

        if (currentPizzaStock >= stockWarning){
            currentPizzaStock -= numOfPizza;
        } else {
            System.out.println("Pizza stock is low. Remaining pizzas: " + currentPizzaStock);
        }

        return new Customer(fName, sName, cId, numOfPizza);
    }

    public static void addCustomer() {
        Scanner scanner = new Scanner(System.in);

        Customer newCustomer = getDetails(scanner);
        int minQueueIndex = newArrays[0].getMinQueueIndex(); // Find the queue with the minimum length

        boolean addedToQueue = false;

        for (int i = 0; i < newArrays.length; i++) {
            char[] queue = newArrays[minQueueIndex].getQueue();

            if (queueIsNotFull(queue)) {
                for (int j = 0; j < queue.length; j++) {
                    if (queue[j] == 'X') {
                        queue[j] = 'O';
                        addedToQueue = true;
                        storeCustomers[countCustomers] = newCustomer; // Add the customer to storeCustomers array
                        countCustomers++;
                        System.out.println(newCustomer.getfName() + " is added to Queue " + minQueueIndex);
                        break;
                    }
                }
            }

            if (addedToQueue) {
                break;
            }

            minQueueIndex = getNextAvailableQueueIndex();
        }

        if (!addedToQueue) {
            if (!newArrays[minQueueIndex].addToWaitingList(newCustomer)) {
                System.out.println("All queues and waiting list are currently full. " + newCustomer.getFullName() + " cannot be accommodated.");
            }
        }
    }


    private static int getNextAvailableQueueIndex() {
        int minQueueIndex = newArrays[0].getMinQueueIndex();
        int nextQueueIndex = minQueueIndex;

        // Find the index of the next available queue
        for (int i = 1; i < newArrays.length; i++) {
            nextQueueIndex = (nextQueueIndex + 1) % newArrays.length;
            char[] queue = newArrays[nextQueueIndex].getQueue();
            if (queueIsNotFull(queue)) {
                return nextQueueIndex;
            }
        }

        return minQueueIndex; // Return the original minQueueIndex if no queue is available
    }


    private static boolean queueIsNotFull(char[] queue) {
        for (char element : queue) {
            if (element == 'X') {
                return true;
            }
        }
        return false;
    }

    public static void removeCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Queue number to remove a customer from | 0 | 1 | 2 |: ");
        int arrayIndex = scanner.nextInt();

        if (arrayIndex < 0 || arrayIndex >= arrays.length) {
            FoodQueue.printInvalid();
            return;
        }

        char[] queue = newArrays[arrayIndex].getQueue();

        System.out.println("Enter the index of the customer you wish to remove: ");
        int customerIndex = scanner.nextInt();

        if (customerIndex < 0 || customerIndex >= queue.length) {
            FoodQueue.printInvalid();
            return;
        }

        if (queue[customerIndex] == 'O') {
            Customer removedCustomer = storeCustomers[arrayIndex * queue.length + customerIndex];
            queue[customerIndex] = 'X';
            System.out.println(removedCustomer.getfName() + " is removed from Queue " + arrayIndex);
        } else {
            System.out.println("There are no customers in this space currently");
        }


    }

    public static void listOfServedCustomers() {
        System.out.println("Snack King Food Center | Served Customers:");
        for (Customer customer : storeCustomers) {                          // Generate a list of served customers to display when removing a served customer
            if (customer != null) {
                System.out.println(customer.getcId() + ": " + customer.getFullName());
            }
        }
    }

    public static void removeServedCustomer() {
        Scanner scanner = new Scanner(System.in);

        listOfServedCustomers();
        System.out.println(" ");
        System.out.println("Enter the customer's ID you wish to remove: ");
        int customerId = scanner.nextInt();

        for (int i = 0; i < countCustomers; i++) {
            if (storeCustomers[i].getcId() == customerId) {
                char[] queue = newArrays[0].getQueue();
                int indexInArray = -1;

                for (int j = 0; j < queue.length; j++) {
                    if (queue[j] == 'O' && storeCustomers[j].getcId() == customerId) {
                        indexInArray = j;
                        break;
                    }
                }

                if (indexInArray != -1) {
                    for (FoodQueue foodQueue : newArrays) {
                        char[] customerQueue = foodQueue.getQueue();
                        if (customerQueue[indexInArray] == 'O') {
                            customerQueue[indexInArray] = 'X';
                            if (indexInArray < customerQueue.length - 1 && customerQueue[indexInArray + 1] == 'O') {
                                customerQueue[indexInArray] = 'O';
                                customerQueue[indexInArray + 1] = 'X';
                                storeCustomers[indexInArray] = storeCustomers[indexInArray + 1];
                                storeCustomers[indexInArray + 1] = null;
                            }
                        }
                    }
                }

                storeCustomers[i] = null;
                System.out.println(customerId + " has been removed.");
                break;
            }
        }
    }

    public static void listOfWaitingCustomers() {
        System.out.println("Snack King Food Center | Served Customers:");
        for (Customer customer : waitingCustomers) {                          // Generate a list of served customers to display when removing a served customer
            if (customer != null) {
                System.out.println(customer.getcId() + ": " + customer.getFullName());
            }
        }
    }


    public static void viewCustomersSortedAlphabetically() {
        String[] sortedNames = new String[countCustomers];

        for (int i = 0; i < countCustomers; i++) {
            sortedNames[i] = storeCustomers[i].getFullName();
        }

        for (int i = 0; i < sortedNames.length - 1; i++) {
            for (int j = 0; j < sortedNames.length - i - 1; j++) {
                if (sortedNames[j].compareTo(sortedNames[j + 1]) > 0) {
                    String temp = sortedNames[j];
                    sortedNames[j] = sortedNames[j + 1];
                    sortedNames[j + 1] = temp;
                }
            }
        }

        System.out.println("Customer Names | Sorted Alphabetically");
        for (String customerName : sortedNames) {
            System.out.println(customerName);
        }
    }


    public static void storeProgramDataToFile(){
        try {
            FileWriter myWriter = new FileWriter("Snack_King_Program_Data.txt");
            myWriter.write("Snack King Food Center" + "\n\n");

            for (FoodQueue foodQueue : newArrays) {
                char[] queue = foodQueue.getQueue();
                myWriter.write("Cashier : ");
                for (char element : queue) {
                    myWriter.write(element + " ");
                }
                myWriter.write("\n");
            }

            myWriter.write("\n" + "Customer Names" + "\n\n");
            for (int i = 0; i < countCustomers; i++) {
                Customer customer = storeCustomers[i];
                if (customer != null) {
                    myWriter.write(customer.getFullName() + "\n");
                }
            }

            myWriter.close();
            System.out.println("Snack King program data have been stored successfully.");
        } catch (IOException e) {
            System.out.println("An error has occurred. Please try again");
            e.printStackTrace();
        }
    }



    public static void loadProgramDataFromFile(){
        try {
            FileReader fileReader = new FileReader("Snack_King_Program_Data.txt");
            Scanner scanner = new Scanner(fileReader);

            for (int i = 0; i < arrays.length; i++) {                       // Read and update the three cashier queues
                String queueLine = scanner.nextLine();
                String[] elements = queueLine.split(" ");
                if (elements.length == arrays[i].length) {
                    for (int j = 0; j < elements.length; j++) {
                        arrays[i][j] = elements[j].charAt(0);
                    }
                } else {
                    break;
                }
            }

            scanner.close();

            System.out.println("The program data of the Snack King Food Center has been loaded to a text file.");

        } catch (IOException e) {
            System.out.println("An error has occurred. Please try again.");
            e.printStackTrace();
        }
    }


    public static void viewRemainingPizzaStock(){
        System.out.println("The remaining pizza stock: " + currentPizzaStock);
    }

    public static void addPizzaToStock(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of pizzas to add to stock: ");
        int addNumOfPizza = scanner.nextInt();

        if (currentPizzaStock + addNumOfPizza <= pizzaStockCapacity) {                          //check whether the maximum amount of the pizza stock is being exceeded
            currentPizzaStock += addNumOfPizza;
            System.out.println(addNumOfPizza + " pizzas has been added to the stock. The updated stock has " + currentPizzaStock + " pizzas");
        } else {
            System.out.println("Cannot exceed maximum stock capacity. Please try again.");
        }
    }

    public static void viewIncomeOfEachQueue() {}


}
