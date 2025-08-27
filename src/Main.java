import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame splashFrame = new JFrame();
            splashFrame.setUndecorated(true);
            splashFrame.add(new SplashScreen());
            splashFrame.pack();
            splashFrame.setLocationRelativeTo(null);
            splashFrame.setVisible(true);

            Timer t = new Timer(3000, e -> {
                splashFrame.dispose();
                new MainScreen();
            });
            t.setRepeats(false);
            t.start();

        });
    }
}