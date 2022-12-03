import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input3.txt").toPath());
        int first = 0;
        for (String line : lines) {
            String a = line.substring(0, line.length() / 2);
            String b = line.substring(line.length() / 2);
            char c = common(a, b);
            first += priority(c);
        }
        System.out.println(first);
        int second =0;
        for (int i = 0; i < lines.size(); i += 3) {
            char c = common(lines.get(i), lines.get(i + 1), lines.get(i + 2));
            second += priority(c);
        }
        System.out.println(second);
    }

    private static char common(String a, String b)
    {
        for (int i = 0; i < a.length(); i++) {
            for  (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    return a.charAt(i);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private static char common(String a, String b, String c)
    {
        for (int i = 0; i < a.length(); i++) {
            for  (int j = 0; j < b.length(); j++) {
                for (int k = 0; k < c.length(); k++) {
                    if (a.charAt(i) == b.charAt(j) && b.charAt(j) == c.charAt(k)) {
                        return a.charAt(i);
                    }
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private static int priority(char c)
    {
        if (c >= 'a' &&  c <= 'z') {
            return c - 'a' + 1;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 27;
        }
        throw new IllegalArgumentException();
    }
}
