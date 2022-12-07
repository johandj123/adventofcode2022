import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input7.txt").toPath());
        Dir root = new Dir();
        Dir current = root;
        for (String line : lines) {
            if (line.startsWith("$")) {
                if (line.startsWith("$ cd")) {
                    String name = line.substring(5);
                    if (!"/".equals(name)) {
                        if ("..".equals(name)) {
                            current = current.parent;
                        } else {
                            current = current.addDir(name);
                        }
                    }
                }
            } else if (line.charAt(0) >= '0' && line.charAt(0) <= '9') {
                String[] parts = line.split(" ");
                long size = Long.parseLong(parts[0]);
                String name = parts[1];
                current.addFile(size, name);
            }
        }
        List<Dir> dirs = new ArrayList<>();
        root.collect(dirs);
        long first = dirs.stream()
                .mapToLong(Dir::size)
                .filter(s -> s <= 100000)
                .sum();
        System.out.println(first);
        long free = 70000000 - root.size();
        long needed = 30000000 - free;
        long second = dirs.stream()
                .mapToLong(Dir::size)
                .filter(s -> s >= needed)
                .min().orElse(0L);
        System.out.println(second);
    }

    static class Dir {
        private long fileSize = 0;
        private Map<String, Dir> subdirs = new HashMap<>();
        private Dir parent;

        public void addFile(long size,String name)
        {
            fileSize += size;
        }

        public Dir addDir(String name)
        {
            Dir dir = new Dir();
            subdirs.put(name, dir);
            dir.parent = this;
            return dir;
        }

        public long size()
        {
            return fileSize + subdirs.values().stream().mapToLong(Dir::size).sum();
        }

        public void collect(List<Dir> result)
        {
            result.add(this);
            subdirs.values().forEach(dir -> dir.collect(result));
        }
    }
}
