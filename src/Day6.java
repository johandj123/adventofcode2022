import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("input6.txt").toPath()).trim();
        process(input, 4);
        process(input, 14);
    }

    private static void process(String input, int count) {
        Deque<Character> deque = new ArrayDeque<>();
        for (int i = 0; i < input.length(); i++) {
            deque.addLast(input.charAt(i));
            if (deque.size() > count) {
                deque.removeFirst();
            }
            if (deque.size() == count && allUnique(deque)) {
                System.out.println(i + 1);
                break;
            }
        }
    }

    private static boolean allUnique(Deque<Character> deque) {
        Set<Character> set = new HashSet<>(deque);
        return set.size() == deque.size();
    }
}
