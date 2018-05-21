import javax.swing.*;
import java.awt.*;

public class TreeChart extends JPanel {
    Thread updateThread;
    private int startartangle;
    TreeChart(){
        startartangle = 0;
        this.setBackground(new Color(0x66ccff));
        this.setVisible(true);
        this.setSize(200,200);
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    TreeChart.this.repaint();
                    startartangle++;
                    try{
                        Thread.sleep(20);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        updateThread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (g==null)return;
        int height = this.getHeight();
        int width = this.getWidth();

        g.setColor(new Color(0x0));
        g.drawArc((width/2)-25,(height/2)-25,50,50,startartangle,180);
    }
}
