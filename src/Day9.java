import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day9 {
    List<Position> moves = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day9().start();
    }

    private void start() throws IOException {
        readMoves();
        first();
        second();
    }

    private void first() {
        Position head = new Position(0, 0);
        Position tail = new Position(0, 0);
        Set<Position> positions = new HashSet<>();
        positions.add(tail);
        for (Position move : moves) {
            head = head.add(move);
            tail = correctTail(head, tail);
            positions.add(tail);
        }
        System.out.println(positions.size());
    }

    private void second() {
        Position[] knots = new Position[10];
        Arrays.fill(knots, new Position(0, 0));
        Set<Position> positions = new HashSet<>();
        positions.add(knots[knots.length - 1]);
        for (Position move : moves) {
            knots[0] = knots[0].add(move);
            for (int i = 0; i < knots.length - 1; i++) {
                knots[i + 1] = correctTail(knots[i], knots[i + 1]);
            }
            positions.add(knots[knots.length - 1]);
        }
        System.out.println(positions.size());

    }

    private Position correctTail(Position head,Position tail) {
        int dx = Math.abs(head.x - tail.x);
        int dy = Math.abs(head.y - tail.y);
        if (dx <= 1 && dy <= 1) {
            // Do nothing since head/tail are touching
            return tail;
        }

        if (dx <= 1 && dy == 2) {
            return new Position(head.x, towards(head.y, tail.y));
        }
        if (dx == 2 && dy <= 1) {
            return new Position(towards(head.x, tail.x), head.y);
        }
        if (dx == 2 && dy == 2) {
            return new Position(towards(head.x, tail.x), towards(head.y, tail.y));
        }
        throw new UnsupportedOperationException("Should not happen");
    }

    private int towards(int value,int to)
    {
        if (value == to - 2) {
            return to - 1;
        }
        if (value == to + 2) {
            return to + 1;
        }
        throw new UnsupportedOperationException("Should not happen");
    }

    private void readMoves() throws IOException {
        List<String> lines = Files.readAllLines(new File("input9.txt").toPath());
        for (String line : lines) {
            String[] split = line.split(" ");
            int n = Integer.parseInt(split[1]);
            Position dir = null;
            switch (split[0]) {
                case "L": dir = new Position(-1, 0); break;
                case "R": dir = new Position(1, 0); break;
                case "U": dir = new Position(0, -1); break;
                case "D": dir = new Position(0, 1); break;
            }
            Objects.requireNonNull(dir);
            for (int i = 0; i < n; i++) {
                moves.add(dir);
            }
        }
    }

    static class Position {
        final int x;
        final int y;

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

        public Position add(Position o)
        {
            return new Position(x + o.x, y + o.y);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }
}
