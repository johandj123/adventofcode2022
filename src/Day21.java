import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day21 {
    Map<String, String> map;
    Set<String> humanInvolved = new HashSet<>();

    public static void main(String[] args) throws IOException {
        new Day21().start();

    }

    private void start() throws IOException {
        List<String> lines = Files.readAllLines(new File("input21.txt").toPath());
        map = new HashMap<>();
        for (String line : lines) {
            int i = line.indexOf(':');
            map.put(line.substring(0, i), line.substring(i + 1).trim());
        }
        first();
        second();
    }

    private void second() {
        markHumanInvolved("root");
        String[] s = map.get("root").split(" ");
        System.out.println(solve(s[0], calc(s[2])));
    }

    private long solve(String key, long value) {
        if ("humn".equals(key)) {
            return value;
        }
        String[] s = map.get(key).split(" ");
        String a = s[0];
        String op = s[1];
        String b = s[2];
        if (humanInvolved.contains(b)) {
            long avalue = calc(a);
            switch (op) {
                case "+":
                    // solve a + b = value => b = value - a
                    return solve(b, value - avalue);
                case "-":
                    // solve a - b = value => b = a - value
                    return solve(b, avalue - value);
                case "*":
                    // solve a * b = value => b = value / a
                    return solve(b, value / avalue);
                case "/":
                    // solve a / b = value => b = value * a
                    return solve(b, avalue / value);
                default:
                    throw new IllegalArgumentException(s[1]);
            }
        } else {
            long bvalue = calc(b);
            switch (op) {
                case "+":
                    // solve a + b = value => a = value - b
                    return solve(a, value - bvalue);
                case "-":
                    // solve a - b = value => a = value + b
                    return solve(a, value + bvalue);
                case "*":
                    // solve a * b = value => a = value / b
                    return solve(a, value / bvalue);
                case "/":
                    // solve a / b = value => a = value * b
                    return solve(a, value * bvalue);
                default:
                    throw new IllegalArgumentException(s[1]);
            }
        }
    }

    private boolean markHumanInvolved(String key) {
        if ("humn".equals(key)) {
            humanInvolved.add(key);
            return true;
        }
        String[] s = map.get(key).split(" ");
        if (s.length == 3) {
            if (markHumanInvolved(s[0]) || markHumanInvolved(s[2])) {
                humanInvolved.add(key);
                return true;
            }
        }
        return false;
    }

    private void first() {
        System.out.println(calc("root"));
    }

    private long calc(String key) {
        String[] s = map.get(key).split(" ");
        if (s.length == 1) {
            return Long.parseLong(s[0]);
        } else {
            switch (s[1]) {
                case "+":
                    return calc(s[0]) + calc(s[2]);
                case "-":
                    return calc(s[0]) - calc(s[2]);
                case "*":
                    return calc(s[0]) * calc(s[2]);
                case "/":
                    return calc(s[0]) / calc(s[2]);
                default:
                    throw new IllegalArgumentException(s[1]);
            }
        }
    }
}
