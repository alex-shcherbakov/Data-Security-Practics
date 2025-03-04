import java.util.*;

class First {
    private static final Map<Character, String> alphabetToCode = new HashMap<>();
    private static final Map<String, Character> codeToCipher = new HashMap<>();

    static {
        // таблиця відповідностей + ключ шифровки
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();
        String[] codes = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26"};

        int[] key = {16, 22, 4, 17, 19, 24, 20, 8, 14, 15, 0, 18, 3, 5, 6, 26, 9, 10, 11, 25, 23, 2, 21, 1, 13, 12, 7};

        for (int i = 0; i < alphabet.length; i++) {
            alphabetToCode.put(alphabet[i], codes[i]);
            codeToCipher.put(codes[i], alphabet[key[i]]);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть текст для шифрування:");
        String inputText = scanner.nextLine();

        String encrypted = encrypt(inputText);
        System.out.println("Зашифрований текст: " + encrypted);
    }
    public static String encrypt(String text) {
        text = text.toUpperCase(); // все в верхній регістр
        StringBuilder encryptedText = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (alphabetToCode.containsKey(ch)) {
                String code = alphabetToCode.get(ch);
                encryptedText.append(codeToCipher.get(code));
            }
        }

        return encryptedText.toString();
    }
}