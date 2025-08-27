import java.awt.*;
import javax.swing.*;
import java.util.Map;

class PieChartPanel extends JPanel {
    private Map<String, Double> data;

    public PieChartPanel(Map<String, Double> data) {
        this.data = data;
        setPreferredSize(new Dimension(350, 250));
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        repaint();
    }

    // 🔥 Method to generate unique bright color based on index
    private Color getUniqueColor(int index, int total) {
        float hue = (float) index / total;   // equally spaced hue
        return Color.getHSBColor(hue, 0.8f, 0.9f); // bright & colorful
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 80;

        // 🔹 Calculate total sum
        double total = 0;
        for (double value : data.values()) {
            total += value;
        }

        // 🔹 Draw Pie
        int startAngle = 0;
        int i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double angle = (entry.getValue() / total) * 360;

            g2.setColor(getUniqueColor(i, data.size()));
            g2.fillArc(20, 20, diameter, diameter, startAngle, (int) angle);

            startAngle += (int) angle;
            i++;
        }

        // 🔹 Draw Legend
        int legendX = diameter + 40;
        int legendY = 30;
        i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            g2.setColor(getUniqueColor(i, data.size()));
            g2.fillRect(legendX, legendY, 15, 15);

            g2.setColor(Color.BLACK);
            g2.drawRect(legendX, legendY, 15, 15);
            g2.drawString(entry.getKey(), legendX + 25, legendY + 12);

            legendY += 20;
            i++;
        }
    }
}