import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day23 {
    private Set<Position> initialElves;

    public static void main(String[] args) throws IOException {
        new Day23().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        Set<Position> elves = new HashSet<>(initialElves);
        for (int i = 0; i < 10; i++) {
            elves = round(elves, i);
        }
        System.out.println(elvesInBox(elves));
    }

    private void second()
    {
        Set<Position> elves = new HashSet<>(initialElves);
        int i = 0;
        while (true) {
            Set<Position> next = round(elves, i++);
            if (elves.equals(next)) {
                break;
            }
            elves = next;
        }
        System.out.println(i);
    }

    private int elvesInBox(Set<Position> elves) {
        int xmin = Integer.MAX_VALUE;
        int xmax = Integer.MIN_VALUE;
        int ymin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        for (Position position : elves) {
            if (position.x < xmin) {
                xmin = position.x;
            }
            if (position.x > xmax) {
                xmax = position.x;
            }
            if (position.y < ymin) {
                ymin = position.y;
            }
            if (position.y > ymax) {
                ymax = position.y;
            }
        }
        int w = (xmax - xmin) + 1;
        int h = (ymax - ymin) + 1;
        return (w * h) - elves.size();
    }

    private Set<Position> round(Set<Position> elves,int start) {
        Map<Position, Integer> map = new HashMap<>();
        for (Position elf : elves) {
            Position next = proposePosition(elf, elves, start);
            int count = map.getOrDefault(next, 0);
            map.put(next, count + 1);
        }
        Set<Position> result = new HashSet<>();
        for (Position elf : elves) {
            Position next = proposePosition(elf, elves, start);
            if (map.get(next) == 1) {
                result.add(next);
            } else {
                result.add(elf);
            }
        }
        return result;
    }

    private Position proposePosition(Position position, Set<Position> elves, int start) {
        if (!elves.contains(position.add(0, -1)) &&
                !elves.contains(position.add(-1, -1)) &&
                !elves.contains(position.add(1, -1)) &&
                !elves.contains(position.add(0, 1)) &&
                !elves.contains(position.add(-1, 1)) &&
                !elves.contains(position.add(1, 1)) &&
                !elves.contains(position.add(-1, 0)) &&
                !elves.contains(position.add(1, 0))) {
            return position;
        }

        for (int i = start; i < start + 4; i++) {
            int rule = i % 4;
            switch (rule) {
                case 0:
                    if (!elves.contains(position.add(0, -1)) &&
                            !elves.contains(position.add(-1, -1)) &&
                            !elves.contains(position.add(1, -1))) {
                        return position.add(0, -1);
                    }
                    break;
                case 1:
                    if (!elves.contains(position.add(0, 1)) &&
                            !elves.contains(position.add(-1, 1)) &&
                            !elves.contains(position.add(1, 1))) {
                        return position.add(0, 1);
                    }
                    break;
                case 2:
                    if (!elves.contains(position.add(-1, 0)) &&
                            !elves.contains(position.add(-1, -1)) &&
                            !elves.contains(position.add(-1, 1))) {
                        return position.add(-1, 0);
                    }
                    break;
                case 3:
                    if (!elves.contains(position.add(1, 0)) &&
                            !elves.contains(position.add(1, -1)) &&
                            !elves.contains(position.add(1, 1))) {
                        return position.add(1, 0);
                    }
                    break;
            }
        }
        return position;
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input23.txt").toPath());
        CharMatrix charMatrix = new CharMatrix(lines);
        initialElves = new HashSet<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '#') {
                    initialElves.add(new Position(x, y));
                }
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

        public Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }
}
