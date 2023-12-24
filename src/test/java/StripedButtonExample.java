import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class StripedButton extends JButton {
    private Color stripeColor;
    boolean a = false;

    public StripedButton(String text, Color stripeColor) {
        super(text);
        this.stripeColor = stripeColor;
        setContentAreaFilled(false); // 设置按钮内容区域透明，以便显示横条纹
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (a) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.red);
            int stripeHeight = 5;
            for (int i = 0; i < getHeight(); i += stripeHeight * 2) {
                g2d.fill(new Rectangle2D.Double(0, i, getWidth(), stripeHeight));
            }
            super.paintComponent(g2d);
            g2d.dispose();
        } else {
            super.paintComponent(g);
        }

    }
}

public class StripedButtonExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Striped Button Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 创建带有横条纹的按钮
            StripedButton stripedButton = new StripedButton("Striped Button", Color.RED);

            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(stripedButton);

            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
