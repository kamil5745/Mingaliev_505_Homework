import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GameV2 {

    // --- Ситуации ---
    static final int STRESSED = 0;        // устала/перегружена
    static final int NEEDS_ATTENTION = 1; // не хватает внимания
    static final int NEEDS_SPACE = 2;     // нужно пространство
    static final int CELEBRATORY = 3;     // праздник/радость
    static final int BUSY_BUT_OPEN = 4;   // занята, но на связи
    static final int SICK = 5;            // приболела

    // --- Действия ---
    static final int LISTEN = 1;         // выслушать
    static final int INVITE = 2;         // позвать на встречу
    static final int GIVE_SPACE = 3;     // дать пространство
    static final int SMALL_GESTURE = 4;  // маленький знак внимания
    static final int SUPPORT = 5;        // практическая поддержка по делу
    static final int LIGHT_JOKE = 6;     // лёгкая шутка

    // --- Шкалы ---
    static int trust = 50;    // доверие
    static int comfort = 50;  // комфорт/границы
    static int affinity = 50; // симпатия/интерес

    static int turn = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        println("=== Симулятор общения (без глобального наказания; >100 разрешено) ===");
        println("Конец игры — только если любая шкала упадёт ниже 0.\n");

        while (true) {
            turn++;

            int situation = randomSituation(); // 0..5

            println("\nДень " + turn + ".");
            printSituation(situation);

            int action = readAction(in);

            int[] d = calcDeltas(situation, action); // d[0]=trust, d[1]=comfort, d[2]=affinity

            int oldT = trust, oldC = comfort, oldA = affinity;

            // применяем дельты
            trust   += d[0];
            comfort += d[1];
            affinity+= d[2];

            // мягкий шум: -1..+1
            trust   += noise();
            comfort += noise();
            affinity+= noise();

            println("\nРезультат хода:");
            println("Доверие:  " + trust   + " (" + deltaStr(oldT, trust) + ")");
            println("Комфорт:  " + comfort + " (" + deltaStr(oldC, comfort) + ")");
            println("Симпатия: " + affinity + " (" + deltaStr(oldA, affinity) + ")");

            // конец — только нижняя граница
            if (trust < 0 || comfort < 0 || affinity < 0) {
                println("\nИгра окончена! (одна из шкал < 0)");
                println("Вы продержались дней: " + turn);
                break;
            }
        }

        in.close();
    }

    // --------- ЛОГИКА ДЕЛЬТ (база + контекст; минусы усилены локально) ---------
    static int[] calcDeltas(int situation, int action) {
        int dT = 0, dC = 0, dA = 0;

        // БАЗА (в целом мягко позитивная)
        switch (action) {
            case LISTEN:        dT += 3; dC += 2; dA += 1; break;
            case INVITE:        dT += 2; dC += -1; dA += 3; break;
            case GIVE_SPACE:    dT += 1; dC += 3; dA += 0; break;
            case SMALL_GESTURE: dT += 2; dC += 2; dA += 2; break;
            case SUPPORT:       dT += 3; dC += 1; dA += 1; break;
            case LIGHT_JOKE:    dT += 1; dC += 0; dA += 3; break;
        }

        // КОНТЕКСТ (минусы подкручены прямо здесь)
        switch (situation) {
            case STRESSED: // устала
                if (action == LISTEN)            { dT += 3; dC += 2; }
                else if (action == GIVE_SPACE)   { dT += 1; dC += 2; }
                else if (action == SUPPORT)      { dT += 3; }
                else if (action == SMALL_GESTURE){ dT += 2; dC += 2; dA += 1; }
                else if (action == INVITE)       { dT -= 10; dC -= 12; dA -= 3; } // было -7/-9/-2
                else if (action == LIGHT_JOKE)   { dC -= 4;  dA -= 2;  }         // было -3/-1
                break;

            case NEEDS_ATTENTION: // не хватает внимания
                if (action == LISTEN)            { dT += 2; dC += 2; dA += 2; }
                else if (action == INVITE)       { dT += 3; dC += 2; dA += 3; }
                else if (action == SMALL_GESTURE){ dT += 2; dC += 2; dA += 2; }
                else if (action == SUPPORT)      { dT += 1; }
                else if (action == GIVE_SPACE)   { dT -= 10; dC -= 14; dA -= 5; } // было -8/-12/-4
                else if (action == LIGHT_JOKE)   { dA += 2; }
                break;

            case NEEDS_SPACE: // нужно пространство
                if (action == GIVE_SPACE)        { dT += 3;  dC += 6; }
                else if (action == SMALL_GESTURE){ dT += 1;  dC += 2; }
                else if (action == LISTEN)       { dC -= 2; }                     // было -1
                else if (action == LIGHT_JOKE)   { dC -= 4;  dA -= 3; }           // было -3/-2
                else if (action == INVITE)       { dT -= 12; dC -= 18; dA -= 7; } // было -10/-15/-6
                else if (action == SUPPORT)      { dC -= 3; }                     // было -2
                break;

            case CELEBRATORY: // праздник/радость
                if (action == LIGHT_JOKE)        { dA += 2; }
                if (action == INVITE)            { dT += 2; dC += 1; dA += 2; }
                if (action == SMALL_GESTURE)     { dT += 2; dC += 2; dA += 1; }
                if (action == LISTEN)            { dA += 1; }
                // GIVE_SPACE здесь не наказываем — просто не добавляет
                break;

            case BUSY_BUT_OPEN: // занята, но на связи
                if (action == LISTEN)            { dT += 1; dC += 1; }
                if (action == SUPPORT)           { dT += 2; }
                if (action == GIVE_SPACE)        { dT += 1; dC += 1; }
                if (action == SMALL_GESTURE)     { dT += 1; dC += 1; dA += 1; }
                if (action == INVITE)            { dC -= 3; dA += 1; }            // было -2
                break;

            case SICK: // приболела
                if (action == GIVE_SPACE)        { dT += 2; dC += 3; }
                if (action == LISTEN)            { dT += 1; dC += 1; }
                if (action == SUPPORT)           { dT += 3; }
                if (action == SMALL_GESTURE)     { dT += 2; dC += 2; }
                if (action == INVITE)            { dT -= 10; dC -= 14; dA -= 5; } // было -8/-12/-4
                if (action == LIGHT_JOKE)        { dT -= 3;  dC -= 3; }           // было -2/-2
                break;
        }

        return new int[]{ dT, dC, dA };
    }

    // --------- СЛУЧАЙНОСТЬ ---------
    static int randomSituation() {
        return ThreadLocalRandom.current().nextInt(0, 6); // 0..5
    }

    static int noise() {
        return ThreadLocalRandom.current().nextInt(-1, 2); // -1..+1
    }

    // --------- ВВОД/ВЫВОД ---------
    static int readAction(Scanner in) {
        while (true) {
            println("\nВыберите действие:");
            println("1) LISTEN        (выслушать)");
            println("2) INVITE        (позвать на встречу)");
            println("3) GIVE_SPACE    (дать пространство)");
            println("4) SMALL_GESTURE (маленький знак внимания)");
            println("5) SUPPORT       (практическая поддержка по делу)");
            println("6) LIGHT_JOKE    (лёгкая шутка)");
            print("> ");

            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;

            if (line.equals("1") || line.equalsIgnoreCase("listen")) return LISTEN;
            if (line.equals("2") || line.equalsIgnoreCase("invite")) return INVITE;
            if (line.equals("3") || line.equalsIgnoreCase("give_space") || line.equalsIgnoreCase("givespace")) return GIVE_SPACE;
            if (line.equals("4") || line.equalsIgnoreCase("small_gesture") || line.equalsIgnoreCase("gesture")) return SMALL_GESTURE;
            if (line.equals("5") || line.equalsIgnoreCase("support")) return SUPPORT;
            if (line.equals("6") || line.equalsIgnoreCase("joke") || line.equalsIgnoreCase("light_joke")) return LIGHT_JOKE;

            println("Некорректный ввод. Введите 1..6 или ключевое слово действия.");
        }
    }

    static void printSituation(int s) {
        switch (s) {
            case STRESSED:
                println("Ситуация: «Сегодня завал, очень устала.»");
                break;
            case NEEDS_ATTENTION:
                println("Ситуация: «Давно не общались…»");
                break;
            case NEEDS_SPACE:
                println("Ситуация: «Хочу побыть одна сегодня.»");
                break;
            case CELEBRATORY:
                println("Ситуация: «Отличные новости! Хочу поделиться радостью.»");
                break;
            case BUSY_BUT_OPEN:
                println("Ситуация: «Плотный день, но могу на пару минут на связи.»");
                break;
            case SICK:
                println("Ситуация: «Приболела, самочувствие не очень.»");
                break;
        }
    }

    static String deltaStr(int oldVal, int newVal) {
        int d = newVal - oldVal;
        if (d > 0) return "↑+" + d;
        if (d < 0) return "↓" + d;
        return "→0";
    }

    static void println(String s) { System.out.println(s); }
    static void print(String s)   { System.out.print(s); }
}