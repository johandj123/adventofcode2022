import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {
    String moves;
    int moveCount;
    List<List<Position>> shapes = new ArrayList<>();
    int shapeCount;

    public static void main(String[] args) throws IOException {
        new Day17().start();
    }

    private void start() throws IOException {
        init();
        drop(2022);
        drop(1000000);
    }

    private void drop(int count) {
        moveCount = 0;
        shapeCount = 0;
        Field field = new Field();
        for (int i = 0; i < count; i++) {
            List<Position> current = nextShape();
            int ymax = field.towerHeight();
            current = move(current, 2, ymax + 3);
            while (true) {
                int dx = nextMove();
                List<Position> afterMove = move(current, dx, 0);
                if (inBounds(afterMove) && field.noCommon(afterMove)) {
                    current = afterMove;
                }
                List<Position> afterFall = move(current, 0, -1);
                if (inBounds(afterFall) && field.noCommon(afterFall)) {
                    current = afterFall;
                } else {
                    break;
                }
            }
//            printField(field, current);
            field.addAll(current);
        }
        System.out.println(field.towerHeight());
    }

    private void printField(Field field, List<Position> current) {
        int h = field.towerHeight() + 4;
        for (int y = h; y >= 0;  y--) {
            System.out.print('|');
            for (int x = 0; x < 7; x++) {
                Position position = new Position(x, y);
                System.out.print(field.field.contains(position) ? '#' :
                        (current.contains(position) ? '@' : '.'));
            }
            System.out.println('|');
        }
        System.out.println("+-------+");
    }

    private void init() throws IOException {
        moves = Files.readString(new File("input17.txt").toPath()).trim();
        shapes.add(List.of(
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0),
                new Position(3, 0)
        ));
        shapes.add(List.of(
                new Position(1, 0),
                new Position(0, 1),
                new Position(1, 1),
                new Position(2, 1),
                new Position(1, 2)
        ));
        shapes.add(List.of(
                new Position(2, 2),
                new Position(2, 1),
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0)
        ));
        shapes.add(List.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(0, 3)
        ));
        shapes.add(List.of(
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

    private List<Position> nextShape() {
        List<Position> result = shapes.get(shapeCount++);
        if (shapeCount == shapes.size()) {
            shapeCount = 0;
        }
        return result;
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

    private boolean inBounds(List<Position> positions) {
        return positions.stream()
                .allMatch(position -> position.x >= 0 && position.x < 7 && position.y >= 0);
    }

    private List<Position> move(List<Position> positions, int dx, int dy) {
        return positions.stream()
                .map(position -> position.add(dx, dy))
                .collect(Collectors.toList());
    }

    static class Field
    {
        int h = 0;
        Set<Position> field = new HashSet<>();

        public void addAll(List<Position> positions)
        {
            for (Position position : positions) {
                h = Math.max(h, position.y + 1);
                field.add(position);
            }
            field = field.stream()
                    .filter(p -> p.y >= h - 50)
                    .collect(Collectors.toSet());
        }

        public boolean noCommon(List<Position> a) {
            for (Position position : a) {
                if (field.contains(position)) {
                    return false;
                }
            }
            return true;
        }

        public int towerHeight() {
            return h;
        }
    }
}
