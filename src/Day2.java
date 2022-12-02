import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input2.txt").toPath());
        int score = 0;
        int score2 = 0;
        for (String line : lines) {
            int a = line.charAt(0) - 'A';
            int b = line.charAt(2) - 'X';
            int d = (3 + (b - a)) % 3;
            score += (b + 1);
            if (d == 1) {
                score += 6;
            } else if (d == 0) {
                score += 3;
            }

            int c = (a + b + 2) % 3;
            int e = (3 + (c - a)) % 3;
            score2 += (c + 1);
            if (e == 1) {
                score2 += 6;
            } else if (e == 0) {
                score2 += 3;
            }
        }
        System.out.println("First: " + score);
        System.out.println("Second: " + score2);
    }
}
