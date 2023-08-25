package Center;

import java.io.*;

import java.util.Scanner;

import static java.lang.System.in;

public class viva {

    private static final char[][] arrays = new char[3][];

    private static final int pizzaStockCapacity = 100;

    private static int currentPizzaStock = pizzaStockCapacity;


    private static final int stockWarning = 20;

    private static final String[] storeCustomers = new String[10];

    private static int countCustomers = 0;

    public static void main(String[] args) {
         arrays[0] = new char[2];
         arrays[1] = new char[3];
         arrays[2] = new char[5];

        // Initialize queues as empty
        for (char[] array : arrays) {
            for (int i = 0; i < array.length; i++) {
                array[i] = 'X';
            }
        }

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
            } else if (option.equals("999") || option.equals("EXT")) {
                System.out.println("Exiting Snack King Food Center");
                exit = true;
            } else {
                System.out.println("Invalid");
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
        System.out.println("108 or STK: View Remaining burgers Stock.");
        System.out.println("109 or AFS: Add burgers to Stock.");
        System.out.println("999 or EXT: Exit the Program.");
        System.out.print("Enter your choice: ");
    }



    public static void viewAllQueues() {
        System.out.println("Snack King Food Center");
        System.out.println("********************");
        System.out.println("*     Cashiers     *");
        System.out.println("********************");
        for (int i = 0; i < arrays.length; i++) {
            System.out.print("Cashier " + i + ": ");
            for (int j = 0; j < arrays[i].length; j++) {
                System.out.print(arrays[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void viewAllEmptyQueues() {
        System.out.println("Empty Slots" + "\n");
        for (int i = 0; i < arrays.length; i++) {
            System.out.print("Queue " + i + " : ");
            for (char element : arrays[i]) {
                if (element == 'X') {
                    System.out.print(element + " ");
                }
            }
            System.out.println();
        }
    }

    public static String getName() {
        Scanner scanner = new Scanner(in);
        System.out.print("Enter the customer's name: ");
        return scanner.nextLine();
    }
    public static void addCustomer() {
        Scanner scanner = new Scanner(in);

        String name = getName();

        System.out.println("Enter the Queue number to add the customer | 0 | 1 | 2 |: ");
        int arrayIndex = scanner.nextInt();

        if (arrayIndex < 0 || arrayIndex >= arrays.length) {
            System.out.println("Invalid");
            return;
        }

        int position = -1;

        for (int j = 0; j < arrays[arrayIndex].length; j++) {
            if (arrays[arrayIndex][j] == 'X') {
                position = j;
                break;
            }
        }

        if (position != -1) {
            if (currentPizzaStock >= 10) {
                arrays[arrayIndex][position] = 'O';
                storeCustomers[countCustomers++] = name;
                currentPizzaStock -= 10; // Decrement pizza from the stock by assuming the customer buys 10 pizzas
                System.out.println(name + " is added to Queue " + arrayIndex);
            } else {
                System.out.println("Not enough pizzas available.");
            }

            if (currentPizzaStock <= stockWarning) {
                System.out.println("Pizza stock is low. Remaining pizzas: " + currentPizzaStock);
            }
        } else {
            System.out.println("The queue is currently full");
        }
    }



    public static void removeCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Queue number to remove a customer from | 0 | 1 | 2 |: ");
        int arrayIndex = scanner.nextInt();

        if (arrayIndex < 0 || arrayIndex >= arrays.length) {
            System.out.println("Invalid");
            return;
        }

        System.out.println("Enter the index of the customer you wish to remove: ");
        int customerIndex = scanner.nextInt();

        if (customerIndex < 0 || customerIndex >= arrays[arrayIndex].length) {
            System.out.println("Invalid");
            return;
        }

        if (arrays[arrayIndex][customerIndex] == 'O') {
            String removedCustomerName = storeCustomers[arrayIndex * arrays[arrayIndex].length + customerIndex];
            arrays[arrayIndex][customerIndex] = 'X';
            System.out.println(removedCustomerName + " is removed from Queue " + arrayIndex);
        } else {
            System.out.println("There are no customers in this space currently");
        }

        // get the customer name who is currently at the index after creating the store program data method
    }

    public static void listOfServedCustomers() {
        System.out.println("Snack King Food Center | Served Customers:");
        for (String customer : storeCustomers) {                          // Generate a list of served customers to display when removing a served customer
            if (customer != null) {
                System.out.println(customer);
            }
        }
    }


    public static void removeServedCustomer() {
        Scanner scanner = new Scanner(System.in);

        listOfServedCustomers();
        System.out.println(" ");
        System.out.println("Enter the customer's name you wish to remove: ");
        String customerName = scanner.nextLine();

        boolean foundTheCustomer = false;

        for (int i = 0; i < countCustomers; i++) {                                   // iterate through the store customer array to find and remove the specific customer
            if (storeCustomers[i] != null && storeCustomers[i].equals(customerName)) {
                storeCustomers[i] = null;
                foundTheCustomer = true;
                System.out.println(customerName + " has been removed.");
                break;
            }
        }

        if (!foundTheCustomer) {
            System.out.println("The customer cannot be found.");
        }

    }

    public static void viewCustomersSortedAlphabetically() {
        String[] sortedValues = new String[countCustomers];
        System.arraycopy(storeCustomers, 0, sortedValues, 0, countCustomers);

        for (int i = 0; i < sortedValues.length - 1; i++) {                     //Sort customer names alphabetically
            for (int j = 0; j < sortedValues.length - i - 1; j++) {
                if (sortedValues[j].compareTo(sortedValues[j + 1]) > 0) {
                    String temp = sortedValues[j];
                    sortedValues[j] = sortedValues[j + 1];
                    sortedValues[j + 1] = temp;
                }
            }
        }

        System.out.println("Customer Names | Sorted Alphabetically"); // Finally display the alphabetically sorted names
        for (String customerName : sortedValues) {
            System.out.println(customerName);
        }
    }



    public static void storeProgramDataToFile() {
        try {
            FileWriter myWriter = new FileWriter("Snack_King_Program_Data.txt");
            myWriter.write("Snack King Food Center" + "\n\n");

            for (char[] array : arrays) {
                myWriter.write("Cashier : ");
                for (char element : array) {              // Getting the elements/index status of the queue arrays
                    myWriter.write(element + " ");
                }
                myWriter.write("\n");
            }

            myWriter.write("\n" + "Customer Names" + "\n\n");
            for (String customer : storeCustomers) {                       //Getting customer names from the array where the names are stored
                if (customer != null) {
                    myWriter.write(customer + "\n");
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

            int customerIndex = 0;
            while (scanner.hasNextLine()) {
                String textLine = scanner.nextLine();
                if (!textLine.isEmpty() && customerIndex < storeCustomers.length) {      // Reading line by line and updating a list of customer names
                    storeCustomers[customerIndex++] = textLine;
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

}
