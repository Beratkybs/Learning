import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

class WordQuiz extends JPanel {
    private final JLabel questionLabel; // Soru etiketini tanımla
    private final JButton option1Button; // Birinci seçenek butonu
    private final JButton option2Button; // İkinci seçenek butonu
    private final JButton favoriteButton; // Favorilere ekle butonu
    private final WordsPanel wordsPanel; // Favorilere ekle panelini tutacak
    private int correctCount = 0;
    private int totalCount = 0;
    private JLabel statsLabel;

    private final Map<String, String> wordQuizData; // Kelime ve anlamlarını tutan veri yapısı
    private String currentWord; // Şu anki soru kelimesi

    public WordQuiz(WordsPanel wordsPanel) {
        this.wordsPanel = wordsPanel;
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("📝  Kelime Quiz");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        statsLabel = ModernTheme.createScoreBadge("✅ 0 / 0");
        topBar.add(statsLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Quiz verilerini başlat
        wordQuizData = new HashMap<>();
        wordQuizData.put("apple", "elma");
        wordQuizData.put("book", "kitap");
        wordQuizData.put("car", "araba");
        wordQuizData.put("dog", "köpek");
        wordQuizData.put("house", "ev");

        // Merkez — soru kartı
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BG_DARK);

        ModernTheme.RoundedPanel questionCard = new ModernTheme.RoundedPanel(20, ModernTheme.BG_PANEL);
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setPreferredSize(new Dimension(450, 300));
        questionCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Soru ikonu
        JLabel qIcon = new JLabel("🤔", SwingConstants.CENTER);
        qIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        qIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionCard.add(qIcon);
        questionCard.add(Box.createVerticalStrut(12));

        // Soru etiketi
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(ModernTheme.FONT_HEADING);
        questionLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionCard.add(questionLabel);
        questionCard.add(Box.createVerticalStrut(30));

        // Seçenek butonları
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        optionsPanel.setOpaque(false);
        optionsPanel.setMaximumSize(new Dimension(400, 50));
        optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        option1Button = ModernTheme.createStyledButton("", ModernTheme.BG_CARD);
        option2Button = ModernTheme.createStyledButton("", ModernTheme.BG_CARD);

        optionsPanel.add(option1Button);
        optionsPanel.add(option2Button);
        questionCard.add(optionsPanel);
        questionCard.add(Box.createVerticalStrut(20));

        // Favorilere ekleme butonu
        favoriteButton = ModernTheme.createStyledButton("⭐  Favorilere Ekle", ModernTheme.ACCENT_PURPLE);
        favoriteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionCard.add(favoriteButton);

        centerPanel.add(questionCard);
        add(centerPanel, BorderLayout.CENTER);

        // Butonlara tıklama işlemleri ekle
        option1Button.addActionListener(e -> checkAnswer(option1Button.getText()));
        option2Button.addActionListener(e -> checkAnswer(option2Button.getText()));
        favoriteButton.addActionListener(e -> addToFavorites());

        // Quiz'i başlat
        nextQuestion();
    }

    private void nextQuestion() {
        // Rastgele bir kelime seç
        Object[] keys = wordQuizData.keySet().toArray();
        currentWord = (String) keys[(int) (Math.random() * keys.length)];
        String correctAnswer = wordQuizData.get(currentWord);

        // Rastgele yanlış bir seçenek oluştur
        Object[] values = wordQuizData.values().toArray();
        String wrongAnswer;
        do {
            wrongAnswer = (String) values[(int) (Math.random() * values.length)];
        } while (wrongAnswer.equals(correctAnswer)); // Doğru cevabı seçmeden yanlış cevabı oluştur

        // Seçenekleri karıştır
        if (Math.random() > 0.5) {
            option1Button.setText(correctAnswer);
            option2Button.setText(wrongAnswer);
        } else {
            option1Button.setText(wrongAnswer);
            option2Button.setText(correctAnswer);
        }

        // Soru etiketini güncelle
        questionLabel.setText("\"" + currentWord + "\" ne demek?");
    }

    private void checkAnswer(String selectedAnswer) {
        totalCount++;
        // Seçilen cevabın doğru olup olmadığını kontrol et
        if (wordQuizData.get(currentWord).equals(selectedAnswer)) {
            correctCount++;
            JOptionPane.showMessageDialog(this, "✅ Doğru!", "Sonuç", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Yanlış! Doğru cevap: " + wordQuizData.get(currentWord),
                    "Sonuç", JOptionPane.ERROR_MESSAGE);
        }
        statsLabel.setText("✅ " + correctCount + " / " + totalCount);
        nextQuestion(); // Sonraki soruyu başlat
    }

    private void addToFavorites() {
        String translation = wordQuizData.get(currentWord); // Seçilen kelimenin çevirisini al
        if (translation != null) {
            wordsPanel.addWord(currentWord + " — " + translation); // Kelimeyi ve çevirisini favorilere ekle
            JOptionPane.showMessageDialog(this, "⭐ " + currentWord + " favorilere eklendi!",
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
