package lib;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CharMatrix {
    private char[][] content;
    private char fill;

    public CharMatrix(char[][] content, char fill) {
        this.content = content;
        this.fill = fill;
    }

    public CharMatrix(String[] content, char fill) {
        int width = Arrays.stream(content).mapToInt(String::length).max().orElse(0);
        this.content = new char[content.length][width];
        for (int y = 0; y < content.length; y++) {
            Arrays.fill(this.content[y], fill);
            for (int x = 0; x < content[y].length(); x++) {
                this.content[y][x] = content[y].charAt(x);
            }
        }
        this.fill = fill;
    }

    public CharMatrix(String[] content)
    {
        this(content, ' ');
    }

    public CharMatrix(String content, char fill) {
        this(content.split("\n"), fill);
    }

    public CharMatrix(String content) {
        this(content.split("\n"));
    }

    public int getWidth()
    {
        return content[0].length;
    }

    public int getHeight()
    {
        return content.length;
    }

    public char get(int x,int y)
    {
        return content[y][x];
    }

    public char getUnbounded(int x,int y)
    {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return fill;
        } else {
            return content[y][x];
        }
    }

    public char getWrap(int x,int y)
    {
        return content[Math.floorMod(y, getHeight())][Math.floorMod(x, getWidth())];
    }

    public void set(int x,int y,char c)
    {
        content[y][x] = c;
    }

    public String getRow(int y)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < content[y].length; x++) {
            stringBuilder.append(content[y][x]);
        }
        return stringBuilder.toString();
    }

    public String getColumn(int x)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < content.length; y++) {
            stringBuilder.append(content[y][x]);
        }
        return stringBuilder.toString();
    }

    public CharMatrix transpose()
    {
        String[] s = new String[getWidth()];
        for (int i = 0; i < getWidth(); i++) {
            s[i] = getColumn(i);
        }
        return new CharMatrix(s);
    }

    public CharMatrix mirrorHorizontal()
    {
        char[][] newContent = new char[getHeight()][getWidth()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                newContent[y][x] = content[y][getWidth() - x - 1];
            }
        }
        return new CharMatrix(newContent, fill);
    }

    public CharMatrix mirrorVertical()
    {
        char[][] newContent = new char[getHeight()][getWidth()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                newContent[y][x] = content[getHeight() - y - 1][x];
            }
        }
        return new CharMatrix(newContent, fill);
    }

    @Override
    public String toString()
    {
        return IntStream.range(0, content.length)
                .mapToObj(this::getRow)
                .collect(Collectors.joining("\n"));
    }
}
