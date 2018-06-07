package moe.reinwd;

import debug.LogTool;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Main {
    File file = new File("str");
    BufferedReader reader= new BufferedReader(new FileReader(file));
    String last;
    private Random random;
    ArrayList<String> strList;
    RBTChecker checker;

    private void next() throws IOException {
        String buffer;
        if (last!=null) tree2.add(last);
        buffer = reader.readLine();
        last = buffer;
        tree.add(buffer);
    }

    RedBlackTree<String> tree,tree2;

    public Main() throws IOException {
        checker = new RBTChecker();
        tree = new RedBlackTree<>();
        tree2 = new RedBlackTree<>();
        treeView.setModel(new RBTreeModel<>(tree));
        treeViewPre.setModel(new RBTreeModel<>(tree2));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    next();
                    treeView.updateUI();
                    treeViewPre.updateUI();
                    for (int i = 0; i < treeView.getRowCount(); i++) {
                        treeView.expandRow(i);
                        treeViewPre.expandRow(i);
                    }
                    checkValidation();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (random == null)random = new Random();
                int index = random.nextInt(tree.size());
                Iterator<String> iterator = tree.iterator();
                for (int i = 0; i <= index; i++) {
                    iterator.next();
                }
                iterator.remove();
                checkValidation();
                treeView.updateUI();
            }
        });
        splitLine.setDividerLocation(splitLine.getMaximumDividerLocation());
    }

    private void checkValidation() {
        boolean result = checker.checkRoute(tree.getRoot());
        isValidTree.setText(String.valueOf(result));
        LogTool.logValidate(result);
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel panel1;
    private JPanel panel2;
    private JTree treeView;
    private JButton nextButton;
    private JTree treeViewPre;
    private JLabel isValidTree;
    private JButton previousButton;
    private JSplitPane splitLine;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel2 = new TreeChart();

    }

    class RBTreeModel<T extends Comparable> implements TreeModel {
        RedBlackTree<T> tree;

        RBTreeModel(RedBlackTree<T> tree){
            this.tree = tree;
        }

        @Override
        public Object getRoot() {
            return tree.getRoot();
        }

        @Override
        public Object getChild(Object parent, int index) {
            RedBlackTree.Node node = ((RedBlackTree.Node) parent);
            switch (index){
                case 0:
                    if (node.hasLeftChild())return node.leftChild;
                    else return node.rightChild;
                case 1:
                    return node.rightChild;
                default:return null;
            }
        }

        @Override
        public int getChildCount(Object parent) {
            RedBlackTree.Node node = ((RedBlackTree.Node) parent);
            int count = 0;
            if (node.hasRightChild())count++;
            if (node.hasLeftChild())count++;
            return count;
        }

        @Override
        public boolean isLeaf(Object node) {
            return !((RedBlackTree.Node) node).hasLeftChild()&&!((RedBlackTree.Node) node).hasRightChild();
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            return 0;
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {

        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {

        }
    }
}
