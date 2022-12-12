import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day12 {
    private CharMatrix m;
    private Position start;
    private Position end;

    public static void main(String[] args) throws IOException {
        new Day12().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        Set<Position> explored = new HashSet<>();
        Set<Position> current = new HashSet<>();
        current.add(start);
        int counter = 0;
        while (!current.contains(end)) {
            explored.addAll(current);
            Set<Position> next = new HashSet<>();
            for (Position position : current) {
                for (Position nextPostion : getNeighbours(position)) {
                    if (!explored.contains(nextPostion)) {
                        next.add(nextPostion);
                    }
                }
            }
            counter++;
            current = next;
        }
        System.out.println(counter);
    }

    private void second() {
        Set<Position> explored = new HashSet<>();
        Set<Position> current = new HashSet<>();
        current.add(end);
        int counter = 0;
        while (current.stream().noneMatch(position -> m.get(position.x, position.y) == 'a')) {
            explored.addAll(current);
            Set<Position> next = new HashSet<>();
            for (Position position : current) {
                for (Position nextPostion : getNeighboursReverse(position)) {
                    if (!explored.contains(nextPostion)) {
                        next.add(nextPostion);
                    }
                }
            }
            counter++;
            current = next;
        }
        System.out.println(counter);
    }

    private List<Position> getNeighbours(Position position) {
        List<Position> result = new ArrayList<>();
        char c1 = m.get(position.x, position.y);
        for (Position delta : new Position[] {
                new Position(-1, 0),
                new Position(1, 0),
                new Position(0, -1),
                new Position(0, 1),
        }) {
            Position next = position.add(delta);
            char c2 = m.getUnbounded(next.x, next.y);
            if (c2 != ' ' && c2 <= c1 + 1) {
                result.add(next);
            }
        }
        return result;
    }

    private List<Position> getNeighboursReverse(Position position) {
        List<Position> result = new ArrayList<>();
        char c1 = m.get(position.x, position.y);
        for (Position delta : new Position[] {
                new Position(-1, 0),
                new Position(1, 0),
                new Position(0, -1),
                new Position(0, 1),
        }) {
            Position next = position.add(delta);
            char c2 = m.getUnbounded(next.x, next.y);
            if (c2 != ' ' && c2 >= c1 - 1) {
                result.add(next);
            }
        }
        return result;
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input12.txt").toPath());
        m = new CharMatrix(lines.toArray(new String[0]));
        for (int y = 0; y < m.getHeight(); y++) {
            for (int x = 0; x < m.getWidth(); x++) {
                char c = m.get(x, y);
                if (c == 'S') {
                    start = new Position(x, y);
                    m.set(x, y, 'a');
                } else if (c == 'E') {
                    end = new Position(x, y);
                    m.set(x,  y, 'z');
                }
            }
        }
    }

    static class Position
    {
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

        public Position add(Position delta)
        {
            return new Position(x + delta.x, y + delta.y);
        }
    }
}
