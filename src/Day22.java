import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Day22 {
    CharMatrix charMatrix;
    List<String> directions = new ArrayList<>();
    CharMatrix.Position delta;

    public static void main(String[] args) throws IOException {
        new Day22().start();
    }

    private void start() throws IOException {
        readInput();
//        aroundTheWorld();
        followPath(this::move);
        followPath(this::moveOnCube);
    }

    private void aroundTheWorld() {
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '#') {
                    charMatrix.set(x, y, '.');
                }
            }
        }
        testFor(charMatrix.new Position(1, 0), charMatrix.new Position(51, 1));
        testFor(charMatrix.new Position(-1, 0), charMatrix.new Position(51, 1));
        testFor(charMatrix.new Position(0, 1), charMatrix.new Position(51, 1));
        testFor(charMatrix.new Position(0, -1), charMatrix.new Position(51, 1));
        testFor(charMatrix.new Position(1, 0), charMatrix.new Position(51, 51));
        testFor(charMatrix.new Position(-1, 0), charMatrix.new Position(51, 51));
    }

    private void testFor(CharMatrix.Position delta, CharMatrix.Position startPosition) {
        CharMatrix.Position position = startPosition;
        this.delta = delta;
        System.out.println("Delta " + this.delta);
        for (int i = 0; i < 5; i++) {
            System.out.println(position);
            position = move(position, 50, this::moveOnCube);
        }
        System.out.println();
    }

    private void followPath(IMove move) {
        int y = 0;
        int x = 0;
        while (charMatrix.getWrap(x, y) == ' ') x++;
        CharMatrix.Position position = charMatrix.new Position(x, y);
        delta = charMatrix.new Position(1, 0);
        for (String direction : directions) {
            if ("L".equals(direction)) {
                delta = left(delta);
            } else if ("R".equals(direction)) {
                delta = right(delta);
            } else {
                position = move(position, Integer.parseInt(direction), move);
            }
        }
        int row = 1 + (Math.floorMod(position.getY(), charMatrix.getHeight()));
        int column = 1 + (Math.floorMod(position.getX(), charMatrix.getWidth()));
        int facing = (delta.getY() == 0 ? (
                    delta.getX() == -1 ? 2 : 0
                ) : (
                    delta.getY() == -1 ? 1 : 3
                ));
        System.out.println(row * 1000 + column * 4 + facing);
    }

    private CharMatrix.Position right(CharMatrix.Position delta) {
        return charMatrix.new Position(-delta.getY(), delta.getX());
    }

    private CharMatrix.Position left(CharMatrix.Position delta) {
        return charMatrix.new Position(delta.getY(), -delta.getX());
    }

    private CharMatrix.Position turnAround(CharMatrix.Position delta) {
        return charMatrix.new Position(-delta.getX(), -delta.getY());
    }

    private CharMatrix.Position move(CharMatrix.Position position,int count,IMove move)
    {
        CharMatrix.Position current = position;
        for (int i = 0; i < count; i++) {
            CharMatrix.Position savedDelta = delta;
            CharMatrix.Position next = move.move(current);
            if (next.getWrap() == '#') {
                delta = savedDelta;
                return current;
            }
            current = next;
        }
        return current;
    }

    private CharMatrix.Position move(CharMatrix.Position position)
    {
        CharMatrix.Position current = position;
        do {
            current = current.add(delta);
        } while (current.getWrap() == ' ');
        return current;
    }

    private CharMatrix.Position moveOnCube(CharMatrix.Position position)
    {
        CharMatrix.Position originalDelta = delta;
        CharMatrix.Position current = position.add(delta);
        if (current.getUnbounded() == ' ') {
            current = wrapAroundCube(position);
            if (current.getUnbounded() == ' ') {
                throw new IllegalStateException();
            }

            // @ Check inverse
//            CharMatrix.Position savedDelta = delta;
//            delta = turnAround(delta);
//            CharMatrix.Position back = wrapAroundCube(current);
//            if (!back.equals(position)) {
//                delta = originalDelta;
//                current = wrapAroundCube(position);
//                delta = turnAround(delta);
//                current = wrapAroundCube(current);
//                throw new UnsupportedOperationException();
//            }
//            delta = savedDelta;
        }
        return current;
    }

    private CharMatrix.Position wrapAroundCube(CharMatrix.Position position) {
        CharMatrix.Position current;
        int bx = Math.floorDiv(position.getX(), 50);
        int by = Math.floorDiv(position.getY(), 50);
        int px = Math.floorMod(position.getX(), 50);
        int py = Math.floorMod(position.getY(), 50);
        checkAtBorder(px, py);
        if (bx == 1 && by == 0) {
            // Face 1
            if (delta.getX() == -1) {
                delta = charMatrix.new Position(1, 0);
                bx = 0;
                by = 2;
                py = 49 - py;
            } else if (delta.getY() == -1) {
                delta = charMatrix.new Position(1, 0);
                bx = 0;
                by = 3;
                py = px;
                px = 0;
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (bx == 2 && by == 0) {
            // Face 2
            if (delta.getY() == -1) {
                delta = charMatrix.new Position(0, -1);
                bx = 0;
                by = 3;
                py = 49;
            } else if (delta.getX() == 1) {
                delta = charMatrix.new Position(-1, 0);
                bx = 1;
                by = 2;
                py = 49 - py;
            } else if (delta.getY() == 1) {
                delta = charMatrix.new Position(-1, 0);
                bx = 1;
                by = 1;
                py = px;
                px = 49;
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (bx == 1 && by == 1) {
            // Face 3
            if (delta.getX() == -1) {
                delta = charMatrix.new Position(0, 1);
                bx = 0;
                by = 2;
                px = py;
                py = 0;
            } else if (delta.getX() == 1) {
                delta = charMatrix.new Position(0, -1);
                bx = 2;
                by = 0;
                px = py;
                py = 49;
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (bx == 0 && by == 2) {
            // Face 4
            if (delta.getY() == -1) {
                delta = charMatrix.new Position(1, 0);
                bx = 1;
                by = 1;
                py = px;
                px = 0;
            } else if (delta.getX() == -1) {
                delta = charMatrix.new Position(1, 0);
                bx = 1;
                by = 0;
                py = 49 - py;
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (bx == 1 && by == 2) {
            // Face 5
            if (delta.getX() == 1) {
                delta = charMatrix.new Position(-1, 0);
                bx = 2;
                by = 0;
                py = 49 - py;
            } else if (delta.getY() == 1) {
                delta = charMatrix.new Position(-1, 0);
                bx = 0;
                by = 3;
                py = px;
                px = 49;
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (bx == 0 && by == 3) {
            // Face 6
            if (delta.getX() == -1) {
                delta = charMatrix.new Position(0, 1);
                bx = 1;
                by = 0;
                px = py;
                py = 0;
            } else if (delta.getY() == 1) {
                delta = charMatrix.new Position(0, 1);
                bx = 2;
                by = 0;
                py = 0;
            } else if (delta.getX() == 1) {
                delta = charMatrix.new Position(0, -1);
                bx = 1;
                by = 2;
                px = py;
                py = 49;
            }
        } else {
            throw new UnsupportedOperationException();
        }
        checkAtBorder(px, py);
        current = charMatrix.new Position(bx * 50 + px, by * 50 + py);
        return current;
    }

    private void checkAtBorder(int px,int py) {
        if (px != 0 && px != 49 && py != 0 && py != 49) {
            throw new UnsupportedOperationException();
        }
    }

    private void readInput() throws IOException {
        String input = Files.readString(new File("input22.txt").toPath());
        String[] parts = input.split("\n\n");
        charMatrix = new CharMatrix(parts[0]);
        String directionString = parts[1].trim();
        for (int i = 0; i < directionString.length(); i++) {
            char c = directionString.charAt(i);
            if (c >= '0' && c <= '9') {
                StringBuilder sb = new StringBuilder();
                while (i < directionString.length() && directionString.charAt(i) >= '0' && directionString.charAt(i) <= '9') {
                    sb.append(directionString.charAt(i));
                    i++;
                }
                i--;
                directions.add(sb.toString());
            } else {
                directions.add(Character.toString(c));
            }
        }
    }

    interface IMove
    {
        CharMatrix.Position move(CharMatrix.Position position);
    }
}
