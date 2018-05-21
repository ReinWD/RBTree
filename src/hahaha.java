import javax.swing.*;

public class hahaha {
    public static void main(String[] args) {
        JFrame frame = new JFrame("hahaha");
        frame.setContentPane(new hahaha().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel panel1;
    private JPanel panel2;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel2 = new TreeChart();
    }
}
