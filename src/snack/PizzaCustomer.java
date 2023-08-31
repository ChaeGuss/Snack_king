package snack;

public record PizzaCustomer(
    String firstName,
    String lastName,
    int id,
    int pizzas
) {

    @Override
    public String toString() {
        return "Customer " + firstName + " " + lastName;
    }

}
