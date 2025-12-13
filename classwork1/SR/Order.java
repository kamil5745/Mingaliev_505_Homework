package classwork1.SR;

public class Order {
    private String product;
    private int amont;
    public Order(String product, int amont){
        this.product = product;
        this.amont = amont;
    }
    public String getProduct(){
        return product;
    }
}
