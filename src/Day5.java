import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day5 {
    private List<Deque<String>> stacks;

    public static void main(String[] args) throws IOException {
        new Day5().start(false);
        new Day5().start(true);
    }

    private void start(boolean second) throws IOException {
        String input = Files.readString(new File("input5.txt").toPath()).trim();
        String[] parts = input.split("\n\n");
        initStacks(parts[0]);
        moveStacks(parts[1], second);
    }

    private void moveStacks(String part, boolean second) {
        String[] lines = part.split("\n");
        for (String line : lines) {
            String[] parts = line.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;
            if (!second) {
                for (int i = 0; i < count; i++) {
                    String crate = stacks.get(from).removeLast();
                    stacks.get(to).addLast(crate);
                }
            } else {
                Deque<String> load = new ArrayDeque<>();
                for (int i = 0; i < count; i++) {
                    String crate = stacks.get(from).removeLast();
                    load.addFirst(crate);
                }
                stacks.get(to).addAll(load);
            }
        }
        for (var stack : stacks) {
            System.out.print(stack.getLast());
        }
        System.out.println();
    }

    private void initStacks(String part) {
        String[] lines = part.split("\n");
        stacks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Deque<String> stack = new ArrayDeque<>();
            for (int r = lines.length - 2; r >= 0; r--) {
                String crate = lines[r].substring(1 + i * 4, 1 + i * 4 + 1);
                if (!crate.isBlank()) {
                    stack.add(crate);
                }
            }
            stacks.add(stack);
        }
    }
}
