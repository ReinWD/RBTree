import javax.swing.*;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
    }

    static class MainFrame extends JFrame{
        JLabel label;
        TreeChart treeChart;

        MainFrame(){
            this.setTitle("红黑树输出");
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label = new JLabel("SB");
            treeChart = new TreeChart();
            this.add(treeChart);
            treeChart.add(label);
            this.setSize(1600,800);
            this.setVisible(true);
        }
    }
    static class WOW{
        public static void main(String[] args) {
            int cnt = 0;
            BigInteger bigInteger = new BigInteger("0");
            for (int i = 1; i < 1000000; i++) {
                if (!(i%7==0 || String.valueOf(i).contains("7"))){
                    System.out.print(1);
                    cnt++;
//                    bigInteger = bigInteger.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(i)));
                }else {
                    System.out.print(0);
                    cnt++;
                }
                if (cnt>=100){
                    System.out.print("\n");
                    cnt=0;
                }
            }
            System.out.println(bigInteger);
        }
    }

}
