package snack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    public static String mainMenu = """
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
            Enter your choice:
            """;


    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }


    public static void viewAllQueues(PizzaQueue[] queues) {
        System.out.print("""
                Snack King Food Center
                **********************
                *      Cashiers      *
                **********************
                """);
        /*
         Here's example how to use standard libraries to efficiently work with arrays.
         Actually in most IRL cases you wouldn't be able to write better optimized algorithm than there's already
         for sorting, finding a maximum etc. AND (what's no less important) it is much more readable code.
         One line with .max() is much easier to read than 10 lines of `for (int i=0...)`
         */
        PizzaQueue longestQueue = Arrays.stream(queues).max(Comparator.comparing(queue -> queue.size)).orElse(null);
        int maxLength = Objects.requireNonNull(longestQueue).size;
        for (int row = 0; row < maxLength; row++) {
            //Didn't figure out how it was envisioned in the paper, so just used TAB for table-ish format
            System.out.print("\t");
            for (PizzaQueue queue : queues) {
                if (queue.size >= row) {
                    PizzaCustomer customer = queue.getCustomerAt(row);
                    if (customer != null) {
                        System.out.print("O");
                    } else {
                        System.out.print("X");
                    }
                }
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println("""
                X - Not Occupied O - Occupied
                """);
    }

    public static void viewAllEmptyQueues(PizzaQueue[] queues) {
        System.out.println("Empty Slots:");
        for (int i = 0; i < queues.length; i++) {
            System.out.print("Queue " + i + ": ");
            PizzaQueue queue = queues[i];
            for (int j = 0; j < queue.size; j++) {
                if (queue.getCustomerAt(j) == null) {
                    /*
                    It's an example how to write text with variables inside without adding up tons of strings
                    (like "foo " + var1 + " " + var2 + " some bar of " + size + "blaaaaaaaaa").
                    I personally hate both variants.
                     */
                    System.out.printf("%d,", j);
                }
            }
            System.out.println();
        }
    }


}
