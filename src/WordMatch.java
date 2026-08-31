import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class WordMatch extends JPanel {
    // Kelimeler ve anlamları
    private final String[] words = {"apple", "banana", "car", "dog", "house"};
    private final String[] meanings = {"elma", "muz", "araba", "köpek", "ev"};

    // GUI bileşenleri
    private JPanel wordPanel, meaningPanel;
    private ModernTheme.CardLabel[] wordLabels, meaningLabels;
    private int score = 0; // Skor değişkeni
    private JLabel scoreLabel; // Skor etiketi
    private ModernTheme.CardLabel selectedWordLabel = null; // Seçilen kelime etiketi

    private ArrayList<String> shuffledMeanings; // Karıştırılmış anlamlar listesi

    public WordMatch() {
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar — başlık ve skor
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("🔗  Kelime Eşleştirme");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        scoreLabel = ModernTheme.createScoreBadge("⭐ Skor: " + score);
        topBar.add(scoreLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Kelimeler için panel
        wordPanel = new JPanel();
        wordPanel.setLayout(new GridLayout(words.length, 1, 8, 8));
        wordPanel.setBackground(ModernTheme.BG_DARK);
        wordPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 8));

        wordLabels = new ModernTheme.CardLabel[words.length];
        for (int i = 0; i < words.length; i++) {
            wordLabels[i] = new ModernTheme.CardLabel(words[i], ModernTheme.BG_CARD);
            wordLabels[i].setFont(ModernTheme.FONT_BODY);
            final int idx = i;
            wordLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Önceden seçilen kelimeyi sıfırla
                    if (selectedWordLabel != null) {
                        selectedWordLabel.setCardBackground(ModernTheme.BG_CARD);
                    }
                    // Yeni seçilen kelimeyi işaretle
                    selectedWordLabel = wordLabels[idx];
                    selectedWordLabel.setCardBackground(ModernTheme.ACCENT_PURPLE);
                }
            });
            wordPanel.add(wordLabels[i]);
        }

        // Anlamları karıştır
        shuffledMeanings = new ArrayList<>();
        Collections.addAll(shuffledMeanings, meanings);
        Collections.shuffle(shuffledMeanings); // Anlamları karıştır

        // Anlamlar için panel
        meaningPanel = new JPanel();
        meaningPanel.setLayout(new GridLayout(meanings.length, 1, 8, 8));
        meaningPanel.setBackground(ModernTheme.BG_DARK);
        meaningPanel.setBorder(BorderFactory.createEmptyBorder(16, 8, 16, 16));

        meaningLabels = new ModernTheme.CardLabel[meanings.length];
        for (int i = 0; i < meanings.length; i++) {
            meaningLabels[i] = new ModernTheme.CardLabel(shuffledMeanings.get(i), ModernTheme.BG_CARD);
            meaningLabels[i].setFont(ModernTheme.FONT_BODY);
            final int idx = i;
            meaningLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Seçilen kelime varsa, anlamı ile karşılaştır
                    if (selectedWordLabel != null) {
                        String selectedWord = selectedWordLabel.getText();
                        String selectedMeaning = meaningLabels[idx].getText();

                        int wordIndex = -1;
                        // Kelimenin doğru indeksiyle karşılaştır
                        for (int j = 0; j < words.length; j++) {
                            if (selectedWord.equals(words[j])) {
                                wordIndex = j;
                                break;
                            }
                        }

                        // Eğer kelime ve anlam eşleşiyorsa
                        if (wordIndex != -1 && meanings[wordIndex].equals(selectedMeaning)) {
                            selectedWordLabel.setCardBackground(ModernTheme.SUCCESS);
                            meaningLabels[idx].setCardBackground(ModernTheme.SUCCESS);
                            score++;
                            scoreLabel.setText("⭐ Skor: " + score);

                            // Eğer tüm eşleştirmeler tamamlandıysa, kazandınız mesajı göster
                            if (score == words.length) {
                                showWinningMessage();
                            }
                        } else {
                            // Yanlış eşleşme durumunda kırmızı ile işaretle
                            selectedWordLabel.setCardBackground(ModernTheme.ERROR);
                            meaningLabels[idx].setCardBackground(ModernTheme.ERROR);

                            // 800ms sonra renkleri sıfırla
                            Timer resetTimer = new Timer(800, ev -> {
                                selectedWordLabel.setCardBackground(ModernTheme.BG_CARD);
                                meaningLabels[idx].setCardBackground(ModernTheme.BG_CARD);
                            });
                            resetTimer.setRepeats(false);
                            resetTimer.start();
                        }
                        selectedWordLabel = null;
                    }
                }
            });
            meaningPanel.add(meaningLabels[i]);
        }

        // Sütun başlıkları
        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.setBackground(ModernTheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 0, 16));

        JLabel engHeader = new JLabel("  🇬🇧  İngilizce", SwingConstants.LEFT);
        engHeader.setFont(ModernTheme.FONT_BUTTON);
        engHeader.setForeground(ModernTheme.TEXT_SECONDARY);

        JLabel trHeader = new JLabel("  🇹🇷  Türkçe", SwingConstants.LEFT);
        trHeader.setFont(ModernTheme.FONT_BUTTON);
        trHeader.setForeground(ModernTheme.TEXT_SECONDARY);

        headerPanel.add(engHeader);
        headerPanel.add(trHeader);

        // Ana panel (kelimeler ve anlamlar panelini yan yana yerleştir)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2, 0, 0));
        mainPanel.setBackground(ModernTheme.BG_DARK);
        mainPanel.add(wordPanel);
        mainPanel.add(meaningPanel);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(ModernTheme.BG_DARK);
        centerWrapper.add(headerPanel, BorderLayout.NORTH);
        centerWrapper.add(mainPanel, BorderLayout.CENTER);

        add(centerWrapper, BorderLayout.CENTER);
    }

    // Kazanma mesajını gösteren metod
    private void showWinningMessage() {
        JOptionPane.showMessageDialog(this, "🎉 Tebrikler! Tüm eşleştirmeleri doğru yaptınız!",
                "Oyun Bitti", JOptionPane.INFORMATION_MESSAGE); // Bilgilendirme penceresi
    }
}
