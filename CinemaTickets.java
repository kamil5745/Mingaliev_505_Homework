import java.util.Scanner;

public class CinemaTickets {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // === ВВОД ИЗ ПРОШЛОЙ ЗАДАЧИ ===
        int base3DRub = sc.nextInt();   // базовая цена 3D без скидок, за обычные места

        // Приводим к ближайшему шагу 35 (без Math.round)
        int step = 35;
        int r = base3DRub % step;
        int down = base3DRub - r;
        int up = down + step;
        if (r > 17) base3DRub = up; else base3DRub = down;

        // Переходим в копейки для точных расчётов
        int base3DKop = base3DRub * 100;

        // === ПАРАМЕТРЫ ЗАЛА ===
        int N = 30;
        int group = 4;

        // Разметим занятые места
        boolean[] busy = new boolean[N + 1]; // индексация 1..N
        for (int i = 1; i <= 7; i++) busy[i] = true;
        for (int i = 11; i <= 13; i++) busy[i] = true;
        for (int i = 15; i <= 17; i++) busy[i] = true;
        for (int i = 22; i <= 24; i++) busy[i] = true;
        for (int i = 26; i <= 30; i++) busy[i] = true;

        // Ищем первый свободный подряд блок из 4 мест
        int start = -1;
        for (int i = 1; i <= N - group + 1; i++) {
            boolean ok = true;
            for (int k = 0; k < group; k++) {
                if (busy[i + k]) { ok = false; break; }
            }
            if (ok) { start = i; break; }
        }
        if (start == -1) {
            System.out.println("1. Нет доступного блока из 4 мест.");
            System.out.println("2. Общая стоимость: 0.00 ₽");
            return;
        }

        // Места подряд в порядке: Ваня(0), Андрей(1), Катя(2), Вика(3)
        int[] seats = new int[group];
        for (int i = 0; i < group; i++) seats[i] = start + i;

        // Считаем стоимость по правилам:
        // +5% если место в диапазоне 10..20; -15% студентам (кроме Андрея: индекс 1)
        int totalKop = 0;
        for (int i = 0; i < group; i++) {
            int seat = seats[i];
            int priceKop = base3DKop;

            // Наценка +5% (округление до копейки: +50 перед /100)
            if (seat >= 10 && seat <= 20) {
                priceKop = (priceKop * 105 + 50) / 100;
            }

            // Скидка -15% всем, кроме Андрея (i != 1)
            if (i != 1) {
                priceKop = (priceKop * 85 + 50) / 100;
            }

            totalKop += priceKop;
        }

        // === ВЫВОД ===
        // 1) Номера посадочных мест с _ по _ в обратном порядке
        int from = seats[0];
        int to = seats[group - 1];

        System.out.print("1. Номера посадочных мест с " + from + " по " + to + " в обратном порядке: ");
        for (int i = group - 1; i >= 0; i--) {
            System.out.print(seats[i]);
            if (i > 0) System.out.print(" ");
        }
        System.out.println();

        // 2) Общая стоимость (с учётом номера) билетов
        int rub = totalKop / 100;
        int kop = totalKop % 100;
        System.out.println("2. Общая стоимость: " + rub + "." + (kop < 10 ? "0" + kop : kop) + " ₽");
    }
}