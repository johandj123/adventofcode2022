import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {
    public static void main(String[] args) {
        play(true);
        play(false);
    }

    private static void play(boolean first) {
        List<Monkey> monkeys = createMonkeys();
        int limit = first ? 20 : 10000;
        for (int i = 0; i < limit; i++) {
            round(monkeys, first);
        }
        List<Integer> activityList = monkeys.stream()
                .map(monkey -> monkey.activity)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println(((long)activityList.get(0)) * ((long)activityList.get(1)));
    }

    private static void round(List<Monkey> monkeys, boolean first) {
        for (Monkey monkey : monkeys) {
            monkey.round(monkeys, first);
        }
    }

    private static List<Monkey> createMonkeys() {
        List<Monkey> monkeys = new ArrayList<>();

        monkeys.add(new Monkey(List.of(59, 65, 86, 56, 74, 57, 56), old -> old.multiply(BigInteger.valueOf(17)), 3, 3, 6));
        monkeys.add(new Monkey(List.of(63, 83, 50, 63, 56), old -> old.add(BigInteger.valueOf(2)), 13, 3, 0));
        monkeys.add(new Monkey(List.of(93, 79, 74, 55), old -> old.add(BigInteger.valueOf(1)), 2, 0, 1));
        monkeys.add(new Monkey(List.of(86, 61, 67, 88, 94, 69, 56, 91), old -> old.add(BigInteger.valueOf(7)), 11, 6, 7));
        monkeys.add(new Monkey(List.of(76, 50, 51), old -> old.multiply(old), 19, 2, 5));
        monkeys.add(new Monkey(List.of(77, 76), old -> old.add(BigInteger.valueOf(8)), 17, 2, 1));
        monkeys.add(new Monkey(List.of(74), old -> old.multiply(BigInteger.valueOf(2)), 5, 4, 7));
        monkeys.add(new Monkey(List.of(86, 85, 52, 86, 91, 95), old -> old.add(BigInteger.valueOf(6)), 7, 4, 5));

        return monkeys;
    }

    static class Monkey {
        Deque<BigInteger> items;
        Function<BigInteger, BigInteger> operation;
        int testDivisor;
        int trueMonkey;
        int falseMonkey;
        int activity = 0;

        public Monkey(List<Integer> items, Function<BigInteger, BigInteger> operation, int testDivisor, int trueMonkey, int falseMonkey) {
            this.items = items.stream().map(BigInteger::valueOf).collect(Collectors.toCollection(ArrayDeque::new));
            this.operation = operation;
            this.testDivisor = testDivisor;
            this.trueMonkey = trueMonkey;
            this.falseMonkey = falseMonkey;
        }

        public void round(List<Monkey> monkeys,boolean first) {
            while (!items.isEmpty()) {
                activity++;
                BigInteger item = items.removeFirst();
                item = operation.apply(item);
                if (first) {
                    item = item.divide(BigInteger.valueOf(3));
                } else {
                    item = item.mod(BigInteger.valueOf(329789460));
                }
                if (BigInteger.ZERO.equals(item.mod(BigInteger.valueOf(testDivisor)))) {
                    monkeys.get(trueMonkey).items.addLast(item);
                } else {
                    monkeys.get(falseMonkey).items.addLast(item);
                }
            }
        }
    }
}
