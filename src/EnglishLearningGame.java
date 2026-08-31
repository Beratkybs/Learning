import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class EnglishLearningGame extends JPanel {
    // Kelimeler, anlamlar ve eş anlamlılar
    private final String[] words = {"happy", "sad", "fast", "slow"};
    private final String[] meanings = {"mutlu", "üzgün", "hızlı", "yavaş"};
    private final String[] synonyms = {"joyful", "unhappy", "quick", "lethargic"};

    // Skor ve mevcut soru indexi
    private int score = 0;
    private int currentQuestion = 0;

    // GUI bileşenleri
    private final JLabel questionLabel;
    private final JTextArea resultArea;
    private final JButton[] meaningButtons;
    private final JButton nextButton;
    private final JLabel scoreLabel;

    // Constructor, GUI bileşenlerini başlatma
    public EnglishLearningGame() {
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar — başlık ve skor
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("🎓  English Learning Game");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        scoreLabel = ModernTheme.createScoreBadge("⭐ Skor: " + score);
        topBar.add(scoreLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Merkez panel
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setBackground(ModernTheme.BG_DARK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Soru kartı
        ModernTheme.RoundedPanel questionCard = new ModernTheme.RoundedPanel(16, ModernTheme.BG_PANEL);
        questionCard.setLayout(new BorderLayout());
        questionCard.setPreferredSize(new Dimension(0, 60));

        questionLabel = new JLabel("Kelimenin anlamını seçin: " + words[currentQuestion], SwingConstants.CENTER);
        questionLabel.setFont(ModernTheme.FONT_HEADING);
        questionLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        questionCard.add(questionLabel, BorderLayout.CENTER);

        centerPanel.add(questionCard, BorderLayout.NORTH);

        // Sonuç alanı
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(ModernTheme.FONT_MONO);
        resultArea.setBackground(ModernTheme.BG_PANEL);
        resultArea.setForeground(ModernTheme.TEXT_PRIMARY);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        resultArea.setCaretColor(ModernTheme.ACCENT_PINK);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernTheme.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(ModernTheme.BG_PANEL);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Alt panel — butonlar
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setBackground(ModernTheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 16, 24));

        // Cevap butonları (2x2 grid)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(ModernTheme.BG_DARK);
        meaningButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            meaningButtons[i] = ModernTheme.createStyledButton("", ModernTheme.BG_CARD);
            meaningButtons[i].setPreferredSize(new Dimension(0, 48));
            meaningButtons[i].addActionListener(new MeaningButtonListener());
            buttonPanel.add(meaningButtons[i]);
        }

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // "Sonraki" butonu
        nextButton = ModernTheme.createStyledButton("Sonraki  →", ModernTheme.ACCENT_PINK);
        nextButton.addActionListener(new NextButtonListener());
        nextButton.setEnabled(false);

        JPanel nextPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextPanel.setBackground(ModernTheme.BG_DARK);
        nextPanel.add(nextButton);
        bottomPanel.add(nextPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // İlk soruyu yükle
        loadQuestion();
    }

    // Soruyu yükleme ve seçenekleri karıştırma
    private void loadQuestion() {
        // Soruyu güncelle
        questionLabel.setText("\"" + words[currentQuestion] + "\" kelimesinin anlamını seçin");
        resultArea.setText(""); // Sonuç alanını temizle
        nextButton.setEnabled(false); // "Sonraki" butonunu pasif yap

        // Şıklar için anlamları karıştır
        ArrayList<String> options = new ArrayList<>();
        options.add(meanings[currentQuestion]); // Doğru anlamı ilk seçeneğe ekle
        while (options.size() < 4) {
            String randomMeaning = meanings[(int) (Math.random() * meanings.length)]; // Rastgele bir anlam seç
            if (!options.contains(randomMeaning)) {
                options.add(randomMeaning); // Eğer zaten eklenmemişse ekle
            }
        }
        Collections.shuffle(options); // Şıkları karıştır

        // Şıkları butonlara yerleştir
        for (int i = 0; i < 4; i++) {
            meaningButtons[i].setText(options.get(i)); // Butonlara şıkları yerleştir
            meaningButtons[i].setEnabled(true); // Butonları aktif yap
        }
    }

    // Anlam butonlarına tıklama olayları
    class MeaningButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource(); // Tıklanan butonu al
            String selectedMeaning = source.getText(); // Seçilen anlamı al
            String correctMeaning = meanings[currentQuestion]; // Doğru anlamı al
            String correctSynonym = synonyms[currentQuestion]; // Doğru eş anlamlıyı al

            // Anlamı kontrol et
            if (selectedMeaning.equals(correctMeaning)) {
                resultArea.append("✅ Doğru! Bu kelimenin anlamı: " + correctMeaning + "\n");
                score++; // Doğru cevaba skor ekle
            } else {
                resultArea.append("❌ Yanlış, doğru cevap: " + correctMeaning + "\n");
            }

            // Eş anlamlıyı sor
            String selectedSynonym = JOptionPane.showInputDialog(null,
                    "\"" + words[currentQuestion] + "\" kelimesinin eş anlamlısı nedir?");
            if (selectedSynonym != null && selectedSynonym.equalsIgnoreCase(correctSynonym)) {
                resultArea.append("✅ Eş anlamlı sorusuna da doğru cevap verdiniz!\n\n");
                score++; // Eş anlamlıya da doğru cevap verildiğinde skor ekle
            } else {
                resultArea.append("❌ Yanlış, doğru eş anlamlı: " + correctSynonym + "\n\n");
            }

            // Butonları devre dışı bırak ve "Sonraki" butonunu aktif yap
            for (JButton button : meaningButtons) {
                button.setEnabled(false); // Butonları devre dışı bırak
            }
            nextButton.setEnabled(true); // "Sonraki" butonunu aktif yap
        }
    }

    // "Sonraki" butonuna tıklama olayları
    class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            scoreLabel.setText("⭐ Skor: " + score); // Skoru güncelle

            // Sonraki soruya geç
            currentQuestion++;
            if (currentQuestion < words.length) {
                loadQuestion(); // Yeni soruyu yükle
            } else {
                resultArea.append("🎉 Oyun bitti! Final Skoru: " + score + "\n"); // Oyun bitti mesajı
                nextButton.setEnabled(false); // "Sonraki" butonunu devre dışı bırak
                for (JButton button : meaningButtons) {
                    button.setEnabled(false); // Butonları devre dışı bırak
                }
            }
        }
    }
}
