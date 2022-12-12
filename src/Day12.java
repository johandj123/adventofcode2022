import lib.BFS;
import lib.BFSNode;
import lib.CharMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day12 {
    private CharMatrix m;
    private CharMatrix.Position start;
    private CharMatrix.Position end;

    public static void main(String[] args) throws IOException {
        new Day12().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input12.txt").toPath());
        m = new CharMatrix(lines);
        start = m.find('S').get();
        start.set('a');
        end = m.find('E').get();
        end.set('z');
    }

    private void first() {
        BFS<FirstNode> bfs = new BFS<>(new FirstNode(start));
        int distance = bfs.shortestDistance(node -> node.position.equals(end));
        System.out.println(distance);
    }

    private void second() {
        BFS<SecondNode> bfs = new BFS<>(new SecondNode(end));
        int distance = bfs.shortestDistance(node -> node.position.get() == 'a');
        System.out.println(distance);
    }

    static class FirstNode implements BFSNode<FirstNode>
    {
        private final CharMatrix.Position position;

        public FirstNode(CharMatrix.Position position) {
            this.position = position;
        }

        @Override
        public List<FirstNode> getNeighbours() {
            List<FirstNode> result = new ArrayList<>();
            char c1 = position.get();
            for (CharMatrix.Position next : position.getNeighbours()) {
                char c2 = next.get();
                if (c2 != ' ' && c2 <= c1 + 1) {
                    result.add(new FirstNode(next));
                }
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FirstNode firstNode = (FirstNode) o;
            return position.equals(firstNode.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }

    static class SecondNode implements BFSNode<SecondNode>
    {
        private final CharMatrix.Position position;

        public SecondNode(CharMatrix.Position position) {
            this.position = position;
        }

        @Override
        public List<SecondNode> getNeighbours() {
            List<SecondNode> result = new ArrayList<>();
            char c1 = position.get();
            for (CharMatrix.Position next : position.getNeighbours()) {
                char c2 = next.get();
                if (c2 != ' ' && c2 >= c1 - 1) {
                    result.add(new SecondNode(next));
                }
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SecondNode that = (SecondNode) o;
            return position.equals(that.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }
}
