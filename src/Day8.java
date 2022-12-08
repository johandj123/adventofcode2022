import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input8.txt").toPath());
        int[][] c = new int[lines.size()][lines.get(0).length()];
        for (int y = 0; y < c.length; y++) {
            for (int x = 0; x < c[0].length; x++) {
                c[y][x] = lines.get(y).charAt(x) - '0';
            }
        }
        Set<Position> vis = new HashSet<>();
        for (int y = 0; y < c.length; y++) {
            visible(c, vis, 0, y, 1, 0);
            visible(c, vis, c[0].length - 1, y, -1, 0);
        }
        for (int x = 0; x < c[0].length; x++) {
            visible(c, vis, x, 0, 0, 1);
            visible(c, vis, x, c.length - 1, 0, -1);
        }
        System.out.println(vis.size());
//        printvis(c, vis);
        int maxscenic = 0;
        for (int y = 0; y < c.length; y++) {
            for (int x = 0; x < c[0].length; x++) {
                maxscenic = Math.max(maxscenic, scenicScore(c, x, y));
            }
        }
        System.out.println(maxscenic);
    }

    private static void printvis(int[][] c, Set<Position> vis) {
        for (int y = 0; y < c.length; y++) {
            for (int x = 0; x < c[0].length; x++) {
                System.out.print(vis.contains(new Position(x, y)) ? "*" : ".");
            }
            System.out.println();
        }
    }

    static void visible(int[][] c, Set<Position> vis, int x, int y, int dx, int dy) {
        int max = c[y][x];
        vis.add(new Position(x, y));
        while (true) {
            x += dx;
            y += dy;
            if (!(x >= 0 && y >= 0 && x < c[0].length && y < c.length)) break;
            int next = c[y][x];
            if (next > max) vis.add(new Position(x, y));
            max = Math.max(next, max);
        }
    }

    static int scenicScore(int[][] c, int x, int y) {
        return vdist(c, x, y, 1, 0) * vdist(c, x, y, -1, 0) * vdist(c, x, y, 0, 1) * vdist(c, x, y, 0, -1);
    }

    static int vdist(int[][] c, int x, int y, int dx, int dy) {
        int count = 0;
        int start = c[y][x];
        while (true) {
            x += dx;
            y += dy;
            if (!(x >= 0 && y >= 0 && x < c[0].length && y < c.length)) break;
            count++;
            int next = c[y][x];
            if (next >= start) break;
        }
        return count;
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
    }
}
