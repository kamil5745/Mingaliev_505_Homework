package classwork1.SR;

public class Order implements Save{
    private String product;
    private int amont;
    public Order(String product, int amont){
        this.product = product;
        this.amont = amont;
    }

    public String getProduct(){
        return product;
    }

    public int getAmount(){
        return amont;
    }

    public void saveOrder(Order order){
        // Logic to save order to database or file
        System.out.println("Order saved: " + order.getProduct() + ", Amount: " + order.getAmount());
    }
}
