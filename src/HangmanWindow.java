import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class HangmanWindow extends JPanel {
    private JTextField guessField; // Kullanıcının harf tahminini gireceği alan
    private JLabel wordDisplay; // Oyun sırasında tahmin edilen kelimenin gösterileceği alan
    private JLabel wrongGuessesLabel; // Yanlış tahminlerin gösterileceği alan
    private JLabel attemptsLabel; // Kalan hak göstergesi
    private JButton guessButton; // Tahmin butonu
    private JButton newGameButton; // Yeni oyun butonu
    private String wordToGuess; // Tahmin edilecek kelime
    private StringBuilder currentGuess; // Şu anki tahmin (boşluklarla doldurulmuş)
    private int attemptsLeft; // Kalan tahmin hakkı
    private ArrayList<Character> wrongGuesses; // Yanlış tahmin edilen harflerin listesi
    private String[] words = {"java", "programming", "hangman", "chatbot", "computer"}; // Kelimeler
    private JPanel letterCardsPanel; // Harf kartları paneli

    public HangmanWindow() {
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("🎯  Adam Asmaca");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        attemptsLabel = ModernTheme.createScoreBadge("❤️ 6 / 6");
        topBar.add(attemptsLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Merkez panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ModernTheme.BG_DARK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // Kelime kartları paneli
        letterCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        letterCardsPanel.setOpaque(false);
        letterCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        letterCardsPanel.setMaximumSize(new Dimension(600, 100));
        centerPanel.add(letterCardsPanel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Yanlış tahminler
        ModernTheme.RoundedPanel wrongPanel = new ModernTheme.RoundedPanel(12, ModernTheme.BG_PANEL);
        wrongPanel.setLayout(new BorderLayout());
        wrongPanel.setMaximumSize(new Dimension(500, 70));
        wrongPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrongGuessesLabel = new JLabel("Yanlış tahminler: —");
        wrongGuessesLabel.setFont(ModernTheme.FONT_BODY);
        wrongGuessesLabel.setForeground(ModernTheme.ERROR);
        wrongGuessesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wrongPanel.add(wrongGuessesLabel, BorderLayout.CENTER);

        centerPanel.add(wrongPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // Alt panel — giriş alanı
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(ModernTheme.BG_PANEL);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 0));

        JLabel promptLabel = new JLabel("Harf girin:");
        promptLabel.setFont(ModernTheme.FONT_BODY);
        promptLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        bottomPanel.add(promptLabel);

        guessField = ModernTheme.createStyledTextField("Bir harf yazın...");
        guessField.setPreferredSize(new Dimension(180, 40));
        guessField.setColumns(8);
        bottomPanel.add(guessField);

        guessButton = ModernTheme.createStyledButton("Tahmin Et", ModernTheme.ACCENT_PINK);
        guessButton.setPreferredSize(new Dimension(140, 40));
        bottomPanel.add(guessButton);

        newGameButton = ModernTheme.createStyledButton("🔄 Yeni Oyun", ModernTheme.ACCENT_PURPLE);
        newGameButton.setPreferredSize(new Dimension(140, 40));
        bottomPanel.add(newGameButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Tahmin butonuna tıklama olayını işle
        guessButton.addActionListener(e -> processGuess());
        guessField.addActionListener(e -> processGuess());
        newGameButton.addActionListener(e -> startGame());

        startGame(); // Oyunu başlat
    }

    // Kelimeyi harf kartları olarak göster
    private void updateLetterCards() {
        letterCardsPanel.removeAll();
        for (int i = 0; i < currentGuess.length(); i++) {
            char c = currentGuess.charAt(i);
            JLabel card = new JLabel(String.valueOf(c).toUpperCase(), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color bg = getText().equals("_") ? ModernTheme.BG_CARD : ModernTheme.SUCCESS;
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setFont(new Font("Consolas", Font.BOLD, 28));
            card.setForeground(Color.WHITE);
            card.setOpaque(false);
            card.setPreferredSize(new Dimension(48, 56));
            card.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            letterCardsPanel.add(card);
        }
        letterCardsPanel.revalidate();
        letterCardsPanel.repaint();
    }

    // Yeni bir oyun başlatan metod
    private void startGame() {
        Random rand = new Random(); // Rastgele sayı üretici
        wordToGuess = words[rand.nextInt(words.length)]; // Rastgele kelime seç
        currentGuess = new StringBuilder("_".repeat(wordToGuess.length())); // Kelimenin uzunluğunda boşluklar
        attemptsLeft = 6; // Başlangıçta 6 tahmin hakkı
        wrongGuesses = new ArrayList<>(); // Yanlış tahminleri başlat

        updateLetterCards();
        wrongGuessesLabel.setText("Yanlış tahminler: —");
        attemptsLabel.setText("❤️ " + attemptsLeft + " / 6");
        guessButton.setEnabled(true);
        guessField.setEnabled(true);
        guessField.setText("");
        guessField.requestFocusInWindow();
    }

    // Kullanıcının tahminini işleyen metod
    private void processGuess() {
        String guess = guessField.getText().trim().toLowerCase(); // Tahmini al ve küçük harfe çevir
        if (guess.length() == 1 && Character.isLetter(guess.charAt(0))) { // Geçerli bir harf mi?
            char guessedLetter = guess.charAt(0); // Tahmin edilen harfi al
            boolean correctGuess = false;

            // Kelimenin her harfi ile karşılaştırma yap
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == guessedLetter) {
                    currentGuess.setCharAt(i, guessedLetter); // Eğer doğru tahminse, harfi yerleştir
                    correctGuess = true;
                }
            }

            // Eğer doğru tahmin yapılmadıysa, yanlış tahmin ekle
            if (!correctGuess) {
                if (!wrongGuesses.contains(guessedLetter)) {
                    wrongGuesses.add(guessedLetter);
                    attemptsLeft--; // Kalan tahmin hakkını azalt
                }
            }

            // Son durumu güncelle
            updateLetterCards();
            attemptsLabel.setText("❤️ " + attemptsLeft + " / 6");

            StringBuilder wrongText = new StringBuilder("Yanlış tahminler: ");
            if (wrongGuesses.isEmpty()) {
                wrongText.append("—");
            } else {
                for (int i = 0; i < wrongGuesses.size(); i++) {
                    if (i > 0) wrongText.append("  ");
                    wrongText.append(Character.toUpperCase(wrongGuesses.get(i)));
                }
            }
            wrongGuessesLabel.setText(wrongText.toString());
            guessField.setText(""); // Textfield'ı temizle
            guessField.requestFocusInWindow();

            // Oyunun bitip bitmediğini kontrol et
            if (currentGuess.toString().equals(wordToGuess)) {
                JOptionPane.showMessageDialog(this, "🎉 Kazandınız! Kelime: " + wordToGuess.toUpperCase(),
                        "Tebrikler!", JOptionPane.INFORMATION_MESSAGE);
                guessButton.setEnabled(false);
                guessField.setEnabled(false);
            } else if (attemptsLeft <= 0) {
                JOptionPane.showMessageDialog(this, "😔 Kaybettiniz! Kelime: " + wordToGuess.toUpperCase(),
                        "Oyun Bitti", JOptionPane.ERROR_MESSAGE);
                guessButton.setEnabled(false);
                guessField.setEnabled(false);
            }
        } else {
            // Geçersiz giriş için uyarı göster
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir harf girin.",
                    "Geçersiz Giriş", JOptionPane.WARNING_MESSAGE);
        }
    }
}
