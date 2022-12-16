import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
    private Map<String, Valve> valves;

    public static void main(String[] args) throws IOException {
        new Day16().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void readInput() throws IOException {
        valves = Files.readAllLines(new File("input16.txt").toPath())
                .stream()
                .map(Valve::new)
                .collect(Collectors.toMap(v -> v.name, v -> v));
    }

    private void first() {
        Set<Node> current = new HashSet<>();
        current.add(new Node(valves.get("AA"), new HashSet<>(), 0));
        for (int i = 0; i < 30; i++) {
            current = firstEvolve(current);
            current = firstPrune(current);
            System.out.println(i + " : " + current.size());
        }
        int first = current.stream()
                .mapToInt(node -> node.pressure)
                .max()
                .orElseThrow();
        System.out.println(first);
    }

    private Set<Node> firstEvolve(Set<Node> current) {
        return current.stream()
                .flatMap(node -> node.evolve().stream())
                .collect(Collectors.toSet());
    }

    private Set<Node> firstPrune(Set<Node> current) {
        Map<Valve, List<Node>> groups = current.stream().
                collect(Collectors.groupingBy(node -> node.pos));
        Set<Node> result = new HashSet<>();
        for (Map.Entry<Valve, List<Node>> entry : groups.entrySet()) {
            result.addAll(firstPrune(entry.getValue()));
        }
        return result;
    }

    private List<Node> firstPrune(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            boolean betterFound = false;
            for (Node otherNode : nodes) {
                if (node != otherNode &&
                        otherNode.pressure > node.pressure &&
                        otherNode.totalFlow > node.totalFlow) {
                    betterFound = true;
                }
            }
            if (!betterFound) {
                result.add(node);
            }
        }
        return result;
    }

    private void second() {
        Set<Node2> current = new HashSet<>();
        Valve aa = valves.get("AA");
        current.add(new Node2(aa, aa, new HashSet<>(), 0));
        for (int i = 0; i < 26; i++) {
            current = secondEvolve(current);
            if (i != 25) {
                current = secondPrune(current);
            }
            System.out.println(i + " : " + current.size());
        }
        int second = current.stream()
                .mapToInt(node -> node.pressure)
                .max()
                .orElseThrow();
        System.out.println(second);
    }

    private Set<Node2> secondEvolve(Set<Node2> current) {
        return current.stream()
                .flatMap(node -> node.evolve().stream())
                .collect(Collectors.toSet());
    }

    private Set<Node2> secondPrune(Set<Node2> current) {
        Map<Valve2, List<Node2>> groups = current.stream().
                collect(Collectors.groupingBy(node -> new Valve2(node.pos, node.pos2)));
        Set<Node2> result = new HashSet<>();
        for (Map.Entry<Valve2, List<Node2>> entry : groups.entrySet()) {
            result.addAll(secondPrune(entry.getValue()));
        }
        return result;
    }

    private List<Node2> secondPrune(List<Node2> nodes) {
        List<Node2> result = new ArrayList<>();
        for (Node2 node : nodes) {
            boolean betterFound = false;
            for (Node2 otherNode : nodes) {
                if (node != otherNode &&
                        otherNode.pressure > node.pressure &&
                        otherNode.totalFlow > node.totalFlow) {
                    betterFound = true;
                }
            }
            if (!betterFound) {
                result.add(node);
            }
        }
        return result;
    }

    class Node {
        Valve pos;
        Set<Valve> open;
        int pressure;
        int totalFlow;

        public Node(Valve pos, Set<Valve> open, int pressure) {
            this.pos = pos;
            this.open = open;
            this.pressure = pressure;
            totalFlow = open.stream()
                    .mapToInt(valve -> valve.flow)
                    .sum();
        }

        public List<Node> evolve() {
            int nextPressure = pressure + totalFlow;
            List<Node> result = new ArrayList<>();
            for (String next : pos.next) {
                Valve nextValve = valves.get(next);
                result.add(new Node(nextValve, open, nextPressure));
            }
            if (!open.contains(pos)) {
                Set<Valve> nextOpen = new HashSet<>(open);
                nextOpen.add(pos);
                result.add(new Node(pos, nextOpen, nextPressure));
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return pressure == node.pressure && pos.equals(node.pos) && open.equals(node.open);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, open, pressure);
        }
    }

    class Node2 {
        Valve pos;
        Valve pos2;
        Set<Valve> open;
        int pressure;
        int totalFlow;

        public Node2(Valve pos, Valve pos2, Set<Valve> open, int pressure) {
            this.pos = pos;
            this.pos2 = pos2;
            this.open = open;
            this.pressure = pressure;
            totalFlow = open.stream()
                    .mapToInt(valve -> valve.flow)
                    .sum();
            if (pos.name.compareTo(pos2.name) > 0) {
                Valve h = this.pos;
                this.pos = this.pos2;
                this.pos2 = h;
            }
        }

        public List<Node2> evolve() {
            int nextPressure = pressure + totalFlow;
            List<Valve> action1 = actions(pos);
            List<Valve> action2 = actions(pos2);

            List<Node2> result = new ArrayList<>();
            for (Valve a1 : action1) {
                for (Valve a2: action2) {
                    if (a1 == null && a2 == null && pos == pos2) {
                        continue;
                    }
                    Set<Valve> nextOpen = open;
                    if (a1 == null || a2 == null) {
                        nextOpen = new HashSet<>(open);
                        if (a1 == null) {
                            nextOpen.add(pos);
                        }
                        if (a2 == null) {
                            nextOpen.add(pos2);
                        }
                    }
                    result.add(new Node2(
                            a1 == null ? pos : a1,
                            a2 == null ? pos2 : a2,
                            nextOpen,
                            nextPressure
                    ));
                }
            }
            return result;
        }

        private List<Valve> actions(Valve apos) {
            List<Valve> result = new ArrayList<>();
            for (String next : apos.next) {
                result.add(valves.get(next));
            }
            if (!open.contains(apos)) {
                result.add(null);
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node2 node2 = (Node2) o;
            return pressure == node2.pressure && pos.equals(node2.pos) && pos2.equals(node2.pos2) && open.equals(node2.open);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, pos2, open, pressure);
        }
    }

    static class Valve2 {
        final Valve pos;
        final Valve pos2;

        public Valve2(Valve pos, Valve pos2) {
            this.pos = pos;
            this.pos2 = pos2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Valve2 valve2 = (Valve2) o;
            return pos.equals(valve2.pos) && pos2.equals(valve2.pos2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, pos2);
        }
    }

    static class Valve {
        String name;
        int flow;
        Set<String> next;

        public Valve(String input) {
            String[] split = input.split(" ");
            name = split[1];
            flow = Integer.parseInt(split[4].substring(5, split[4].length() - 1));
            next = new HashSet<>();
            for (int i = 9; i < split.length; i++) {
                next.add(split[i].replace(",", ""));
            }
        }
    }
}
