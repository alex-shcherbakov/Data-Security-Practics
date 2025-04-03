import java.math.BigInteger;

public class RSA {
    // зміна за варіантом + обране е
    public static int p = 23;
    public static int q = 89;
    public static int e = 17;

    // текст в ASCII код
    public static String Format(String input) {
        StringBuilder asciiString = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            int asciiValue = (int) input.charAt(i);
            asciiString.append(asciiValue).append(" ");
        }

        return asciiString.toString().trim();
    }

    // шифруєм текст
    public static String encrypt(String formattedString) {
        int n = p * q;

        // formattedString на блоки
        String[] blocks = formattedString.split(" ");
        StringBuilder encryptedText = new StringBuilder();

        // блок шифрується за формулою c = m^e mod n
        for (String b : blocks) {
            int m = Integer.parseInt(b);
            int c = modExponentiation(m, e, n);
            encryptedText.append(c).append(" ");
        }

        return encryptedText.toString().trim();
    }

    // модульно підносимо до степеня
    public static int modExponentiation(int base, int exponent, int mod) {
        int result = 1;
        base = base % mod;  // коли base > mod

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }

            exponent = exponent >> 1;
            base = (base * base) % mod;
        }

        return result;
    }

    // ключ d
    public static int calculatePrivateKey() {
        // функція Ейлера
        int phi = (p - 1) * (q - 1);

        // d, таке що (d * e) % φ(n) = 1
        int d = modInverse(e, phi);
        return d;
    }

    // e ^ (-1)mod ф(n) за допомогою BigInteger
    public static int modInverse(int e, int m) {
        BigInteger A = BigInteger.valueOf(e);
        BigInteger M = BigInteger.valueOf(m);
        return A.modInverse(M).intValue();
    }

    // розшифрування тексту
    public static String decrypt(String encryptedText, int d) {
        int n = p * q;

        String[] blocks = encryptedText.split(" ");
        StringBuilder decryptedText = new StringBuilder();

        // формула m = c^d mod n
        for (String block : blocks) {
            int c = Integer.parseInt(block);
            int m = modExponentiation(c, d, n);
            decryptedText.append((char) m);
        }

        return decryptedText.toString();
    }

    public static void main(String[] args) {
        // починаємо
        String input = "shch'ole";
        String formattedString = Format(input);
        System.out.println("Представимо літери в десятковому коді за таблицею АSCII: " + formattedString);

        // шифруємо
        String encryptedText = encrypt(formattedString);
        System.out.println("Зашифрований текст: " + encryptedText);

        // ключ d
        int d = calculatePrivateKey();
        System.out.println("значення закритого ключа d: " + d);

        // розшифровуємо
        String decryptedText = decrypt(encryptedText, d);
        System.out.println("Розшифрований текст: " + decryptedText);

    }
}
