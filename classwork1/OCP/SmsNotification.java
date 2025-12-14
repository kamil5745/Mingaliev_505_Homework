package classwork1.OCP;

public class SmsNotification implements NotificationServiceLike {
    @Override
    public void send(String message) {
        // Logic to send SMS
        System.out.println("SMS sent: " + message);
    }
}