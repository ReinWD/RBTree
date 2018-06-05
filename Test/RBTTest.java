import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

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
        Assertions.assertTrue(checker.checkRoute(root),checker.cause);
    }

}
