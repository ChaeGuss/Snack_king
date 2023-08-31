package snack;

import java.util.Scanner;

import static java.lang.System.in;
import static snack.Menu.viewAllEmptyQueues;
import static snack.Menu.viewAllQueues;

public class PizzaKing {


    private static final PizzaQueue[] foodQueues = {new PizzaQueue(2), new PizzaQueue(3), new PizzaQueue(5)};

    private static Scanner input;
    private static Menu menu;



    public static void main(String[] args) {
        input = new Scanner(in);
        menu = new Menu(input);
        boolean loop = true;
        do {
            loop = loop();
        } while (loop);
    }

    private static boolean loop() {
        System.out.println(Menu.mainMenu);
        String option = input.nextLine();
        switch (option) {
            case "100", "VFQ" -> viewAllQueues(foodQueues);
            case "101", "VEQ" -> viewAllEmptyQueues(foodQueues);
//            case "102", "ACQ" -> addCustomer();
//            case "103", "RCQ" -> removeCustomer();
//            case "104", "PCQ" -> removeServedCustomer();
//            case "105", "VCS" -> viewCustomersSortedAlphabetically();
//            case "106", "SPD" -> storeProgramDataToFile();
//            case "107", "LPD" -> loadProgramDataFromFile();
//            case "108", "STK" -> viewRemainingPizzaStock();
//            case "109", "AFS" -> addPizzaToStock();
//            case "110", "IFQ" -> viewIncomeOfEachQueue();
            case "999", "EXT" -> {
                System.out.println("Exiting Snack King Food Center");
                return false;
            }
            default -> System.out.println("Invalid");
        }
        return true;
    }




}
