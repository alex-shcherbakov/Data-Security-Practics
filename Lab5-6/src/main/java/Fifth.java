import java.util.*;

public class Fifth {
    private static final String KEYWORD = "ABANDON";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter text to encrypt: ");
        String input = scanner.nextLine().replace(" ", ""); // видаляємо пробіли
        String encrypted = encrypt(input, KEYWORD);
        System.out.println("Encrypted text: " + encrypted);
    }

    private static String encrypt(String text, String keyword) {
        int cols = keyword.length();
        int rows = (int) Math.ceil((double) text.length() / cols);
        char[][] grid = new char[rows][cols]; // cтворюємо таблицю символів

        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = (k < text.length()) ? text.charAt(k++) : '_';
            }
        }

        Integer[] order = getColumnOrder(keyword);
        StringBuilder encryptedText = new StringBuilder();

        for (int col : order) {
            for (int row = 0; row < rows; row++) {
                encryptedText.append(grid[row][col]);
            }
        }

        return encryptedText.toString();
    }

    // сортуємо індекси в ключовому слові
    private static Integer[] getColumnOrder(String keyword) {
        Character[] chars = new Character[keyword.length()];
        for (int i = 0; i < keyword.length(); i++) {
            chars[i] = keyword.charAt(i);
        }

        Integer[] indices = new Integer[keyword.length()];
        for (int i = 0; i < keyword.length(); i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.comparing(i -> chars[i]));
        return indices;
    }
}