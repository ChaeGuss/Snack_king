package Center;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.in;

public class SnackKingQueueManagementSystem {

    private static final char[][] arrays = new char[3][];

    private static final int pizzaStockCapacity = 100;

    private static int currentPizzaStock = pizzaStockCapacity;

    private static final int priceOfAPizza = 1350;


    private static final int stockWarning = 20;


    private static final Customer[] storeCustomers = new Customer[10];

    private static final FoodQueue[] foodQueues = {new FoodQueue(2), new FoodQueue(3), new FoodQueue(5)};

    private static int countCustomers = 0;

    private static final Customer[] waitingCustomers = new Customer[5];
    private static int waitingCount = 0;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "100", "VFQ" -> viewAllQueues();
                case "101", "VEQ" -> viewAllEmptyQueues();
                case "102", "ACQ" -> addCustomer();
                case "103", "RCQ" -> removeCustomer();
                case "104", "PCQ" -> removeServedCustomer();
                case "105", "VCS" -> viewCustomersSortedAlphabetically();
                case "106", "SPD" -> storeProgramDataToFile();
                case "107", "LPD" -> loadProgramDataFromFile();
                case "108", "STK" -> viewRemainingPizzaStock();
                case "109", "AFS" -> addPizzaToStock();
                case "110", "IFQ" -> viewIncomeOfEachQueue();
                case "999", "EXT" -> {
                    System.out.println("Exiting Snack King Food Center");
                    exit = true;
                }
                default -> FoodQueue.printInvalid();
            }
        }

    }

    private static void printMainMenu() {
        String menu = """
                100 or VFQ: View all Queues.
                101 or VEQ: View all Empty Queues.
                102 or ACQ: Add customer to a Queue.
                103 or RCQ: Remove a customer from a Queue.
                104 or PCQ: Remove a served customer.
                105 or VCS: View Customers Sorted in alphabetical order.
                106 or SPD: Store Program Data into file.
                107 or LPD: Load Program Data from file.
                108 or STK: View Remaining Pizza Stock.
                109 or AFS: Add Pizza to Stock.
                999 or EXT: Exit the Program.
                Enter your choice:""";
        System.out.println(menu);
    }


    public static void viewAllQueues() {
        System.out.println("Snack King Food Center");
        System.out.println("********************");
        System.out.println("*     Cashiers     *");
        System.out.println("********************");
        for (int i = 0; i < foodQueues.length; i++) {
            System.out.print("Cashier " + i + ": ");
            char[] queue = foodQueues[i].getQueue();
            for (char element : queue) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public static void viewAllEmptyQueues() {
        System.out.println("Empty Slots" + "\n");
        for (int i = 0; i < foodQueues.length; i++) {
            System.out.print("Queue " + i + " : ");
            for (int j = 0; j < foodQueues[i].getQueue().length; j++) {
                if (foodQueues[i].getQueue()[j] == 'X') {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }


    public static Customer fillCustomerDetails(Scanner scanner) {
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

        if (currentPizzaStock >= stockWarning) {
            currentPizzaStock -= numOfPizza;
        } else {
            System.out.println("Pizza stock is low. Remaining pizzas: " + currentPizzaStock);
        }

        return new Customer(fName, sName, cId, numOfPizza);
    }

    public static void addCustomer() {
        Scanner scanner = new Scanner(System.in);

        Customer newCustomer = fillCustomerDetails(scanner);
        int minQueueIndex = foodQueues[0].getMinQueueIndex(); // Find the queue with the minimum length

        boolean addedToQueue = false;

        for (int i = 0; i < foodQueues.length; i++) {
            char[] queue = foodQueues[minQueueIndex].getQueue();

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
            if (!foodQueues[minQueueIndex].addToWaitingList(newCustomer)) {
                System.out.println("All queues and waiting list are currently full. " + newCustomer.getFullName() + " cannot be accommodated.");
            }
        }
    }


    private static int getNextAvailableQueueIndex() {
        int minQueueIndex = foodQueues[0].getMinQueueIndex();
        int nextQueueIndex = minQueueIndex;

        // Find the index of the next available queue
        for (int i = 1; i < foodQueues.length; i++) {
            nextQueueIndex = (nextQueueIndex + 1) % foodQueues.length;
            char[] queue = foodQueues[nextQueueIndex].getQueue();
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

        char[] queue = foodQueues[arrayIndex].getQueue();

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
                char[] queue = foodQueues[0].getQueue();
                int indexInArray = -1;

                for (int j = 0; j < queue.length; j++) {
                    if (queue[j] == 'O' && storeCustomers[j].getcId() == customerId) {
                        indexInArray = j;
                        break;
                    }
                }

                if (indexInArray != -1) {
                    for (FoodQueue foodQueue : foodQueues) {
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


    public static void storeProgramDataToFile() {
        try {
            FileWriter myWriter = new FileWriter("Snack_King_Program_Data.txt");
            myWriter.write("Snack King Food Center" + "\n\n");

            for (FoodQueue foodQueue : foodQueues) {
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


    public static void loadProgramDataFromFile() {
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


    public static void viewRemainingPizzaStock() {
        System.out.println("The remaining pizza stock: " + currentPizzaStock);
    }

    public static void addPizzaToStock() {
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

    public static void viewIncomeOfEachQueue() {
    }


}
