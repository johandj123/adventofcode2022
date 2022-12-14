import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 {
    private Map<Position, Character> field;
    private int maxY;

    public static void main(String[] args) throws IOException {
        new Day14().start();

    }

    private void start() throws IOException {
        readInput();
        maxY = field.keySet().stream().mapToInt(position -> position.y).max().orElseThrow();
        first();
        readInput();
        second();
    }

    private void second() {
        for (int x = -10000; x <= 10000; x++) {
            field.put(new Position(x, maxY + 2), '#');
        }
        while (!field.containsKey(new Position(500, 0))) {
            if (!dropSand()) {
                throw new IllegalStateException();
            }
        }
        System.out.println(field.values().stream().filter(c -> c == 'o').count());
    }

    private void first() {
        while (dropSand()) {
        }
        System.out.println(field.values().stream().filter(c -> c == 'o').count());
    }

    private boolean dropSand() {
        Position position = new Position(500, 0);
        while (true) {
            if (position.y > maxY + 2) {
                return false;
            }
            Position down = new Position(position.x,position.y + 1);
            if (!field.containsKey(down)) {
                position = down;
                continue;
            }
            Position downLeft = new Position(position.x - 1, position.y + 1);
            if (!field.containsKey(downLeft)) {
                position = downLeft;
                continue;
            }
            Position downRight = new Position(position.x + 1, position.y + 1);
            if (!field.containsKey(downRight)) {
                position = downRight;
                continue;
            }
            field.put(position, 'o');
            break;
        }
        return true;
    }

    private void readInput() throws IOException {
        field = new HashMap<>();
        List<String> lines = Files.readAllLines(new File("input14.txt").toPath());
        for (String line : lines) {
            String[] points = line.split(" -> ");
            List<Position> positions = Arrays.stream(points).map(Position::new).collect(Collectors.toList());
            for (int i = 0; i < positions.size() - 1; i++) {
                Position a = positions.get(i);
                Position b = positions.get(i + 1);
                if (a.x == b.x) {
                    for (int y = Math.min(a.y, b.y); y <= Math.max(a.y, b.y); y++) {
                        field.put(new Position(a.x, y), '#');
                    }
                } else if (a.y == b.y) {
                    for (int x = Math.min(a.x, b.x); x <= Math.max(a.x, b.x); x++) {
                        field.put(new Position(x, a.y), '#');
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    static class Position
    {
        private final int x;
        private final int y;

        public Position(String s) {
            String[] sp = s.split(",");
            x = Integer.parseInt(sp[0]);
            y = Integer.parseInt(sp[1]);
        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
