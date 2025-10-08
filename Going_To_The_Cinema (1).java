public class Going_To_The_Cinema {
    public static void main(String[] args) {
        // 1) Объявления + инициализации разными способами (требование задания)
        int step3D = 35;               // объявление с одновременной инициализацией
        int step2D; step2D = 27;       // объявление + отдельная инициализация

        int vanyaMax3D = 500;          // Ваня: хочет 3D не дороже 500 (его личный платёж)
        int andreyBudget = 200;        // Андрей: свои деньги
        int vikaHelp = 200;            // Вика: докидывает на билет Андрея
        int katyaMinStrict = 300;      // Катя: согласна только если цена строго > 300

        double studDiscount = 0.15;    // 15% скидка (у всех, кроме Андрея)

        // Наличие студенческого билета
        boolean hasStudentVanya = true;
        boolean hasStudentAndrey = false; // у Андрея нет
        boolean hasStudentKatya = true;
        boolean hasStudentVika = true;

        // 2) Проверка гипотезы "2D за 200" с демонстрацией цикла while по шагу 27
        int target2D = 200;
        boolean cinemaHas2D_200 = false;
        int probe2D = 0;
        System.out.println("Шаг 1. Проверяем, существует ли в кинотеатре 2D-цена ровно 200 при шаге 27:");
        while (probe2D <= 1000) { // ограничим разумно вверх
            if (probe2D == target2D) {
                cinemaHas2D_200 = true;
                break;
            }
            probe2D += step2D;
        }
        System.out.println("  - Найдена ли цена 200 среди 2D? " + (cinemaHas2D_200 ? "Да" : "Нет"));
        System.out.println("  - Устраивает ли Катю цена 200? " + (target2D > katyaMinStrict ? "Да" : "Нет (нужно > 300)"));

        boolean canGo2D = cinemaHas2D_200 && (target2D > katyaMinStrict);
        // Даже если бы цена 200 существовала, Катя бы не согласилась. Значит 2D отпадает.
        if (canGo2D) {
            // Этот блок по условию задачи не сработает, но оставим для полноты.
            System.out.println("ИТОГ: Идут на 2D за " + target2D + " руб.");
        } else {
            System.out.println("Вывод: 2D-вариант невозможен (Катя требует > 300). Ищем подходящее 3D.");
        }

        // 3) Поиск минимально подходящей 3D-цены по шагу 35
        // Условия для 3D:
        // - Цена > 300 (для Кати)
        // - Андрей платит без скидки, но с помощью Вики: цена <= andreyBudget + vikaHelp
        // - Ваня хочет 3D и не платить > 500 с учётом своей скидки (то есть 0.85*цена <= 500)
        int chosen3D = -1;
        int andreyMaxPayable = andreyBudget + vikaHelp;

        System.out.println("\nШаг 2. Перебор 3D-цен по шагу 35 и проверка ограничений всех друзей:");
        for (int price = step3D; price <= 1000; price += step3D) { // верхнюю границу берём с запасом
            boolean katyaOK   = price > katyaMinStrict;
            boolean andreyOK  = price <= andreyMaxPayable; // он без скидки, но с помощью Вики
            // Ваня платит со скидкой 15%
            double vanyaPays = (hasStudentVanya ? price * (1.0 - studDiscount) : price);
            boolean vanyaOK  = vanyaPays <= vanyaMax3D;

            // Лог шага
            System.out.printf("  Цена 3D = %d: Катя=%s, Андрей=%s, Ваня(платит %.2f руб.)=%s%n",
                    price,
                    katyaOK ? "OK" : "нет",
                    andreyOK ? "OK" : "нет",
                    vanyaPays,
                    vanyaOK ? "OK" : "нет"
            );

            if (katyaOK && andreyOK && vanyaOK) {
                chosen3D = price; // берём минимально подходящую
                break;
            }
            if (price > andreyMaxPayable && price > vanyaMax3D / (1.0 - studDiscount) && price > katyaMinStrict) {
                // эвристика раннего выхода, если очевидно дальше только хуже
                break;
            }
        }

        // 4) Итог и расчёт индивидуальных платежей
        if (chosen3D == -1) {
            System.out.println("\nИТОГ: Подходящего сеанса не найдено.");
            return;
        }

        System.out.println("\nИТОГ: Ребята идут на 3D.");
        System.out.println("Цена билета (номинал, без скидки): " + chosen3D + " руб.");

        // Скидочные платежи
        double vanyaPays = hasStudentVanya ? chosen3D * (1.0 - studDiscount) : chosen3D;
        double katyaPays = hasStudentKatya ? chosen3D * (1.0 - studDiscount) : chosen3D;
        double vikaPays  = hasStudentVika  ? chosen3D * (1.0 - studDiscount) : chosen3D;
        double andreyPaysNoHelp = hasStudentAndrey ? chosen3D * (1.0 - studDiscount) : chosen3D;

        // Учтём помощь Вики в 200 руб. именно Андрею (для прозрачности платежей)
        int help = vikaHelp;
        double andreyFinal = Math.max(0, andreyPaysNoHelp - help);
        double vikaFinal   = vikaPays + help; // Вика доплачивает за Андрея сверх своего билета

        // 5) Вывод «цена без скидки / со скидкой» для каждого + кто сколько платит фактически
        System.out.println("\nЦены билетов для каждого (номинал / со скидкой):");
        System.out.printf("Ваня:  без скидки %d руб. / со скидкой %.2f руб.%n", chosen3D, vanyaPays);
        System.out.printf("Катя:  без скидки %d руб. / со скидкой %.2f руб.%n", chosen3D, katyaPays);
        System.out.printf("Вика:  без скидки %d руб. / со скидкой %.2f руб.%n", chosen3D, vikaPays);
        System.out.printf("Андрей:без скидки %d руб. / со скидкой нет (%.2f руб.)%n", chosen3D, andreyPaysNoHelp);

        System.out.println("\nФактические платежи с учётом помощи Вики Андрею:");
        System.out.printf("Андрей платит: %.2f руб.  (Вика добавляет: %d руб.)%n", andreyFinal, help);
        System.out.printf("Вика платит:   %.2f руб.  (свой билет + помощь Андрею)%n", vikaFinal);

        // 6) Для наглядности суммарная проверка бюджета Андрея
        boolean andreyWithinBudget = andreyFinal <= andreyBudget + 1e-9;
        System.out.println("\nПроверка: укладывается ли Андрей в свои 200 руб.? " + (andreyWithinBudget ? "Да" : "Нет"));
    }
}