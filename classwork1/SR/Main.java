package classwork1.SR;

public class Main {
    public static void main(String[] args) {
        Order order = new Order("Laptop", 2);
        
        PrintOrder printOrder = new PrintOrder();
        printOrder.printOrder(order);
        
        SaveOrder saveOrder = new SaveOrder();
        saveOrder.saveOrder(order);
    }
}
