import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 {
    private CharMatrix charMatrix;
    private List<Blizzard> blizzards = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day24().start();
    }

    private void start() throws IOException {
        readInput();
        route(false);
        blizzards.forEach(b ->  b.position = b.initialPosition);
        route(true);
    }

    private void route(boolean second) {
        Set<CharMatrix.Position> current = new HashSet<>();
        CharMatrix.Position startPosition = charMatrix.new Position(1, 0);
        CharMatrix.Position endPosition = charMatrix.new Position(charMatrix.getWidth() - 2, charMatrix.getHeight() - 1);
        current.add(startPosition);
        int counter = 0;
        int round = second ? 0 : 2;
        while (!current.isEmpty()) {
            if (current.contains(endPosition) && (round == 0 || round == 2)) {
                if (round == 2) {
                    System.out.println(counter);
                    return;
                } else {
                    round = 1;
                    current = new HashSet<>();
                    current.add(endPosition);
                }
            }
            if (current.contains(startPosition) && round == 1) {
                round = 2;
                current = new HashSet<>();
                current.add(startPosition);
            }
            blizzards.forEach(Blizzard::advance);
            Set<CharMatrix.Position> occupied = blizzards.stream()
                    .map(b -> b.position)
                    .collect(Collectors.toSet());
            Set<CharMatrix.Position> next = new HashSet<>();
            for (CharMatrix.Position position : current) {
                for (CharMatrix.Position delta : new CharMatrix.Position[]{
                        charMatrix.new Position(-1, 0),
                        charMatrix.new Position(1, 0),
                        charMatrix.new Position(0, -1),
                        charMatrix.new Position(0, 1),
                        charMatrix.new Position(0, 0)
                }) {
                    CharMatrix.Position nextPosition = position.add(delta);
                    if (nextPosition.isValid() &&
                            nextPosition.get() != '#' &&
                            !occupied.contains(nextPosition)) {
                        next.add(nextPosition);
                    }
                }
            }
            counter++;
            current = next;
        }
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input24.txt").toPath());
        charMatrix = new CharMatrix(lines);
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                char c = position.get();
                switch (c) {
                    case '<':
                        blizzards.add(new Blizzard(position, charMatrix.new Position(-1, 0)));
                        break;
                    case '>':
                        blizzards.add(new Blizzard(position, charMatrix.new Position(1, 0)));
                        break;
                    case '^':
                        blizzards.add(new Blizzard(position, charMatrix.new Position(0, -1)));
                        break;
                    case 'v':
                        blizzards.add(new Blizzard(position, charMatrix.new Position(0, 1)));
                        break;
                }
            }
        }
    }

    class Blizzard
    {
        CharMatrix.Position initialPosition;
        CharMatrix.Position position;
        CharMatrix.Position delta;

        public Blizzard(CharMatrix.Position position, CharMatrix.Position delta) {
            this.initialPosition = position;
            this.position = position;
            this.delta = delta;
        }

        public void advance()
        {
            CharMatrix.Position next = position.add(delta);
            if (next.get() == '#') {
                while (next.get() == '#') {
                    next = next.add(delta);
                    next = next.wrap();
                }
            }
            position = next;
        }
    }
}
