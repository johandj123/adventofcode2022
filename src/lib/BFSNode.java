package lib;

import java.util.List;

public interface BFSNode<T extends BFSNode<T>> {
    List<T> getNeighbours();
}
