package classwork1.OCP;

public class Main {
    public static void main(String[] args) {
        NotificationServiceLike notificationService = new SmsNotification();
        notificationService.send("Hello via SMS!");
    }
}
