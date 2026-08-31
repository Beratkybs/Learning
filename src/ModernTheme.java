import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Modern tema yardımcı sınıfı.
 * Tüm panellerde kullanılan renk, font ve özel bileşen tanımları burada merkezi olarak tutulur.
 */
public class ModernTheme {

    // ======================== RENKLER ========================
    public static final Color BG_DARK        = new Color(26, 26, 46);     // #1a1a2e
    public static final Color BG_PANEL       = new Color(22, 33, 62);     // #16213e
    public static final Color BG_CARD        = new Color(15, 52, 96);     // #0f3460
    public static final Color ACCENT_PINK    = new Color(233, 69, 96);    // #e94560
    public static final Color ACCENT_PURPLE  = new Color(83, 52, 131);    // #533483
    public static final Color SUCCESS        = new Color(0, 184, 148);    // #00b894
    public static final Color ERROR          = new Color(255, 107, 107);  // #ff6b6b
    public static final Color WARNING        = new Color(255, 217, 61);   // #ffd93d
    public static final Color TEXT_PRIMARY   = new Color(234, 234, 234);  // #eaeaea
    public static final Color TEXT_SECONDARY = new Color(160, 160, 176);  // #a0a0b0
    public static final Color BORDER_COLOR   = new Color(55, 65, 95);     // subtle border
    public static final Color HOVER_GLOW     = new Color(233, 69, 96, 40);// pink glow
    public static final Color INPUT_BG       = new Color(30, 40, 68);
    public static final Color MENU_HOVER     = new Color(40, 55, 90);

    // ======================== FONTLAR ========================
    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 18);
    public static final Font FONT_HEADING  = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_MONO     = new Font("Consolas", Font.PLAIN, 14);
    public static final Font FONT_BIG      = new Font("Segoe UI", Font.BOLD, 48);

    // ======================== BOYUTLAR ========================
    public static final int BORDER_RADIUS = 12;
    public static final int CARD_PADDING  = 16;

    // ======================== FABRIKA METODLARI ========================

    /** Modern, hover efektli yuvarlak köşeli buton oluşturur. */
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private boolean hovering = false;
            private float hoverProgress = 0f;

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        animateHover();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        animateHover();
                    }
                });
            }

            private void animateHover() {
                Timer timer = new Timer(16, null);
                timer.addActionListener(e -> {
                    if (hovering && hoverProgress < 1f) {
                        hoverProgress = Math.min(1f, hoverProgress + 0.15f);
                        repaint();
                    } else if (!hovering && hoverProgress > 0f) {
                        hoverProgress = Math.max(0f, hoverProgress - 0.15f);
                        repaint();
                    } else {
                        timer.stop();
                    }
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                // Arka plan
                Color bg = bgColor;
                if (hoverProgress > 0) {
                    bg = blendColors(bgColor, bgColor.brighter(), hoverProgress * 0.3f);
                }

                // Glow efekti
                if (hoverProgress > 0) {
                    g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(),
                            (int)(40 * hoverProgress)));
                    g2.fill(new RoundRectangle2D.Float(-4, -4, w + 8, h + 8, BORDER_RADIUS + 4, BORDER_RADIUS + 4));
                }

                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, BORDER_RADIUS, BORDER_RADIUS));

                // Üst parlaklık efekti
                GradientPaint shine = new GradientPaint(0, 0,
                        new Color(255, 255, 255, 30), 0, h / 2, new Color(255, 255, 255, 0));
                g2.setPaint(shine);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h / 2, BORDER_RADIUS, BORDER_RADIUS));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setPreferredSize(new Dimension(200, 44));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return button;
    }

    /** Modern stil textfield oluşturur. */
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                g2.dispose();
                super.paintComponent(g);

                // Placeholder
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g3 = (Graphics2D) getGraphics();
                    if (g3 != null) {
                        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g3.setColor(TEXT_SECONDARY);
                        g3.setFont(FONT_BODY);
                        g3.drawString(placeholder, 12, getHeight() / 2 + 5);
                        g3.dispose();
                    }
                }
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_PINK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(280, 40));
        return field;
    }

    /** İki rengi karıştırır. */
    public static Color blendColors(Color c1, Color c2, float ratio) {
        float ir = 1.0f - ratio;
        return new Color(
                (int)(c1.getRed() * ir + c2.getRed() * ratio),
                (int)(c1.getGreen() * ir + c2.getGreen() * ratio),
                (int)(c1.getBlue() * ir + c2.getBlue() * ratio)
        );
    }

    // ======================== ÖZEL PANEL SINIFLARI ========================

    /** Gradient arka planlı panel. */
    public static class GradientPanel extends JPanel {
        private final Color color1, color2;
        private final boolean vertical;

        public GradientPanel(Color c1, Color c2, boolean vertical) {
            this.color1 = c1;
            this.color2 = c2;
            this.vertical = vertical;
            setOpaque(false);
        }

        public GradientPanel(Color c1, Color c2) {
            this(c1, c2, true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp;
            if (vertical) {
                gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            } else {
                gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
            }
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Yuvarlak köşeli, gölgeli kart paneli. */
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));
        }

        public RoundedPanel(Color bgColor) {
            this(BORDER_RADIUS, bgColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gölge
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fill(new RoundRectangle2D.Float(3, 3, getWidth() - 3, getHeight() - 3, radius, radius));

            // Ana arka plan
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 3, getHeight() - 3, radius, radius));

            // İnce kenarlık
            g2.setColor(new Color(255, 255, 255, 15));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, radius, radius));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Hover efektli kart label (kelime eşleştirme vb. için). */
    public static class CardLabel extends JLabel {
        private boolean hovering = false;
        private Color cardBg;
        private final Color defaultBg;

        public CardLabel(String text, Color bg) {
            super(text, SwingConstants.CENTER);
            this.cardBg = bg;
            this.defaultBg = bg;
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
            });
        }

        public void setCardBackground(Color c) { this.cardBg = c; repaint(); }
        public void resetBackground() { this.cardBg = defaultBg; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = hovering ? cardBg.brighter() : cardBg;

            // Gölge
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 2, getHeight() - 2, 10, 10));

            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 2, 10, 10));

            // Kenarlık
            if (hovering) {
                g2.setColor(ACCENT_PINK);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 4, getHeight() - 4, 10, 10));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Menü butonu — ikon + metin, hover efektli. */
    public static JButton createMenuButton(String emoji, String text) {
        JButton button = new JButton(emoji + "  " + text) {
            private boolean hovering = false;

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setHorizontalAlignment(SwingConstants.LEFT);
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovering) {
                    g2.setColor(MENU_HOVER);
                    g2.fill(new RoundRectangle2D.Float(4, 2, getWidth() - 8, getHeight() - 4, 8, 8));

                    // Sol kenar aksan çizgisi
                    g2.setColor(ACCENT_PINK);
                    g2.fillRoundRect(0, 6, 3, getHeight() - 12, 3, 3);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return button;
    }

    /** Skor badge'i oluşturur. */
    public static JLabel createScoreBadge(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, ACCENT_PURPLE, getWidth(), 0, ACCENT_PINK);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(FONT_BUTTON);
        label.setForeground(Color.WHITE);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        label.setPreferredSize(new Dimension(160, 36));
        return label;
    }

    /** Uygulamanın LookAndFeel'ini yapılandırır. */
    public static void applyTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Panel.background", BG_DARK);
            UIManager.put("OptionPane.background", BG_PANEL);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("OptionPane.messageFont", FONT_BODY);
            UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
            UIManager.put("Button.background", BG_CARD);
            UIManager.put("Button.foreground", TEXT_PRIMARY);
            UIManager.put("Button.font", FONT_BUTTON);
            UIManager.put("TextField.background", INPUT_BG);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", ACCENT_PINK);
            UIManager.put("TextArea.background", BG_PANEL);
            UIManager.put("TextArea.foreground", TEXT_PRIMARY);
            UIManager.put("ScrollPane.background", BG_DARK);
            UIManager.put("ScrollBar.background", BG_DARK);
            UIManager.put("ScrollBar.thumb", BG_CARD);
            UIManager.put("ScrollBar.track", BG_DARK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
