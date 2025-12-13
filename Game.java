import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    // Ситуации
    static final int STRESSED = 0;         // устала
    static final int NEEDS_ATTENTION = 1;  // не хватает внимания
    static final int NEEDS_SPACE = 2;      // нужно пространство

    // Действия
    static final int LISTEN = 1;      // выслушать
    static final int INVITE = 2;      // позвать на прогулку
    static final int GIVE_SPACE = 3;  // дать пространство

    // Шкалы
    static int trust = 50;
    static int comfort = 50;
    static int turn = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        println("=== Симулятор общения (минимальная версия) ===");
        println("Цель: продержаться как можно больше ходов, сохраняя баланс шкал 0..100.");
        println("Шкалы стартуют с 50. Любой выход <0 или >100 — конец игры.\n");

        while (true) {
            turn++;

            int situation = randomSituation();
            println("\nДень " + turn + ". Ситуация:");
            printSituation(situation);

            int action = readAction(in);

            int[] deltas = calcDeltas(situation, action); // deltas[0]=dTrust, deltas[1]=dComfort

            int noiseT = noise();
            int noiseC = noise();

            int oldTrust = trust;
            int oldComfort = comfort;

            trust += deltas[0] + noiseT;
            comfort += deltas[1] + noiseC;

            // Печать реакции и итогов
            println("\nРезультат хода:");
            println("Доверие:  " + trust + " (" + deltaStr(oldTrust, trust) + ")");
            println("Комфорт:  " + comfort + " (" + deltaStr(oldComfort, comfort) + ")");

            // Проверка окончания
            if (trust < 0 || trust > 100 || comfort < 0 || comfort > 100) {
                println("\nИгра окончена!");
                println("Вы продержались дней: " + turn);
                break;
            }
        }

        in.close();
    }

    // ----- Логика -----

    // Базовые эффекты + модификаторы ситуации + шум применяется отдельно ниже
    static int[] calcDeltas(int situation, int action) {
        int dT = 0, dC = 0;

        // Базовые эффекты (усиленные)
        switch (action) {
            case LISTEN:      dT += 8;  dC += 5;  break;
            case INVITE:      dT += 5;  dC += -8; break;
            case GIVE_SPACE:  dT += 3;  dC += 9;  break;
            default: /* не должно случиться */ break;
        }

        // Контекстные модификаторы
        switch (situation) {
            case STRESSED: // устала
                if (action == LISTEN)      { dT += 3; dC += 2; }
                else if (action == INVITE) { dT += -2; dC += -6; }
                else if (action == GIVE_SPACE) { dT += 0; dC += 3; }
                break;

            case NEEDS_ATTENTION: // не хватает внимания
                if (action == LISTEN)      { dT += 2; dC += 2; }
                else if (action == INVITE) { dT += 4; dC += 4; }
                else if (action == GIVE_SPACE) { dT += -3; dC += -6; }
                break;

            case NEEDS_SPACE: // нужно пространство
                if (action == LISTEN)      { dT += 0; dC += -2; }
                else if (action == INVITE) { dT += -5; dC += -8; }
                else if (action == GIVE_SPACE) { dT += 2; dC += 7; }
                break;
        }

        return new int[] { dT, dC };
    }

    static int randomSituation() {
        return ThreadLocalRandom.current().nextInt(0, 3); // 0..2
    }

    static int noise() {
        // Равномерно: -2, -1, 0, +1, +2
        return ThreadLocalRandom.current().nextInt(-2, 3);
    }

    // ----- Ввод/вывод -----

    static int readAction(Scanner in) {
        while (true) {
            println("\nВыберите действие:");
            println("1) LISTEN (выслушать)");
            println("2) INVITE (позвать на прогулку)");
            println("3) GIVE_SPACE (дать пространство)");
            print("> ");

            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;

            // Разрешим и цифры, и слова
            if (line.equals("1") || line.equalsIgnoreCase("listen")) return LISTEN;
            if (line.equals("2") || line.equalsIgnoreCase("invite")) return INVITE;
            if (line.equals("3") || line.equalsIgnoreCase("give_space") || line.equalsIgnoreCase("givespace")) return GIVE_SPACE;

            println("Некорректный ввод. Введите 1, 2 или 3 (или listen/invite/give_space).");
        }
    }

    static void printSituation(int s) {
        switch (s) {
            case STRESSED:
                println("Она говорит: «Сегодня сильно устала, много дел.»");
                break;
            case NEEDS_ATTENTION:
                println("Она пишет: «Давно не общались…»");
                break;
            case NEEDS_SPACE:
                println("Она честно сообщает: «Хочу побыть одна сегодня.»");
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
    static void print(String s) { System.out.print(s); }
}