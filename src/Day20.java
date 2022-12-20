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
    }

    private void first() {
        LinkedList<Container> containers = input.stream()
                .map(Container::new)
                .collect(Collectors.toCollection(LinkedList::new));
        mix(containers);
        int index = containers.indexOf(containers.stream().filter(c -> c.value == 0).findFirst().orElseThrow());
        long v1 = containers.get((index + 1000) % containers.size()).value;
        long v2 = containers.get((index + 2000) % containers.size()).value;
        long v3 = containers.get((index + 3000) % containers.size()).value;
        System.out.println(v1 + v2 + v3);
    }

    private void mix(LinkedList<Container> containers) {
        List<Container> orginialOrder = new ArrayList<>(containers);
        for (Container current : orginialOrder) {
//            System.out.println(current);
            mix(containers, current);
//            System.out.println(containers);
        }
    }

    private void mix(LinkedList<Container> containers, Container current) {
        if (current.value == 0) return;
        int index = containers.indexOf(current);
        int newIndex1 = index + current.value;
        int newIndex = Math.floorMod(newIndex1, containers.size() - 1);
        containers.remove(index);
        containers.add(newIndex, current);
    }

    static class Container
    {
        final int value;

        public Container(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }
}
