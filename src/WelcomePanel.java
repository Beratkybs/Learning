import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

class WelcomePanel extends JPanel {
    private float opacity = 0f; // Fade-in animasyonu için

    public WelcomePanel() {
        setLayout(new GridBagLayout());
        setBackground(ModernTheme.BG_DARK);

        // Ana kart
        ModernTheme.RoundedPanel card = new ModernTheme.RoundedPanel(20, ModernTheme.BG_PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(450, 320));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Emoji ikon
        JLabel iconLabel = new JLabel("📘", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(16));

        // Başlık
        JLabel titleLabel = new JLabel("LearnIng");
        titleLabel.setFont(ModernTheme.FONT_BIG);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));

        // Alt başlık
        JLabel subtitleLabel = new JLabel("İngilizce Öğrenme Platformu");
        subtitleLabel.setFont(ModernTheme.FONT_SUBTITLE);
        subtitleLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(24));

        // Dekoratif gradient çizgi
        JPanel gradientLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ModernTheme.ACCENT_PINK,
                        getWidth(), 0, ModernTheme.ACCENT_PURPLE);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 4, 4));
                g2.dispose();
            }
        };
        gradientLine.setMaximumSize(new Dimension(120, 4));
        gradientLine.setPreferredSize(new Dimension(120, 4));
        gradientLine.setOpaque(false);
        gradientLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(gradientLine);
        card.add(Box.createVerticalStrut(20));

        // Bilgi metni
        JLabel infoLabel = new JLabel("Sol menüden bir aktivite seçin");
        infoLabel.setFont(ModernTheme.FONT_SMALL);
        infoLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(infoLabel);

        add(card);

        // Fade-in animasyonu
        Timer fadeTimer = new Timer(30, null);
        fadeTimer.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1f) {
                opacity = 1f;
                fadeTimer.stop();
            }
            repaint();
        });
        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dekoratif arka plan parçacıkları
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * 0.08f));

        // Dekoratif daireler
        g2.setColor(ModernTheme.ACCENT_PINK);
        g2.fillOval(-50, -50, 200, 200);
        g2.setColor(ModernTheme.ACCENT_PURPLE);
        g2.fillOval(getWidth() - 150, getHeight() - 150, 250, 250);
        g2.setColor(ModernTheme.BG_CARD);
        g2.fillOval(getWidth() / 2 - 100, -80, 300, 300);

        g2.dispose();
    }
}
