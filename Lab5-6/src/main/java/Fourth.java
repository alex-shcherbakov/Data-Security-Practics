import java.util.Scanner;

public class Fourth {

    private static final char[][] KEY_MATRIX = {
            {'M', 'V', '.', 'G', 'O'},
            {'-', 'J', 'P', 'T', 'Y'},
            {'D', 'W', 'L', 'K', 'Q'},
            {'A', 'S', 'X', 'F', '_'},
            {'E', 'Z', 'C', 'I', 'H'},
            {'B', 'U', 'N', ',', 'R'}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть текст для шифрування:");
        String plaintext = scanner.nextLine();

        String ciphertext = encrypt(plaintext);
        System.out.println("Зашифрований текст: " + ciphertext);
    }
    public static String encrypt(String plaintext) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z\\s.,-]", ""); // видаляємо недозволені символи
        plaintext = plaintext.replace(' ', '_'); // пробіл це підкреслення

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            char char1 = plaintext.charAt(i);
            char char2 = (i + 1 < plaintext.length()) ? plaintext.charAt(i + 1) : 'X'; // + Х, якщо непарна довжина

            int[] pos1 = findCharPosition(char1);
            int[] pos2 = findCharPosition(char2);

            if (pos1[0] == pos2[0]) { // один рядок
                ciphertext.append(KEY_MATRIX[pos1[0]][(pos1[1] + 1) % 5]);
                ciphertext.append(KEY_MATRIX[pos2[0]][(pos2[1] + 1) % 5]);
            } else if (pos1[1] == pos2[1]) { // один стовпець
                ciphertext.append(KEY_MATRIX[(pos1[0] + 1) % 6][pos1[1]]);
                ciphertext.append(KEY_MATRIX[(pos2[0] + 1) % 6][pos2[1]]);
            } else { // прямокутник
                ciphertext.append(KEY_MATRIX[pos1[0]][pos2[1]]);
                ciphertext.append(KEY_MATRIX[pos2[0]][pos1[1]]);
            }
        }
        return ciphertext.toString();
    }

    private static int[] findCharPosition(char c) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (KEY_MATRIX[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1}; // символ не знайдено
    }
}