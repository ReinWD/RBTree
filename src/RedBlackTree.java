import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class RedBlackTree<T extends Comparable> implements Collection<T>, Serializable, Cloneable {
    public static final int RED = 0;
    public static final int BLACK = 1;

    private int size;
    private boolean isEmpty;

    private transient Node<T> root;

    public Node<T> getRoot() {
        return root;
    }

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
        return root != null && o != null && o instanceof Comparable && tryFind((Comparable) o, root) != null;
    }

    private Node<T> tryFind(@NotNull Comparable<T> o, Node<T> node) {
        int comp = o.compareTo(node.value);
        if (comp == 0) return node;
        if (comp < 0) {
            return node.leftChild == null ? null : tryFind(o, node.leftChild);
        } else
            return node.leftChild == null ? null : tryFind(o, node.rightChild);
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
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size)
            a = (E[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        Object[] result = a;
        Iterator<T> iterator = this.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(@NotNull T t) {
        //type 1
        if (root == null) {
            root = new Node<>(t, BLACK);
            size++;
            isEmpty = false;
            return true;
        }
        //other type see proceedFlip
        return tryInsert(t, root);
    }

    @SuppressWarnings("unchecked")
    private boolean tryInsert(@NotNull T t, Node node) {
        boolean eq = node.rightChild == node;
        if (t.equals(node.value)) return false;
        if (t.compareTo(node.value) > 0) {
            if (node.hasRightChild()) {
                return tryInsert(t, node.rightChild);
            } else {
                node.rightChild = new Node<>(t, node);
                proceedFlip(node.rightChild);
                size++;
                return true;
            }
        } else {
            if (node.hasLeftChild()) {
                return tryInsert(t, node.leftChild);
            } else {
                node.leftChild = new Node<>(t, node);
                proceedFlip(node.leftChild);
                size++;
                return true;
            }
        }
    }

    private void proceedFlip(final Node<T> node) {
        Node<T> parent = node.parent;
        //type 1
        if (parent == null) {
            node.color = BLACK;
            root = node;
            return;
        }
        //type 2
        if (parent.color == BLACK) return;

        if (parent.color == RED) {
            Node<T> grandParent = parent.parent;
            if (parent.isRightChild()) {
                //type 3
                if (grandParent.hasLeftChild()) {
                    if (grandParent.leftChild.color == RED) {
                        parent.color = BLACK;
                        grandParent.color = RED;
                        grandParent.leftChild.color = BLACK;
                        proceedFlip(grandParent);
                    } else {
                        if (node.isRightChild()) {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            grandParent.leftChild.color = BLACK;
                        } else {
                            node.color = BLACK;
                            grandParent.color = RED;
                            parent.color = RED;
                        }
                        spinLeft(grandParent, parent, node);
                    }
                } else {
                    //type 4
                    if (node.isRightChild()) {
                        parent.color = BLACK;
                        grandParent.color = RED;
                    } else {
                        node.color = BLACK;
                        grandParent.color = RED;
                    }
                    spinLeft(grandParent, parent, node);
                }
            } else {
                //type 3
                if (grandParent.hasRightChild()) {
                    if (grandParent.rightChild.color == RED) {
                        parent.color = BLACK;
                        grandParent.color = RED;
                        grandParent.rightChild.color = BLACK;
                        proceedFlip(grandParent);
                    } else {
                        if (node.isRightChild()) {
                            node.color = BLACK;
                            grandParent.color = RED;
                            parent.color = RED;
                        } else {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            node.color = RED;
                        }
                        spinRight(grandParent, parent, node);
                    }
                } else {
                    //type 4
                    if (node.isRightChild()) {
                        node.color = BLACK;
                        grandParent.color = RED;
                    } else {
                        parent.color = BLACK;
                        grandParent.color = RED;
                    }
                    spinRight(grandParent, parent, node);
                }
            }
        }
    }

    private void spinRight(final Node<T> grandParent, final Node<T> parent, final Node<T> node) {
        boolean gpIsRight = grandParent.isRightChild();
        boolean nodeIsRight = node.isRightChild();

        if (nodeIsRight) {
            node.parent = grandParent.parent;
            if (node.parent != null) {
                if (gpIsRight) node.parent.rightChild = node;
                else node.parent.leftChild = node;
            } else root = node;

            if (node.rightChild != null) {
                node.rightChild.parent = grandParent;
            }
            grandParent.leftChild = node.rightChild;
            node.rightChild = grandParent;
            grandParent.parent = node;

            if (node.leftChild != null) {
                node.leftChild.parent = parent;
            }
            parent.rightChild = node.leftChild;
            node.leftChild = parent;
            parent.parent = node;
        } else {
            parent.parent = grandParent.parent;
            if (parent.parent != null) {
                if (gpIsRight) parent.parent.rightChild = parent;
                else parent.parent.leftChild = parent;
            } else root = parent;

            if (parent.rightChild != null)
                parent.rightChild.parent = grandParent;
            grandParent.leftChild = parent.rightChild;
            parent.rightChild = grandParent;
            grandParent.parent = parent;
        }
    }

    private void spinLeft(final Node<T> grandParent, final Node<T> parent, final Node<T> node) {
        boolean gpIsRight = grandParent.isRightChild();
        boolean nodeIsRight = node.isRightChild();

        if (!nodeIsRight) {
            node.parent = grandParent.parent;
            if (node.parent != null) {
                if (gpIsRight) node.parent.rightChild = node;
                else node.parent.leftChild = node;
            } else root = node;

            if (node.leftChild != null) {
                node.leftChild.parent = grandParent;
            }
            grandParent.rightChild = node.leftChild;
            node.leftChild = grandParent;
            grandParent.parent = node;

            if (node.rightChild != null) {
                node.rightChild.parent = parent;
            }
            parent.leftChild = node.rightChild;
            node.rightChild = parent;
            parent.parent = node;
        } else {
            parent.parent = grandParent.parent;
            if (parent.parent != null) {
                if (gpIsRight) parent.parent.rightChild = parent;
                else parent.parent.leftChild = parent;
            } else root = parent;

            if (parent.leftChild != null)
                parent.leftChild.parent = grandParent;
            grandParent.rightChild = parent.leftChild;
            parent.leftChild = grandParent;
            grandParent.parent = parent;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        if (o instanceof Comparable) {
            final Node<T> node = tryFind(((Comparable<T>) o), root);
            if (node == null) return false;
            else {//node != null
                final Node<T> parent = node.parent;
                if (node.color == RED){
                    if (!node.hasLeftChild()&&!node.hasRightChild()){
                        node.changeParent(null);
                        return true;
                    }
                    if (node.hasLeftChild()^node.hasRightChild()){
                        Node<T> child = node.hasLeftChild()? node.leftChild : node.rightChild;
                        node.changeParent(null);
                        child.changeParent(parent);
                        return true;
                    } else {
                        Node<T> replacer = node.rightChild.findLeftBorder();
                        if (replacer.color==RED){
                            replacer.changeParent(parent);
                            return true;
                        }
                        else{
                            Node<T> repParent = replacer.parent;
                            if (replacer.hasRightChild()){
                                replacer.rightChild.color = BLACK;
                                replacer.rightChild.changeParent(repParent);
                            }else {
                                Node<T> brother = repParent.rightChild;
                                if (repParent.color == RED){
                                    if (!brother.hasLeftChild()&&!brother.hasRightChild()){
                                        brother.color = RED;
                                        repParent.color = BLACK;
                                    }else {
                                        if (brother.hasLeftChild())spinLeft(repParent,brother, brother.leftChild);
                                        else spinLeft(repParent,brother,brother.rightChild);
                                    }
                                }
                                if (brother.color==BLACK){
                                    if (!brother.hasLeftChild()&&!brother.hasRightChild()){
                                        //todo
                                    }
                                }else {
                                    //todo
                                }
                            }
                        }
                        replacer.changeParent(parent);
                    }
                }else {
                    //节点无子
                    if (!node.hasLeftChild()&&!node.hasRightChild()){
                        Node<T> brother = null;
                        Node<T> border = null;
                        if (node.isRightChild()) {
                            brother = parent.leftChild;
                            border = parent.leftChild.findRightBorder();
                        }else {
                            brother = parent.rightChild;
                            border = brother.findLeftBorder();
                        }
                        node.changeParent(null);
                        border.changeParent(parent.parent);
                        brother.changeParent(border);
                        parent.changeParent(border);
                        if (border.color == RED) parent.color = BLACK;
                        return true;
                    }
                    //只有一个子元素
                    if (node.hasLeftChild()^node.hasRightChild()){
                        Node<T> child = node.hasLeftChild()? node.leftChild : node.rightChild;
                        node.changeParent(null);
                        child.changeParent(parent);
                        child.color = BLACK;
                    }else {
                        //黑节点有两个子元素
                        //todo
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj :
                c) {
            if (!contains(obj)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null || c.size() == 0) return false;
        boolean result = false;
        for (T o :
                c) {
            if (add(o)) result = true;
        }
        return result;
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
        Objects.requireNonNull(c);
        Iterator<T> it = iterator();
        boolean modified = false;
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    class Node<T extends Comparable> {
        Node<T> parent;
        Node<T> leftChild;
        Node<T> rightChild;
        int color;

        T value;

        Node(T value) {
            this(value, null, null, null, RED);
        }

        Node(T value, int color) {
            this(value, null, null, null, color);
        }

        Node(T value, Node<T> parent) {
            this(value, parent, null, null, RED);
        }

        Node(T value, Node<T> parent, Node<T> leftChild, Node<T> rightChild, int color) {
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

        void changeParent(Node<T> newParent){
            if (newParent == null) {
                if (this.hasParent())
                    if (this.isRightChild())this.parent.rightChild = null;
                    else this.parent.leftChild = null;
                this.parent = null;
                return;
            }
            final Node<T> oldParent = this.parent;
            int comp = this.value.compareTo(newParent.value);
            if (comp == 0) return;
            this.parent = newParent;
            if(this.hasParent())
                if (this.isRightChild())oldParent.rightChild = null;
                else oldParent.leftChild = null;
            if (comp > 0){
                if (newParent.rightChild!=null)throw new IllegalStateException("parent still have a right child");
                newParent.rightChild = this;
            }else {
                if (newParent.leftChild!=null)throw new IllegalStateException("parent still have a left child");
                newParent.leftChild = this;
            }
        }

        Node<T> findLeftBorder() {
            Node<T> temp = this;
            while (temp.hasLeftChild()) temp = temp.leftChild;
            return temp;
        }

        Node<T> findRightBorder(){
            Node<T> temp = this;
            while (temp.hasRightChild()) temp = temp.rightChild;
            return temp;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && this.value.equals(((Node) obj).value);
        }

        @Override
        public String toString() {
            String color = this.color == RED ? "RED" : "BLACK";
            return String.format("%s,%s", color, value);
        }
    }

    class RBIterator implements Iterator<T> {
        boolean initFlag;
        Node<T> node;

        RBIterator() {
            node = root == null?null:root.findLeftBorder();
            initFlag = true;
        }

        @Override
        public boolean hasNext() {
            if (node == null)return false;
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
        public void remove() {
            if(initFlag)throw new IllegalStateException();
            RedBlackTree.this.remove(node.value);
        }

        @Override
        public T next() {
            if (node == null)throw new NoSuchElementException("root is null");
            if (initFlag) {
                initFlag = false;
                return node.value;
            }
            if (node.hasRightChild()) {
                node = node.rightChild;
                while (node.hasLeftChild()) node = node.leftChild;
                return node.value;
            } else {
                Node<T> temp = node;
                while (temp.isRightChild()) temp = temp.parent;
                if (!temp.hasParent()) throw new NoSuchElementException();
                node = temp.parent;
                return node.value;
            }
        }
    }
}
