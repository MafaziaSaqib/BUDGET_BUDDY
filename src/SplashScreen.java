import javax.swing.*;
import java.awt.*;
public class SplashScreen extends JPanel {

    private Image logo;

    public SplashScreen() {
        logo = new ImageIcon(getClass().getResource("/BUDGET_BUDDY.png")).getImage();
        setPreferredSize(new Dimension(1280,690));
        setLayout(new BorderLayout());
        JProgressBar bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setString("Loading...");
        bar.setPreferredSize(new Dimension(200, 25));
        bar.setForeground(new Color(123,162,221));
        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        barPanel.setOpaque(false);
        barPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        barPanel.add(bar);
        add(barPanel, BorderLayout.SOUTH);

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(176,255,223,255),      // left side color (green)
                getWidth(), 0, new Color(133,162,241,245) // right side color (blue)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        int x = (getWidth() - logo.getWidth(this)) / 2;
        int y = (getHeight() - logo.getHeight(this)) / 2;
        g.drawImage(logo, x, y, this);
    }
}


