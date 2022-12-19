import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {
    List<Blueprint> blueprintList;

    public static void main(String[] args) throws IOException {
        new Day19().start();
    }

    private void start() throws IOException {
        blueprintList = Files.readAllLines(new File("input19.txt").toPath()).stream()
                .map(Blueprint::new)
                .collect(Collectors.toList());
        first();
        second();
    }

    private void first() {
        long first = 0;
        for (int i = 0; i < blueprintList.size(); i++) {
            Blueprint blueprint = blueprintList.get(i);
            long quality = blueprint.maxGeodes(24);
            System.out.println(i + ": " + quality);
            first += ((i + 1) * quality);
        }
        System.out.println("First: " + first);
    }

    private void second() {
        long second = 1;
        for (int i = 0; i < 3; i++) {
            Blueprint blueprint = blueprintList.get(i);
            long quality = blueprint.maxGeodes(32);
            System.out.println(i + ": " + quality);
            second *= quality;
        }
        System.out.println("Second: " + second);
    }

    static class Blueprint {
        final Map<String, Amount> costs = new HashMap<>();

        public Blueprint(String line) {
            line = line.substring(line.indexOf(':') + 2);
            String[] split = line.split("\\.");
            for (String s : split) {
                String[] sp = s.trim().split(" ");
                String name = sp[1];
                Amount cost = new Amount();
                for (int i = 4; i < sp.length; i += 3) {
                    int amount = Integer.parseInt(sp[i]);
                    cost.set(sp[i + 1], amount);
                }
                costs.put(name, cost);
            }
            System.out.println();
        }

        public int maxGeodes(int rounds) {
            Set<State> current = new HashSet<>();
            current.add(new State());
            for (int i = 0; i < rounds - 1; i++) {
                Set<State> next = new HashSet<>();
                for (State state : current) {
                    next.addAll(state.getNextStates());
                }
                current = next;
                if (i >= 28 && i < rounds - 2) {
                    current = pruneWorstHalf(current);
                }
                System.out.println(i + " -> " + current.size());
            }
            int maxGeode = 0;
            for (State currentState : current) {
                List<State> next = currentState.getNextStates();
                int max = next.stream().mapToInt(State::getGeodeCount).max().orElse(0);
                maxGeode = Math.max(maxGeode, max);
            }
            return maxGeode;
        }

        private Set<State> pruneWorstHalf(Set<State> current) {
            List<State> result = new ArrayList<>(current);
            result.sort(Comparator.comparing(State::getGeodeCount).reversed());
            return new HashSet<>(result.subList(0, result.size() / 2));
        }

        class State {
            final Amount collected;
            final Amount robots;
            final int didNotBuyMask;

            State() {
                collected = new Amount();
                robots = new Amount();
                robots.set("ore", 1);
                didNotBuyMask = 0;
            }

            public State(Amount collected, Amount robots, int didNotBuyMask) {
                this.collected = collected;
                this.robots = robots;
                this.didNotBuyMask = didNotBuyMask;
            }

            List<State> getNextStates() {
                List<State> result = new ArrayList<>();
                int canPayMask = didNotBuyMask;
                for (Map.Entry<String, Amount> entry : costs.entrySet()) {
                    int keyIndex = Amount.MAP.get(entry.getKey());
                    int keyMask = 1 << keyIndex;
                    if (collected.canPay(entry.getValue()) && (didNotBuyMask & keyMask) == 0) {
                        canPayMask |= keyMask;
                        Amount nextRobots = new Amount(robots);
                        nextRobots.amounts[keyIndex]++;
                        Amount collectedAfterPaying = new Amount(collected);
                        collectedAfterPaying.pay(entry.getValue());

                        Amount nextCollected = collectResources(collectedAfterPaying);

                        result.add(new State(nextCollected, nextRobots, 0));
                    }
                }

                if (canPayMask != 0xf) {
                    Amount nextCollected = collectResources(collected);
                    result.add(new State(nextCollected, robots, canPayMask));
                }

                return result;
            }

            private Amount collectResources(Amount collected1) {
                Amount result = new Amount(collected1);
                result.add(robots);
                return result;
            }

            public int getGeodeCount()
            {
                return collected.get("geode");
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                State state = (State) o;
                return collected.equals(state.collected) && robots.equals(state.robots);
            }

            @Override
            public int hashCode() {
                return Objects.hash(collected, robots);
            }

            @Override
            public String toString() {
                return "State{" +
                        "collected=" + collected +
                        ", robots=" + robots +
                        '}';
            }
        }
    }

    static class Amount {
        static final Map<String, Integer> MAP = Map.of("ore", 0, "clay", 1, "obsidian", 2, "geode", 3);

        final int[] amounts = new int[4];

        public Amount() {
        }

        public Amount(Amount amount) {
            System.arraycopy(amount.amounts, 0, amounts, 0, amounts.length);
        }

        public int get(String name) {
            return amounts[MAP.get(name)];
        }

        public void set(String name, int value) {
            amounts[MAP.get(name)] = value;
        }

        public boolean canPay(Amount costs) {
            for (int i = 0; i < 4; i++) {
                if (amounts[i] < costs.amounts[i]) {
                    return false;
                }
            }
            return true;
        }

        public void pay(Amount costs) {
            for (int i = 0; i < 4; i++) {
                amounts[i] -= costs.amounts[i];
            }
        }

        public void add(Amount costs) {
            for (int i = 0; i < 4; i++) {
                amounts[i] += costs.amounts[i];
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Amount amount = (Amount) o;
            return Arrays.equals(amounts, amount.amounts);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(amounts);
        }

        public boolean less(Amount o) {
            for (int i = 0; i < 4; i++) {
                if (amounts[i] >= o.amounts[i]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return MAP.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + amounts[entry.getValue()])
                    .collect(Collectors.joining(","));
        }
    }
}
