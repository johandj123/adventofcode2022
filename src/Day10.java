import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input10.txt").toPath());
        List<Integer> values = new ArrayList<>();
        for (String line : lines) {
            String[] s = line.split(" ");
            if (s.length == 1) values.add(null); else values.add(Integer.parseInt(s[1]));
        }
        List<Integer> timedValues = new ArrayList<>();
        int current = 1;
        timedValues.add(current);
        for (Integer x : values) {
            timedValues.add(current);
            if (x != null) {
                current += x;
                timedValues.add(current);
            }
        }
        int sum = 0;
        for (int i = 20; i <= 220; i += 40) {
            sum += i * timedValues.get(i - 1);
        }
        System.out.println(sum);
        for (int i = 0; i < 240; i++) {
            int x = (i % 40);
            if (x == 0) {
                System.out.println();
            }
            int value = timedValues.get(i) - 1;
            if (x == value || x == value + 1 || x == value + 2) {
                System.out.print('#');
            } else {
                System.out.print('.');
            }
        }
    }
}
