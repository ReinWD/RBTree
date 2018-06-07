import moe.reinwd.RBTChecker;
import moe.reinwd.RedBlackTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

class RBTTest {
    RedBlackTree<String> a;

    RBTTest (){
        a = new RedBlackTree<>();
        try {
            initTree();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void initTree() throws IOException {
        File file = new File("str");
        BufferedReader reader= new BufferedReader(new FileReader(file));
        String buffer;
        while ((buffer = reader.readLine())!=null){
            a.add(buffer);
        }
    }

    @Test
    void RBTEmptyTest(){
        a= new RedBlackTree<>();
        Assertions.assertEquals(true,a.isEmpty());
        a.add("233");
        Assertions.assertEquals(false,a.isEmpty());
    }

    @Test
    void RBTContainsTest(){
        a.remove("过气游戏");
        Assertions.assertFalse(a.contains("过气游戏"));
        RBTisValid();
    }

    @Test
    void RBTisValid(){
        if (a.isEmpty())return;
        RedBlackTree.Node root = a.getRoot();
        RBTChecker checker = new RBTChecker();
        Assertions.assertTrue(checker.checkRoute(root),checker.getCause());
    }

    @Test
    void finalTest() throws IOException {
        ArrayList<String> stringList = new ArrayList<>();
        RBTChecker checker = new RBTChecker();
        File file = new File("str");
        BufferedReader reader= new BufferedReader(new FileReader(file));
        String buffer;
        while ((buffer = reader.readLine())!=null){
            stringList.add(buffer);
        }
        Random random = new Random();
        RedBlackTree tree = new RedBlackTree();
        while (tree.size()<stringList.size()){
            int index = random.nextInt(stringList.size());
            if (!tree.add(stringList.get(index))){
                tree.remove(stringList.get(index));
            }
            Assertions.assertTrue(checker.checkRoute(tree.getRoot()),checker.getCause());
        }

    }

    @Test
    void iteratorTest(){
        Iterator<String> iterator= a.iterator();
        int i = 0;
        while (iterator.hasNext()){
            i++;
            iterator.next();
        }
        Assertions.assertEquals(i,a.size());
    }
}
