package moe.reinwd;

import javax.swing.*;
import java.awt.*;

public class TreeChart extends JPanel {
    private RedBlackTree<?> rbTree;

    TreeChart(){
        this.setPreferredSize(new Dimension(200,200));
    }

    TreeChart(RedBlackTree<?> rbTree){
        this();
        this.rbTree =rbTree;

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public RedBlackTree<?> getRbTree() {
        return rbTree;
    }
@Deprecated
    public void setRbTree(RedBlackTree<?> rbTree) {
        this.rbTree = rbTree;
    }
}
