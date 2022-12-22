import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Day22 {
    CharMatrix charMatrix;
    List<String> directions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day22().start();
    }

    private void start() throws IOException {
        readInput();
        first();
    }

    private void first() {
        int y = 0;
        int x = 0;
        while (charMatrix.getWrap(x, y) == ' ') x++;
        CharMatrix.Position position = charMatrix.new Position(x, y);
        CharMatrix.Position delta = charMatrix.new Position(1, 0);
        for (String direction : directions) {
            if ("L".equals(direction)) {
                delta = charMatrix.new Position(delta.getY(), -delta.getX());
            } else if ("R".equals(direction)) {
                delta = charMatrix.new Position(-delta.getY(), delta.getX());
            } else {
                position = move(position, delta, Integer.parseInt(direction));
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

    private CharMatrix.Position move(CharMatrix.Position position, CharMatrix.Position delta,int count)
    {
        CharMatrix.Position current = position;
        for (int i = 0; i < count; i++) {
            CharMatrix.Position next = move(current, delta);
            if (next.equals(current)) {
                return current;
            }
            current = next;
        }
        return current;
    }

    private CharMatrix.Position move(CharMatrix.Position position, CharMatrix.Position delta)
    {
        CharMatrix.Position current = position;
        do {
            current = current.add(delta);
        } while (current.getWrap() == ' ');
        if (current.getWrap() == '#') {
            return position;
        }
        return current;
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
}
