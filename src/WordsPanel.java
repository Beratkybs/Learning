import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

class WordsPanel extends JPanel {
    private final JPanel wordsContainer; // Kelimelerin dikey olarak yerleştirileceği panel

    public WordsPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("⭐  Favori Kelimeler");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        add(topBar, BorderLayout.NORTH);

        // Kelime kartları konteyneri
        wordsContainer = new JPanel();
        wordsContainer.setLayout(new BoxLayout(wordsContainer, BoxLayout.Y_AXIS));
        wordsContainer.setBackground(ModernTheme.BG_DARK);
        wordsContainer.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Boş durum mesajı
        JLabel emptyLabel = new JLabel("Henüz favori kelime eklenmedi. Quiz ekranından kelime ekleyebilirsiniz.");
        emptyLabel.setFont(ModernTheme.FONT_BODY);
        emptyLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emptyLabel.setName("emptyLabel");
        wordsContainer.add(emptyLabel);

        JScrollPane scrollPane = new JScrollPane(wordsContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.BG_DARK);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addWord(String word) {
        // Boş durum mesajını kaldır
        for (Component comp : wordsContainer.getComponents()) {
            if (comp instanceof JLabel && "emptyLabel".equals(comp.getName())) {
                wordsContainer.remove(comp);
                break;
            }
        }

        // Kelime kartı oluştur
        JPanel wordCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(ModernTheme.BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                g2.dispose();
                super.paintComponent(g);
            }
        };
        wordCard.setOpaque(false);
        wordCard.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        wordCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        wordCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel wordLabel = new JLabel("📖  " + word);
        wordLabel.setFont(ModernTheme.FONT_BODY);
        wordLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        wordCard.add(wordLabel, BorderLayout.CENTER);

        wordsContainer.add(wordCard);
        wordsContainer.add(Box.createVerticalStrut(8));
        wordsContainer.revalidate();
        wordsContainer.repaint();
    }
}
