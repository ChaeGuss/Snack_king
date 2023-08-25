package Center;

public class Customer {

    private String fName;

    private String sName;

    private int cId;

    private int numOfPizza;

    public Customer(String fName, String sName, int cId, int numOfPizza) {
        this.fName = fName;
        this.sName = sName;
        this.cId = cId;
        this.numOfPizza = numOfPizza;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getFullName() {
        return fName + " " + sName;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getNumOfPizza() {
        return numOfPizza;
    }

    public void setNumOfPizza(int numOfPizza) {
        this.numOfPizza = numOfPizza;
    }
}
