package classwork1.OCP;

public class NotService {
    public void sendNot(String type){
        if(type.equals("email")){
            System.out.println("Send email");
        } else if(type.equals("sms")){
            System.out.println("Send sms");
        }
    }
}
