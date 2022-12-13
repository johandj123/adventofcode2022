import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day13 {
    List<Pair> pairs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day13().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void second() {
        List<Object> all = new ArrayList<>();
        for (Pair pair : pairs) {
            all.add(pair.a);
            all.add(pair.b);
        }
        List<List<Integer>> divider1 = Collections.singletonList(Collections.singletonList(2));
        List<List<Integer>> divider2 = Collections.singletonList(Collections.singletonList(6));
        all.add(divider1);
        all.add(divider2);
        all.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Day13.this.compare(o1, o2);
            }
        });
        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < all.size(); i++) {
            Object o = all.get(i);
            if (o.equals(divider1)) {
                index1 = i + 1;
            } else if (o.equals(divider2)) {
                index2 = i + 1;
            }
        }
        int result = index1 * index2;
        System.out.println(result);
    }

    private void first() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            if (compare(pair.a, pair.b) < 0) {
                indexes.add(i + 1);
            }
        }
        System.out.println(indexes.stream().mapToLong(x -> x).sum());
    }

    private void readInput() throws IOException {
        String input = Files.readString(new File("input13.txt").toPath()).trim();
        String[] sl = input.split("\n\n");
        for (String s : sl) {
            String[] split = s.split("\n");
            Pair pair = new Pair();
            pair.a = parse(split[0]);
            pair.b = parse(split[1]);
            pairs.add(pair);
        }
    }

    private int compare(Object a,Object b)
    {
        if (a instanceof Integer && b instanceof Integer) {
            Integer aa = (Integer) a;
            Integer bb = (Integer) b;
            return aa.compareTo(bb);
        } else if (a instanceof List && b instanceof List) {
            List<Integer> aa = (List<Integer>) a;
            List<Integer> bb = (List<Integer>) b;
            for (int i = 0; i < Math.max(aa.size(), bb.size()); i++) {
                if (i >= aa.size()) {
                    return -1;
                } else if (i >= bb.size()) {
                    return 1;
                }
                int sc = compare(aa.get(i), bb.get(i));
                if (sc != 0) {
                    return sc;
                }
            }
            return 0;
        } else if (a instanceof Integer && b instanceof List) {
            return compare(Collections.singletonList(a), b);
        } else if (a instanceof List && b instanceof Integer) {
            return compare(a, Collections.singletonList(b));
        }
        throw new IllegalStateException("Invalid comparison");
    }

    private Object parse(String input) {
        Reader reader = new Reader(input);
        Object result = parse(reader);
        if (!reader.isAtEnd()) {
            throw new IllegalArgumentException("Unexpected character at end " + reader.current());
        }
        return result;
    }

    private Object parse(Reader reader) {
        if (reader.current() == '[') {
            reader.next();
            List<Object> result = new ArrayList<>();
            while (reader.current() != ']') {
                result.add(parse(reader));
                if (reader.current() == ',') {
                    reader.next();
                }
            }
            reader.next();
            return result;
        }
        if (reader.current() >= '0' && reader.current() <= '9') {
            StringBuilder sb = new StringBuilder();
            while (reader.current() >= '0' && reader.current() <= '9') {
                sb.append(reader.current());
                reader.next();
            }
            return Integer.parseInt(sb.toString());
        }
        throw new IllegalArgumentException("Unknown character " + reader.current());
    }

    static class Pair {
        Object a;
        Object b;
    }

    static class Reader {
        String input;
        int position;

        public Reader(String input) {
            this.input = input;
        }

        public char current() {
            return input.charAt(position);
        }

        public void next() {
            position++;
        }

        public boolean isAtEnd() {
            return position >= input.length();
        }
    }
}
