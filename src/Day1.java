import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("input1.txt").toPath()).trim();
        String[] parts = input.split("\n\n");
        List<Integer> values = new ArrayList<>();
        for (String s : parts) {
            String[] lines = s.split("\n");
            int sum = Arrays.stream(lines)
                    .mapToInt(Integer::parseInt).sum();
            values.add(sum);
        }
        values.sort(Comparator.reverseOrder());
        System.out.println("First: " + values.get(0));
        System.out.println("Second: " + (values.get(0) + values.get(1) + values.get(2)));
    }
}
