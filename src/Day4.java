import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day4 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input4.txt").toPath());
        int first = 0;
        int second = 0;
        for (String line : lines) {
            String[] parts = line.split(",");
            Interval one = new Interval(parts[0]);
            Interval two = new Interval(parts[1]);
            if (fullOverlap(one, two)) {
                first++;
            }
            if (anyOverlap(one, two)) {
                second++;
            }
        }
        System.out.println(first);
        System.out.println(second);
    }

    private static boolean fullOverlap(Interval one,Interval two)
    {
        return fullWithin(one, two) || fullWithin(two, one);
    }

    private static boolean fullWithin(Interval one, Interval two)
    {
        return (one.a >= two.a && one.b <= two.b);
    }

    private static boolean anyOverlap(Interval one,Interval two)
    {
        return (within(one.a, two) || within(one.b, two) ||
                within(two.a, one) || within(two.b, one));
    }

    private static boolean within(int x, Interval i)
    {
        return (x >= i.a && x <= i.b);
    }

    static class Interval
    {
        int a;
        int b;

        Interval(String s)
        {
            String[] parts = s.split("-");
            a = Integer.parseInt(parts[0]);
            b = Integer.parseInt(parts[1]);
        }
    }
}
