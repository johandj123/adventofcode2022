import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day15 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = Files.readAllLines(new File("input15.txt").toPath())
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
        first(lines);
        second(lines);
    }

    private static void second(List<Line> lines) {
        for (int yy = 0; yy <= 4000000; yy++) {
            int y = yy;
            List<Line> ll = lines.stream()
                    .filter(line -> line.relevant(y))
                    .collect(Collectors.toList());
            int left = ll.stream()
                    .mapToInt(line -> line.leftmost(y))
                    .min().orElseThrow();
            int right = ll.stream()
                    .mapToInt(line -> line.rightmost(y))
                    .max().orElseThrow();

            for (int xx = left; xx < right; xx++) {
                int x = xx;
                List<Line> l = ll.stream().filter(line -> line.inside(x, y)).collect(Collectors.toList());
                if (l.isEmpty()) {
                    long second = (x * 4000000L) + ((long) y);
                    System.out.println(second);
                    return;
                } else {
                    xx = l.stream().mapToInt(line -> line.rightmost(y)).max().orElseThrow();
                }
            }
        }
    }

    private static void first(List<Line> lines) {
        int left = lines.stream().mapToInt(line -> line.sensor.x - line.d).min().orElseThrow();
        int right = lines.stream().mapToInt(line -> line.sensor.x + line.d).max().orElseThrow();
        long first = 0;
        for (int x = left; x <= right; x++) {
            Position position = new Position(x, 2000000);
            boolean counts = false;
            for (Line line : lines) {
                if (!position.equals(line.beacon) && !position.equals(line.sensor)) {
                    int d = line.sensor.manhattan(position);
                    if (d <= line.d) {
                        counts = true;
                        break;
                    }
                }
            }
            if (counts) {
                first++;
            }
        }
        System.out.println(first);
    }

    static class Line {
        final Position sensor;
        final Position beacon;
        final int d;

        public Line(String line) {
            String[] parts = line.split("[^\\d]+");
            sensor = new Position(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            beacon = new Position(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
            d = sensor.manhattan(beacon);
        }

        public boolean inside(int x,int y)
        {
            return sensor.manhattan(new Position(x, y)) <= d;
        }

        public boolean relevant(int y)
        {
            int dd = d - Math.abs(sensor.y - y);
            return dd >= 0;
        }

        public int leftmost(int y)
        {
            int dd = d - Math.abs(sensor.y - y);
             return sensor.x - dd;
        }

        public int rightmost(int y)
        {
            int dd = d - Math.abs(sensor.y - y);
            return sensor.x + dd;
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

        public int manhattan(Position o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y);
        }
    }
}
