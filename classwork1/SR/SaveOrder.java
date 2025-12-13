package classwork1.SR;

public class SaveOrder {
    public void saveOrder(Order order){
        // Logic to save order to database or file
        System.out.println("Order saved: " + order.getProduct() + ", Amount: " + order.getAmount());
    }
}
