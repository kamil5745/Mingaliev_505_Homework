import java.util.*;

public class DrinkTea {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Старт");
        if (!askYesNo(sc, "Есть вода в чайнике?")) {
            System.out.println("Налить воду в чайник");
        }
        System.out.println("Вскипятить воду");
        System.out.println("Положить чай");
        System.out.println("Залить кипяток");
        int m = askIntInRange(sc, "Засечь время заваривания (3–5 минут). Введите число:", 3, 5);
        System.out.println("Таймер: " + m + " мин");
        boolean strong = askYesNo(sc, "Достаточно крепкий?");
        while (!strong) {
            System.out.println("Подождать ещё 1 минуту");
            m++;
            System.out.println("Таймер: " + m + " мин");
            strong = askYesNo(sc, "Теперь достаточно крепкий?");
        }
        if (askYesNo(sc, "Добавки? (сахар/молоко/лимон)")) {
            System.out.print("Что добавить? Перечислите через запятую: ");
            String add = sc.hasNextLine() ? sc.nextLine().trim() : "";
            System.out.println(add.isEmpty() ? "Ничего не добавлено" : "Добавлено: " + add);
        }
        System.out.println("Перемешать");
        System.out.println("Пить чай ☕");
    }

    static boolean askYesNo(Scanner sc, String prompt) {
        for (;;) {
            System.out.print(prompt + " (да/нет): ");
            String s = sc.hasNextLine() ? sc.nextLine().trim().toLowerCase(Locale.ROOT) : "";
            if (s.equals("да") || s.equals("д") || s.equals("y") || s.equals("yes")) return true;
            if (s.equals("нет") || s.equals("н") || s.equals("n") || s.equals("no")) return false;
        }
    }

    static int askIntInRange(Scanner sc, String prompt, int min, int max) {
        for (;;) {
            System.out.print(prompt + " ");
            String s = sc.hasNextLine() ? sc.nextLine().trim() : "";
            if (isDigits(s)) {
                int v = toIntBounded(s, max + 1);
                if (v >= min && v <= max) return v;
            }
        }
    }

    static boolean isDigits(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    static int toIntBounded(String s, int limit) {
        long v = 0;
        for (int i = 0; i < s.length(); i++) {
            v = v * 10 + (s.charAt(i) - '0');
            if (v >= limit) return limit;
        }
        return (int) v;
    }
}