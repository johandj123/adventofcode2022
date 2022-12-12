import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 {
    private CharMatrix m;
    private CharMatrix.Position start;
    private CharMatrix.Position end;

    public static void main(String[] args) throws IOException {
        new Day12().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        Set<CharMatrix.Position> explored = new HashSet<>();
        Set<CharMatrix.Position> current = new HashSet<>();
        current.add(start);
        int counter = 0;
        while (!current.contains(end)) {
            explored.addAll(current);
            Set<CharMatrix.Position> next = new HashSet<>();
            for (CharMatrix.Position position : current) {
                for (CharMatrix.Position nextPostion : getNeighbours(position)) {
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
        Set<CharMatrix.Position> explored = new HashSet<>();
        Set<CharMatrix.Position> current = new HashSet<>();
        current.add(end);
        int counter = 0;
        while (current.stream().noneMatch(position -> position.get() == 'a')) {
            explored.addAll(current);
            Set<CharMatrix.Position> next = new HashSet<>();
            for (CharMatrix.Position position : current) {
                for (CharMatrix.Position nextPostion : getNeighboursReverse(position)) {
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

    private List<CharMatrix.Position> getNeighbours(CharMatrix.Position position) {
        List<CharMatrix.Position> result = new ArrayList<>();
        char c1 = position.get();
        for (CharMatrix.Position next : position.getNeighbours()) {
            char c2 = next.get();
            if (c2 != ' ' && c2 <= c1 + 1) {
                result.add(next);
            }
        }
        return result;
    }

    private List<CharMatrix.Position> getNeighboursReverse(CharMatrix.Position position) {
        List<CharMatrix.Position> result = new ArrayList<>();
        char c1 = position.get();
        for (CharMatrix.Position next : position.getNeighbours()) {
            char c2 = next.get();
            if (c2 != ' ' && c2 >= c1 - 1) {
                result.add(next);
            }
        }
        return result;
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input12.txt").toPath());
        m = new CharMatrix(lines);
        start = m.find('S').get();
        start.set('a');
        end = m.find('E').get();
        end.set('z');
    }

}
