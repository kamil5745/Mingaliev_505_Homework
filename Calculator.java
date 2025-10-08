import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String name = "";
        while (true) {
            System.out.print("Введите имя: ");
            name = sc.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Имя не может быть пустым. Повторите ввод.");
        }

        String op = "";
        while (true) {
            System.out.print("Введите тип операции (Сумма, Вычитание, Умножение, Деление): ");
            op = sc.nextLine().trim().toLowerCase();
            if (op.equals("сумма") || op.equals("вычитание") || op.equals("умножение") || op.equals("деление")) break;
            System.out.println("Неверный тип операции. Введите: Сумма, Вычитание, Умножение, Деление.");
        }

        String numPattern = "^[+-]?\\d+(\\.\\d+)?$";

        double a = 0.0;
        while (true) {
            System.out.print("Введите первое число: ");
            String s = sc.nextLine().trim().replace(',', '.');
            if (s.matches(numPattern)) {
                a = Double.parseDouble(s);
                break;
            }
            System.out.println("Некорректное число. Примеры: 12, 3.14, -0.5");
        }

        double b = 0.0;
        while (true) {
            System.out.print(op.equals("деление") ? "Введите второе число (не 0): " : "Введите второе число: ");
            String s = sc.nextLine().trim().replace(',', '.');
            if (s.matches(numPattern)) {
                b = Double.parseDouble(s);
                if (op.equals("деление") && b == 0.0) {
                    System.out.println("Делить на 0 нельзя. Повторите ввод.");
                } else {
                    break;
                }
            } else {
                System.out.println("Некорректное число. Примеры: 12, 3.14, -0.5");
            }
        }

        double result = 0.0;
        switch (op) {
            case "сумма":
                result = a + b;
                break;
            case "вычитание":
                result = a - b;
                break;
            case "умножение":
                result = a * b;
                break;
            case "деление":
                result = a / b;
                break;
            default:
                System.out.println("Неожиданная операция.");
                return;
        }

        System.out.println(name + " ваш результат: " + result);
    }
}