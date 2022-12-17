import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {
    String moves;
    int moveCount;
    List<Set<Position>> shapes = new ArrayList<>();
    int shapeCount;

    public static void main(String[] args) throws IOException {
        new Day17().start();
    }

    private void start() throws IOException {
        init();
        first();
    }

    private void first() {
        Set<Position> field = new HashSet<>();
        for (int i = 0; i < 2022; i++) {
            Set<Position> current = nextShape();
            int ymax = towerHeight(field);
            current = move(current, 2, ymax + 3);
            while (true) {
                int dx = nextMove();
                Set<Position> afterMove = move(current, dx, 0);
                if (inBounds(afterMove) && noCommon(afterMove, field)) {
                    current = afterMove;
                }
                Set<Position> afterFall = move(current, 0, -1);
                if (inBounds(afterFall) && noCommon(afterFall, field)) {
                    current = afterFall;
                } else {
                    break;
                }
            }
//            printField(field, current);
            field.addAll(current);
        }
        System.out.println(towerHeight(field));
    }

    private void printField(Set<Position> field, Set<Position> current) {
        int h = Math.max(towerHeight(field), towerHeight(current));
        for (int y = h - 1; y >= 0;  y--) {
            System.out.print('|');
            for (int x = 0; x < 7; x++) {
                Position position = new Position(x, y);
                System.out.print(field.contains(position) ? '#' :
                        (current.contains(position) ? '@' : '.'));
            }
            System.out.println('|');
        }
        System.out.println("+-------+");
    }

    private int towerHeight(Set<Position> field) {
        return field.stream().mapToInt(p -> p.y + 1).max().orElse(0);
    }

    private void init() throws IOException {
        moves = Files.readString(new File("input17.txt").toPath()).trim();
        shapes.add(Set.of(
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0),
                new Position(3, 0)
        ));
        shapes.add(Set.of(
                new Position(1, 0),
                new Position(0, 1),
                new Position(1, 1),
                new Position(2, 1),
                new Position(1, 2)
        ));
        shapes.add(Set.of(
                new Position(2, 2),
                new Position(2, 1),
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0)
        ));
        shapes.add(Set.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(0, 3)
        ));
        shapes.add(Set.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(1, 0),
                new Position(1, 1)
        ));
    }

    private int nextMove() {
        char c = moves.charAt(moveCount++);
        if (moveCount == moves.length()) {
            moveCount = 0;
        }
        return c == '<' ? -1 : 1;
    }

    private Set<Position> nextShape() {
        Set<Position> result = shapes.get(shapeCount++);
        if (shapeCount == shapes.size()) {
            shapeCount = 0;
        }
        return result;
    }

    private boolean noCommon(Set<Position> a, Set<Position> b) {
        for (Position position : a) {
            if (b.contains(position)) {
                return false;
            }
        }
        return true;
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position add(int dx, int dy) {
            return new Position(x + dx, y + dy);
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

    private boolean inBounds(Set<Position> positions) {
        return positions.stream()
                .allMatch(position -> position.x >= 0 && position.x < 7 && position.y >= 0);
    }

    private Set<Position> move(Set<Position> positions, int dx, int dy) {
        return positions.stream()
                .map(position -> position.add(dx, dy))
                .collect(Collectors.toSet());
    }
}
