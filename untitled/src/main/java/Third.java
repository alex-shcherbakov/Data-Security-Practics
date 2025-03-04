import java.util.Scanner;

public class Third {
    private static final int ALPHABET_SIZE = 26;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть текст для шифрування:");
        String inputText = scanner.nextLine();

        String key = "QUEST";
        String encrypted = encrypt(inputText, key);
        System.out.println("Зашифрований текст: " + encrypted);
    }
    public static String encrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", ""); // видаляємо пробіли та інше
        key = key.toUpperCase();
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char plainChar = text.charAt(i);
            char keyChar = key.charAt(j % key.length());

            char encryptedChar = (char) ('A' + (plainChar - 'A' + keyChar - 'A') % ALPHABET_SIZE);
            encryptedText.append(encryptedChar);
            j++;
        }

        return encryptedText.toString();
    }
}