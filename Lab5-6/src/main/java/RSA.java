import java.math.BigInteger;

public class RSA {
    // зміна за варіантом + обране е (первинне значення)
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
        base = base % mod;

        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = (result * base) % mod;
            }
            exponent = exponent >> 1;
            base = (base * base) % mod;
        }

        return result;
    }

    // обчислення НСД
    public static int Divisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // e^(-1) mod m
    public static int modInverse(int e, int m) {
        BigInteger A = BigInteger.valueOf(e);
        BigInteger M = BigInteger.valueOf(m);
        return A.modInverse(M).intValue();
    }
    // ключ d через розширений алгоритм Евкліда
    public static int calculatePrivateKey() {
        int phi = (p - 1) * (q - 1);
        int a = e, b = phi;
        int x0 = 1, x1 = 0;

        while (b != 0) {
            int q = a / b;
            int temp = a % b;
            a = b;
            b = temp;

            int xTemp = x0 - q * x1;
            x0 = x1;
            x1 = xTemp;
    }
    // якщо x0 від’ємний
    int d = (x0 % phi + phi) % phi;

    return d;
}

    // розшифрування тексту
    public static String decrypt(String encryptedText, int d) {
        int n = p * q;

        String[] blocks = encryptedText.split(" ");
        StringBuilder decryptedText = new StringBuilder();

        for (String block : blocks) {
            int c = Integer.parseInt(block);
            int m = modExponentiation(c, d, n);
            decryptedText.append((char) m);
        }

        return decryptedText.toString();
    }

    // автоматичний підбір e, якщо поточне не підходить
    public static int findValidE(int phi) {
        int newE = 2;
        while (newE < phi) {
            if (Divisor(newE, phi) == 1) {
                return newE;
            }
            newE++;
        }
        throw new RuntimeException("Не вдалося знайти допустиме e");
    }

    public static void main(String[] args) {
        int phi = (p - 1) * (q - 1);

        // чи e і φ(n) взаємно прості
        if (Divisor(e, phi) != 1 || e > phi) {
            System.out.println("Обране e не підходить. Підбираємо нове...");
            e = findValidE(phi);
            System.out.println("Нове значення e: " + e);
        }

        // вхідний текст
        String input = "shch'ole";
        String formattedString = Format(input);
        System.out.println("Представимо літери в десятковому коді за таблицею ASCII: " + formattedString);

        // шифрування
        String encryptedText = encrypt(formattedString);
        System.out.println("Зашифрований текст: " + encryptedText);

        // обчислення приватного ключа d
        int d = calculatePrivateKey();
        System.out.println("Значення закритого ключа d: " + d);

        // розшифрування
        String decryptedText = decrypt(encryptedText, d);
        System.out.println("Розшифрований текст: " + decryptedText);
    }
}