import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneRoundDES {
    // таблиця початкової перестановки
    private static final int[][] Table_IpBits = {
            {58, 50, 42, 34, 26, 18, 10, 2},
            {60, 52, 44, 36, 28, 20, 12, 4},
            {62, 54, 46, 38, 30, 22, 14, 6},
            {64, 56, 48, 40, 32, 24, 16, 8},
            {57, 49, 41, 33, 25, 17, 9, 1},
            {59, 51, 43, 35, 27, 19, 11, 3},
            {61, 53, 45, 37, 29, 21, 13, 5},
            {63, 55, 47, 39, 31, 23, 15, 7}
    };

    // таблиця для PC-1
    private static final int[][] Table_PC1 = {
            {57, 49, 41, 33, 25, 17, 9},
            {1, 58, 50, 42, 34, 26, 18},
            {10, 2, 59, 51, 43, 35, 27},
            {19, 11, 3, 60, 52, 44, 36},
            {63, 55, 47, 39, 31, 23, 15},
            {7, 62, 54, 46, 38, 30, 22},
            {14, 6, 61, 53, 45, 37, 29},
            {21, 13, 5, 28, 20, 12, 4}
    };

    // таблиця для PC-2
    private static final int[][] Table_PC2 = {
            {14, 17, 11, 24, 1, 5},
            {3, 28, 15, 6, 21, 10},
            {23, 19, 12, 4, 26, 8},
            {16, 7, 27, 20, 13, 2},
            {41, 52, 31, 37, 47, 55},
            {30, 40, 51, 45, 33, 48},
            {44, 49, 39, 56, 34, 53},
            {46, 42, 50, 36, 29, 32}
    };

    // таблиця розширення E
    private static final int[][] Table_E = {
            {32, 1, 2, 3, 4, 5},
            {4, 5, 6, 7, 8, 9},
            {8, 9, 10, 11, 12, 13},
            {12, 13, 14, 15, 16, 17},
            {16, 17, 18, 19, 20, 21},
            {20, 21, 22, 23, 24, 25},
            {24, 25, 26, 27, 28, 29},
            {28, 29, 30, 31, 32, 1}
    };

    // S блоки
    private static final int[][][] S_BOXES = {
            { // №1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { // №2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { // №3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { // №4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { // №5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { // №6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { // №7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 10, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { // №8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };
    // таблиця для P
    private static final int[][] Table_P = {
            {16, 7, 20, 21},
            {29, 12, 28, 17},
            {1, 15, 23, 26},
            {5, 18, 31, 10},
            {2, 8, 24, 14},
            {32, 27, 3, 9},
            {19, 13, 30, 6},
            {22, 11, 4, 25}
    };

    // таблиця кінцевої перестановки
    private static final int[][] Table_IP_Inverse = {
            {40, 8, 48, 16, 56, 24, 64, 32},
            {39, 7, 47, 15, 55, 23, 63, 31},
            {38, 6, 46, 14, 54, 22, 62, 30},
            {37, 5, 45, 13, 53, 21, 61, 29},
            {36, 4, 44, 12, 52, 20, 60, 28},
            {35, 3, 43, 11, 51, 19, 59, 27},
            {34, 2, 42, 10, 50, 18, 58, 26},
            {33, 1, 41, 9, 49, 17, 57, 25}
    };

    // перетворюємо текст в двійковий вигляд за таблицею АSCII
    public static String textToBinary(String text) {
        return text.chars()
                .mapToObj(c -> String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'))
                .reduce("", String::concat);
    }

    // перестановка для двійкового коду
    public static String applyPermutation(String binaryCode, int[][] permutationTable) {
        return Arrays.stream(permutationTable)
                .flatMapToInt(Arrays::stream)
                .filter(index -> index <= binaryCode.length())
                .mapToObj(index -> String.valueOf(binaryCode.charAt(index - 1)))
                .collect(Collectors.joining());
    }

    // циклічний зсув на 1 біт вліво
    public static String leftShift(String bits, int shift) {
        return bits.substring(shift) + bits.substring(0, shift);
    }

    // порозрядне додавання за модулем 2.
    public static String xorBinaryStrings(String str1, String str2) {
        return IntStream.range(0, Math.min(str1.length(), str2.length()))
                .mapToObj(i -> str1.charAt(i) == str2.charAt(i) ? "0" : "1")
                .collect(Collectors.joining());
    }

    // обробляємо S-блоки
    public static String applySBoxes(String input) {
        StringBuilder result = new StringBuilder();

        // потрібно щоб було 48 бітів
        if (input.length() != 48) {
            input = input + "0".repeat(48 - input.length());
        }

        // обробляємо кожен 6-бітний блок
        for (int i = 0; i < 8; i++) {
            String sixBits = input.substring(i * 6, (i + 1) * 6);

            // визначаємо рядок і стовпець
            int row = Integer.parseInt(sixBits.charAt(0) + "" + sixBits.charAt(5), 2);
            int col = Integer.parseInt(sixBits.substring(1, 5), 2);

            // знаходимо потрібне значення з відповідного S-блоку
            int value = S_BOXES[i][row][col];

            // перетворюємо на 4-бітне двійкове
            StringBuilder binaryValue = new StringBuilder(Integer.toBinaryString(value));

            // доповнюємо до 4 біт якщо значення недостатнє
            while (binaryValue.length() < 4) {
                binaryValue.insert(0, "0");
            }

            result.append(binaryValue);
        }

        return result.toString();
    }

    // форматує двійковий код для виведення
    public static String formatBinary(String binary, int groupSize) {
        return IntStream.iterate(0, i -> i < binary.length(), i -> i + groupSize)
                .mapToObj(i -> binary.substring(i, Math.min(i + groupSize, binary.length())))
                .collect(Collectors.joining(" "));
    }


    // головна функція, для виконання алгоритма DES
    public static String oneRoundDES(String plaintext, String key) {
        System.out.println("АЛГОРИТМ DES(один раунд)");

        // 1) текст і ключ у двійковий код
        String binaryText = textToBinary(plaintext);
        String binaryKey = textToBinary(key);

        System.out.println("\n1. Вхідні дані");
        System.out.println("Текст для шифрування: " + plaintext);
        System.out.println("Текст у двійковому коді (ASCII): " + formatBinary(binaryText, 8));
        System.out.println("Ключ: " + key);
        System.out.println("Ключ у двійковому коді (ASCII): " + formatBinary(binaryKey, 8));

        // 2) Початкова перестановка
        String ipResult = applyPermutation(binaryText, Table_IpBits);
        System.out.println("\n2. Початкова перестановка");
        System.out.println("Результат: " + formatBinary(ipResult, 8));

        // Розділяємо блок на дві частини
        String leftHalf = ipResult.substring(0, ipResult.length() / 2);
        String rightHalf = ipResult.substring(ipResult.length() / 2);

        System.out.println("L: " + formatBinary(leftHalf, 8));
        System.out.println("R: " + formatBinary(rightHalf, 8));

        // 3) створюємо ключ раунду
        System.out.println("\n3. Формування ключа раунду:");

        // Перестановка PC-1
        String pc1Result = applyPermutation(binaryKey, Table_PC1);
        System.out.println("Результат після PC-1: " + formatBinary(pc1Result, 8));

        // Розділяємо ключ на C0 і D0
        String c0 = pc1Result.substring(0, 28);
        String d0 = pc1Result.substring(28, 56);

        System.out.println("C0: " + formatBinary(c0, 4));
        System.out.println("D0: " + formatBinary(d0, 4));

        // Циклічний зсув
        String c1 = leftShift(c0, 1);
        String d1 = leftShift(d0, 1);

        System.out.println("Зсув на 1 біт вліво");
        System.out.println("C1: " + formatBinary(c1, 4));
        System.out.println("D1: " + formatBinary(d1, 4));

        // З'єднуємо C1 і D1
        String c1d1 = c1 + d1;
        System.out.println("C1D1: " + formatBinary(c1d1, 8));

        // Перестановка PC-2
        String roundKey = applyPermutation(c1d1, Table_PC2);
        System.out.println("Ключ раунду (після PC-2): " + formatBinary(roundKey, 8));

        // 4) Функція раунду
        System.out.println("\n4. Функція раунду");

        // Розширення E
        String expandedR = applyPermutation(rightHalf, Table_E);
        System.out.println("Розширена права частина (E(R)): " + formatBinary(expandedR, 8));

        // XOR з ключем раунду
        String xorResult = xorBinaryStrings(expandedR, roundKey);
        System.out.println("Результат порозрядного додавання за модулем2: " + formatBinary(xorResult, 6));

        // 5) Обробка S-блоками
        System.out.println("\n5. Обробка S-Блоками");

        // Розбиваємо на 6-бітні блоки для виведення
        for (int i = 0; i < 8; i++) {
            String block = xorResult.substring(i * 6, (i + 1) * 6);
            System.out.println("Блок " + (i + 1) + ": " + block);
        }

        // Обчислюємо вихід S-блоків
        String sBoxOutput = applySBoxes(xorResult);
        System.out.println("Результат S-блоків: " + formatBinary(sBoxOutput, 4));

        // 6) Перестановка P
        System.out.println("\n6. Перестановка P");
        String pOutput = applyPermutation(sBoxOutput, Table_P);
        System.out.println("Результат після перестановки P: " + formatBinary(pOutput, 8));

        // 7) XOR з лівою частиною
        System.out.println("\n7. Завершення раунду");
        String newRight = xorBinaryStrings(leftHalf, pOutput);
        System.out.println("Функція раунду порозрядно додається за модулем 2 з лівою частиною вхідного блоку: " + formatBinary(newRight, 8));

        // Формуємо результат раунду (L' = R, R' = L XOR F(R, K))
        String roundResult = rightHalf + newRight;
        System.out.println("Результат раунду (L'R'): " + formatBinary(roundResult, 8));

        // 8) Кінцева перестановка IP^(-1)
        System.out.println("\n8. Кінцева перестановка");
        String cipherBinary = applyPermutation(roundResult, Table_IP_Inverse);
        System.out.println("Зашифрований результат (у двійковому коді): " + formatBinary(cipherBinary, 8));

        return cipherBinary;
    }


    public static void main(String[] args) {

        String textToEncrypt = "shch'ole";
        String encryptionKey = "password";

        String encryptedBinary = oneRoundDES(textToEncrypt, encryptionKey);

        System.out.println("\nРЕЗУЛЬТАТ ШИФРУВАННЯ");
        System.out.println("Вхідний текст: " + textToEncrypt);
        System.out.println("Ключ: " + encryptionKey);
        System.out.println("Зашифрований двійковий код: " + formatBinary(encryptedBinary, 8));
    }
}
