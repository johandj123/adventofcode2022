import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18 {
    private static final Position[] SIDES = new Position[]{
            new Position(1, 0, 0),
            new Position(-1, 0, 0),
            new Position(0, -1, 0),
            new Position(0, 1, 0),
            new Position(0, 0, -1),
            new Position(0, 0, 1),
    };

    public static void main(String[] args) throws IOException {
        Set<Position> positions = Files.readAllLines(new File("input18.txt").toPath()).stream()
                .map(Position::new)
                .collect(Collectors.toSet());
        first(positions);
        second(positions);
    }

    private static void first(Set<Position> positions) {
        int count = 0;
        for (Position position : positions) {
            for (Position side : SIDES) {
                Position x = position.add(side);
                if (!positions.contains(x)) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    private static void second(Set<Position> positions) {
        Position low = new Position(
                positions.stream().mapToInt(p -> p.x).min().orElseThrow() - 1,
                positions.stream().mapToInt(p -> p.y).min().orElseThrow() - 1,
                positions.stream().mapToInt(p -> p.z).min().orElseThrow() - 1
        );
        Position high = new Position(
                positions.stream().mapToInt(p -> p.x).max().orElseThrow() + 1,
                positions.stream().mapToInt(p -> p.y).max().orElseThrow() + 1,
                positions.stream().mapToInt(p -> p.z).max().orElseThrow() + 1
        );
        Set<Position> done = new HashSet<>();
        Set<Position> current = new HashSet<>();
        current.add(low);
        while (!current.isEmpty()) {
            done.addAll(current);
            Set<Position> next = new HashSet<>();
            for (Position position : current) {
                for (Position side : SIDES) {
                    Position nextPos = position.add(side);
                    if (!done.contains(nextPos) &&
                            !positions.contains(nextPos) &&
                            nextPos.inBounds(low, high)) {
                        next.add(nextPos);
                    }
                }
            }
            current = next;
        }

        int count = 0;
        for (Position position : done) {
            for (Position side : SIDES) {
                Position x = position.add(side);
                if (positions.contains(x)) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    static class Position {
        final int x;
        final int y;
        final int z;

        public Position(String line) {
            String[] split = line.split(",");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);
        }

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position add(Position o) {
            return new Position(x + o.x, y + o.y, z + o.z);
        }

        public boolean inBounds(Position low, Position high) {
            return (x >= low.x && y >= low.y && z >= low.z &&
                    x <= high.x && y <= high.y && z <= high.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
