import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class Day25 {
    private static final Map<Character, Integer> SNAFU_DIGIT_TO_INT = Map.of(
            '=', -2, '-', -1, '0', 0, '1', 1, '2', 2
    );

    private static final Map<Integer, Character> INT_TO_SNAFU_DIGIT = Map.of(
        -2, '=', -1, '-', 0, '0', 1, '1', 2, '2'
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input25.txt").toPath());

        long sum = lines.stream()
                .mapToLong(Day25::snafuToLong)
                .sum();
        System.out.println(longToSnafu(sum));
    }

    private static long snafuToLong(String snafu) {
        long result = 0;
        long j = 1;
        for (int i = snafu.length() - 1; i >= 0; i--, j *= 5) {
            char c = snafu.charAt(i);
            long v = SNAFU_DIGIT_TO_INT.get(c);
            result += (v * j);
        }
        return result;
    }

    private static String longToSnafu(long v)
    {
        String f = Long.toString(v, 5);
        int[] digits = new int[f.length() + 1];
        for (int i = 0; i < f.length(); i++) {
            digits[i + 1] = f.charAt(i) - '0';
        }
        for (int i = digits.length - 1; i > 0; i--) {
            if (digits[i] > 2) {
                digits[i] -= 5;
                digits[i - 1]++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = digits[0] == 0 ? 1: 0; i < digits.length; i++) {
            sb.append(INT_TO_SNAFU_DIGIT.get(digits[i]));
        }
        return sb.toString();
    }
}
