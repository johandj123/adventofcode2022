import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Day17 {
    String moves;
    int moveCount;
    List<Shape> shapes = new ArrayList<>();
    int shapeCount;

    public static void main(String[] args) throws IOException {
        new Day17().start();
    }

    private void start() throws IOException {
        init();
        drop(2022);
        long start = new Date().getTime();
        drop(100000000);
        System.out.println("ms: " + (new Date().getTime() - start));
    }

    private void drop(int count) {
        moveCount = 0;
        shapeCount = 0;
        Field field = new Field();
        for (int i = 0; i < count; i++) {
            Shape shape = nextShape();
            long ymax = field.towerHeight();
            int dx = 2;
            long dy = ymax + 3;
            while (true) {
                int movex = nextMove();
                if (dx + movex >= 0 && dx + movex < shape.xlimit && field.noCommon(shape.positions, dx + movex, dy)) {
                    dx += movex;
                }
                if (dy - 1 > 0 && field.noCommon(shape.positions, dx, dy - 1)) {
                    dy--;
                } else {
                    break;
                }
            }
//            printField(field, current);
            field.addAll(shape.positions, dx, dy);
        }
        System.out.println(field.towerHeight());
    }

//    private void printField(Field field, List<Position> current) {
//        long h = field.towerHeight() + 4;
//        for (long y = h; y >= 0; y--) {
//            System.out.print('|');
//            for (int x = 0; x < 7; x++) {
//                Position position = new Position(x, y);
//                System.out.print(field.contains(position.x, position.y) ? '#' :
//                        (current.contains(position) ? '@' : '.'));
//            }
//            System.out.println('|');
//        }
//        System.out.println("+-------+");
//    }

    private void init() throws IOException {
        moves = Files.readString(new File("input17.txt").toPath()).trim();
        shapes.add(new Shape(
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0),
                new Position(3, 0)
        ));
        shapes.add(new Shape(
                new Position(1, 0),
                new Position(0, 1),
                new Position(1, 1),
                new Position(2, 1),
                new Position(1, 2)
        ));
        shapes.add(new Shape(
                new Position(2, 2),
                new Position(2, 1),
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0)
        ));
        shapes.add(new Shape(
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(0, 3)
        ));
        shapes.add(new Shape(
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

    private Shape nextShape() {
        Shape result = shapes.get(shapeCount++);
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
    }

    static class RollArray {
        static final int HPART = 100;
        int start = 0;
        long ystart = 0;
        boolean[][] array = new boolean[HPART][7];

        int getIndex(long y)
        {
            if (y < ystart || y >= ystart + HPART) {
                throw new IllegalArgumentException();
            }
            long r = y - ystart;
            return ((int)r + start) % HPART;
        }

        boolean contains(int x,long y)
        {
            if (y >= ystart + HPART) {
                return false;
            }
            return array[getIndex(y)][x];
        }

        void add(int x,long y)
        {
            while (y >= ystart + HPART) {
                Arrays.fill(array[start], false);
                start = (start + 1) % HPART;
                ystart++;
            }
            array[getIndex(y)][x] = true;
        }
    }

    static class Field {
        long h = 0;
        RollArray rollArray = new RollArray();

        public void addAll(Position[] positions, int dx, long dy) {
            for (Position position : positions) {
                h = Math.max(h, position.y + dy + 1);
                add(position.x + dx, position.y + dy);
            }
        }

        private void add(int px,long py) {
            rollArray.add(px, py);
        }

        public boolean contains(int px, long py) {
            return rollArray.contains(px, py);
        }

        public boolean noCommon(Position[] a, int dx, long dy) {
            for (Position position : a) {
                if (contains(position.x + dx, position.y + dy)) {
                    return false;
                }
            }
            return true;
        }

        public long towerHeight() {
            return h;
        }
    }

    static class Shape
    {
        Position[] positions;
        int xlimit;

        public Shape(Position... positions) {
            this.positions = positions;
            xlimit = 7 - Arrays.stream(positions)
                    .mapToInt(p -> p.x)
                    .max()
                    .orElseThrow();
        }
    }
}
