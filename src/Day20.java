import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {
    List<Integer> input;

    public static void main(String[] args) throws IOException {
        new Day20().start();
    }

    private void start() throws IOException {
        input = Files.readAllLines(new File("input20.txt").toPath()).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        first();
        second();
    }

    private void first() {
        LinkedList<Container> containers = input.stream()
                .map(Container::new)
                .collect(Collectors.toCollection(LinkedList::new));
        mix(containers, 1);
        int index = containers.indexOf(containers.stream().filter(c -> c.value == 0).findFirst().orElseThrow());
        long v1 = containers.get((index + 1000) % containers.size()).value;
        long v2 = containers.get((index + 2000) % containers.size()).value;
        long v3 = containers.get((index + 3000) % containers.size()).value;
        System.out.println(v1 + v2 + v3);
    }

    private void second() {
        LinkedList<Container> containers = input.stream()
                .map(value -> new Container(value * 811589153L))
                .collect(Collectors.toCollection(LinkedList::new));
        mix(containers, 10);
        int index = containers.indexOf(containers.stream().filter(c -> c.value == 0).findFirst().orElseThrow());
        long v1 = containers.get((index + 1000) % containers.size()).value;
        long v2 = containers.get((index + 2000) % containers.size()).value;
        long v3 = containers.get((index + 3000) % containers.size()).value;
        System.out.println(v1 + v2 + v3);
    }

    private void mix(LinkedList<Container> containers, int count) {
        List<Container> orginialOrder = new ArrayList<>(containers);
        for (int i = 0; i < count; i++) {
            for (Container current : orginialOrder) {
                mix(containers, current);
            }
        }
    }

    private void mix(LinkedList<Container> containers, Container current) {
        if (current.value == 0) return;
        int index = containers.indexOf(current);
        int newIndex = Math.floorMod(index + current.value, containers.size() - 1);
        containers.remove(index);
        containers.add(newIndex, current);
    }

    static class Container
    {
        final long value;

        public Container(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }
}
