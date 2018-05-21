import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTree<T extends Comparable> implements Collection<T> {
    public static final int RED = 0;
    public static final int BLACK = 1;

    int size;
    boolean isEmpty;
    Node root;

    public RedBlackTree() {
        size = 0;
        isEmpty = true;
        root = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new RBIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] objs = new Object[size];
        Iterator it = iterator();
        for (int i = 0; i < size; i++) {
            objs[i] = it.next();
        }
        return objs;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        if (root == null) {
            root = new Node(t, BLACK);
            size++;
            isEmpty = false;
            return true;
        }
        return tryInsert(t, root);
    }

    private boolean tryInsert(@NotNull T t, Node node) {
        if (t.equals(node.value)) return false;
        if (t.compareTo(node.value) > 0) {
            if (node.hasRightChild()) {
                return tryInsert(t, node.rightChild);
            } else {
                node.rightChild = new Node(t, node);
                proceedFlip(node.rightChild);
                size++;
                return true;
            }

        } else {
            if (node.hasLeftChild()) {
                return tryInsert(t, node.leftChild);
            } else {
                node.leftChild = new Node(t, node);
                proceedFlip(node.leftChild);
                size++;
                return true;
            }
        }
    }

    private void proceedFlip(Node node) {
        //todo
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object obj :
                c) {
            if (this.contains(obj)) {
                result = true;
                remove(obj);
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    class Node {
        Node parent;
        Node leftChild;
        Node rightChild;
        int color;

        T value;

        Node(T value) {
            this.value = value;
            parent = null;
            leftChild = null;
            rightChild = null;
            color = RED;
        }

        Node(T value, int color) {
            this.value = value;
            this.color = color;
        }

        Node(T value, Node parent) {
            this.value = value;
            this.parent = parent;
            leftChild = null;
            rightChild = null;
        }

        Node(T value, Node parent, Node leftChild, Node rightChild, int color) {
            this.value = value;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.color = color;
        }

        boolean hasRightChild() {
            return this.rightChild != null;
        }

        boolean hasLeftChild() {
            return this.leftChild != null;
        }

        boolean hasParent() {
            return this.parent != null;
        }

        boolean isRightChild() {
            return hasParent() && parent.rightChild == this;
        }
    }

    class RBIterator implements Iterator<T> {
        boolean initFlag;
        Node node;

        RBIterator() {
            node = findLeftborder(root);
            initFlag = true;
        }

        private Node findLeftborder(Node node) {
            Node temp = node;

            while (temp.hasLeftChild()) temp = temp.leftChild;
            return temp;
        }

        @Override
        public boolean hasNext() {
            if (!node.isRightChild()) return true;
            else if (node.hasRightChild()) return true;
            else {
                Node temp = node;
                while (temp.isRightChild()) temp = temp.parent;
                if (temp.hasParent()) return true;
            }
            return false;
        }

        @Override
        public T next() {
            if (initFlag) {
                initFlag = false;
                return node.value;
            }
            if (node.hasRightChild()) {
                node = node.rightChild;
                while (node.hasLeftChild()) node = node.leftChild;
                return node.value;
            } else {
                Node temp = node;
                while (temp.isRightChild()) temp = temp.parent;
                if (!temp.hasParent()) throw new NoSuchElementException();
                node = temp.parent;
                return node.value;
            }
        }
    }
}
