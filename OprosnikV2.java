import java.util.Scanner;
import java.util.Arrays;

public class OprosnikV2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] nameStrings = new String[5];
        int[] ageInts = new int[5];
        String[] profStrings = new String[5];
        String[] emailStrings = new String[5];
        String[] adreStrings = new String[5];
        String[] instagramStrings = new String[5];

        for (int i = 0; i < 5; i++) {                     
            System.out.println("User # " + (i + 1) + ".");
            nameStrings[i] = scanner.next();
            ageInts[i] = scanner.nextInt();
            profStrings[i] = scanner.next();
            emailStrings[i] = scanner.next();
            adreStrings[i] = scanner.next();
            instagramStrings[i] = scanner.next();
        }
        scanner.close();

        String longestName = "";
        int averageAge = 0;
        String[] emailWithoutSymbolArr = new String[5];

        for (String name : nameStrings) {                  
            if (name.length() > longestName.length()) {
                longestName = name;
            }
        }

        for (int age : ageInts) {
            averageAge += age;                             
        }
        averageAge = averageAge / ageInts.length;          

        for (int i = 0; i < emailStrings.length; i++) {    
            String email = emailStrings[i];
            StringBuilder sb = new StringBuilder();
            for (char symbol : email.toCharArray()) {
                if (symbol != '@') sb.append(symbol);
                break;
            }
            emailWithoutSymbolArr[i] = sb.toString();
        }

        System.out.println("Longest name is: " + longestName);
        System.out.println("Average age is: " + averageAge);
        System.out.println(Arrays.toString(emailWithoutSymbolArr)); 
    }
}