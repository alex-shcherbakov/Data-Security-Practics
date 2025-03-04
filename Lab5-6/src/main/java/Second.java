import java.util.*;

public class Second {
    private static final Map<Character, Character> substitutionTable = new HashMap<>();

    static {
        // таблиця відповідностей + гасло LETSGO_
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();
        char[] key = "LETSGO_ABCDFHIJKMNPQRUWVXYZ".toCharArray();

        for (int i = 0; i < alphabet.length; i++) {
            substitutionTable.put(alphabet[i], key[i]);
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
        text = text.toUpperCase(); // знову верхній регістр
        StringBuilder encryptedText = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (substitutionTable.containsKey(ch)) {
                encryptedText.append(substitutionTable.get(ch));
            }
        }

        return encryptedText.toString();
    }
}