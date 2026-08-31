
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;


// Uygulamanın başlatıldığı ana sınıf
public class Main1 {
    public static void main(String[] args) {
        // Temayı uygula
        ModernTheme.applyTheme();

        // Ana frame (pencere) başlatma
        JFrame frame = new JFrame("LearnIng - Dil Öğrenme Uygulaması");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Ana gradient panel
        ModernTheme.GradientPanel mainPanel = new ModernTheme.GradientPanel(
                ModernTheme.BG_DARK, new Color(22, 33, 62));
        mainPanel.setLayout(null);
        frame.setContentPane(mainPanel);

        // Sol panel (Gradient bordo → koyu mor)
        ModernTheme.GradientPanel leftPanel = new ModernTheme.GradientPanel(
                new Color(102, 0, 53), ModernTheme.ACCENT_PURPLE);
        leftPanel.setBounds(0, 0, 280, 450);
        leftPanel.setLayout(null);
        mainPanel.add(leftPanel);

        // Sol panelde büyük emoji ikon
        JLabel iconLabel = new JLabel("📘", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setBounds(90, 50, 100, 80);
        leftPanel.add(iconLabel);

        // Sol panelde başlık
        JLabel appTitle = new JLabel("LearnIng");
        appTitle.setFont(ModernTheme.FONT_TITLE);
        appTitle.setForeground(Color.WHITE);
        appTitle.setBounds(60, 140, 200, 40);
        leftPanel.add(appTitle);

        // Sol panelde açıklama metni
        JLabel desc1 = new JLabel("İngilizcenin Derinliklerini");
        desc1.setFont(ModernTheme.FONT_BODY);
        desc1.setForeground(new Color(255, 255, 255, 200));
        desc1.setBounds(40, 195, 220, 22);
        leftPanel.add(desc1);

        JLabel desc2 = new JLabel("Keşfetmeye Hazır mısın?");
        desc2.setFont(ModernTheme.FONT_BODY);
        desc2.setForeground(new Color(255, 255, 255, 200));
        desc2.setBounds(40, 218, 220, 22);
        leftPanel.add(desc2);

        // Dekoratif çizgi
        JPanel line = new JPanel();
        line.setBackground(ModernTheme.ACCENT_PINK);
        line.setBounds(40, 260, 60, 3);
        leftPanel.add(line);

        // Versiyon etiketi
        JLabel versionLabel = new JLabel("v1.0 — Modern Edition");
        versionLabel.setFont(ModernTheme.FONT_SMALL);
        versionLabel.setForeground(new Color(255, 255, 255, 120));
        versionLabel.setBounds(40, 390, 200, 20);
        leftPanel.add(versionLabel);

        // Sağ panel (Koyu arka plan)
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setBounds(280, 0, 420, 450);
        rightPanel.setLayout(null);
        mainPanel.add(rightPanel);

        // Sağ taraf başlık
        JLabel welcomeLabel = new JLabel("Hoş Geldiniz");
        welcomeLabel.setFont(ModernTheme.FONT_HEADING);
        welcomeLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        welcomeLabel.setBounds(60, 100, 300, 30);
        rightPanel.add(welcomeLabel);

        JLabel subLabel = new JLabel("Devam etmek için adınızı girin");
        subLabel.setFont(ModernTheme.FONT_SMALL);
        subLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        subLabel.setBounds(60, 135, 300, 20);
        rightPanel.add(subLabel);

        // Kullanıcı adı etiketi
        JLabel usernameLabel = new JLabel("Kullanıcı Adı");
        usernameLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        usernameLabel.setFont(ModernTheme.FONT_SMALL);
        usernameLabel.setBounds(60, 180, 100, 20);
        rightPanel.add(usernameLabel);

        // Modern textfield
        JTextField usernameField = ModernTheme.createStyledTextField("Adınızı yazın...");
        usernameField.setBounds(60, 205, 300, 42);
        rightPanel.add(usernameField);

        // Modern giriş butonu
        JButton loginButton = ModernTheme.createStyledButton("Giriş Yap  →", ModernTheme.ACCENT_PINK);
        loginButton.setBounds(60, 270, 300, 46);
        rightPanel.add(loginButton);

        // Giriş butonuna aksiyon ekle
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText(); // Kullanıcı adını al

                // Eğer kullanıcı adı girilmişse
                if (!username.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Giriş Başarılı!\nHoş geldiniz, " + username + "!");
                    frame.setVisible(false); // Giriş penceresini gizle

                    // Ana Menü penceresini oluştur
                    SwingUtilities.invokeLater(() -> {
                        AppFrame appFrame = new AppFrame();
                        appFrame.setVisible(true);
                    });
                } else {
                    // Eğer kullanıcı adı girilmemişse hata mesajı göster
                    JOptionPane.showMessageDialog(frame, "Lütfen kullanıcı adını girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Enter tuşu ile giriş
        usernameField.addActionListener(e -> loginButton.doClick());

        // Frame'i görünür yap
        frame.setVisible(true);
    }

    // Çok satırlı metin oluşturmak için yardımcı yöntem
    private static JPanel createMultilineLabel(String text, Font font, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Dikey düzen
        panel.setOpaque(false);

        String[] lines = text.split("\n"); // Metni satırlara ayır
        for (String line : lines) {
            JLabel label = new JLabel(line);
            label.setFont(font); // Yazı fontu
            label.setForeground(color); // Yazı rengi
            panel.add(label); // Etiketi panele ekle
        }

        return panel; // Paneli geri döndür
    }
}

// Ana Menü Frame Sınıfı
class AppFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public AppFrame() {
        setTitle("LearnIng — Dil Öğrenme Uygulaması");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sol Menü Paneli
        ModernTheme.GradientPanel menuPanel = new ModernTheme.GradientPanel(
                new Color(18, 18, 38), new Color(22, 30, 55));
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Menü üst logo
        JLabel menuLogo = new JLabel("  📘 LearnIng");
        menuLogo.setFont(ModernTheme.FONT_HEADING);
        menuLogo.setForeground(Color.WHITE);
        menuLogo.setBorder(BorderFactory.createEmptyBorder(20, 10, 25, 10));
        menuLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(menuLogo);

        // Dekoratif çizgi
        JPanel separator = new JPanel();
        separator.setMaximumSize(new Dimension(180, 1));
        separator.setBackground(ModernTheme.BORDER_COLOR);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(separator);
        menuPanel.add(Box.createVerticalStrut(10));

        // İçerik Paneli (CardLayout kullanılıyor)
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout); // CardLayout ile panel değişimi sağlanacak
        contentPanel.setBackground(ModernTheme.BG_DARK);

        // Panelleri başlat
        WelcomePanel welcomePanel = new WelcomePanel();
        WordsPanel wordsPanel = new WordsPanel();
        EnglishLearningGame englishLearningGame = new EnglishLearningGame();
        WordMatch wordMatch = new WordMatch();
        HangmanWindow hangmanWindow = new HangmanWindow();
        ChatWindow chatWindow = new ChatWindow();
        WordQuiz wordQuiz = new WordQuiz(wordsPanel); // WordsPanel'i WordQuiz'e geçir

        // Panelleri CardLayout'a ekle
        contentPanel.add(welcomePanel, "Welcome");
        contentPanel.add(wordsPanel, "Words");
        contentPanel.add(englishLearningGame, "EnglishLearningGame");
        contentPanel.add(wordMatch, "WordMatch");
        contentPanel.add(hangmanWindow, "HangmanWindow");
        contentPanel.add(chatWindow, "ChatWindow");
        contentPanel.add(wordQuiz, "WordQuiz"); // WordQuiz'i ekle

        // Menü Butonları
        JButton chatWindowButton = ModernTheme.createMenuButton("💬", "ChatBot");
        JButton wordsButton = ModernTheme.createMenuButton("⭐", "Favoriler");
        JButton englishLearningGameButton = ModernTheme.createMenuButton("🎓", "English Game");
        JButton wordMatchButton = ModernTheme.createMenuButton("🔗", "Kelime Eşleştir");
        JButton hangmanWindowButton = ModernTheme.createMenuButton("🎯", "Adam Asmaca");
        JButton wordQuizButton = ModernTheme.createMenuButton("📝", "Kelime Quiz");

        // Butonlara aksiyon dinleyicileri ekle
        chatWindowButton.addActionListener(e -> cardLayout.show(contentPanel, "ChatWindow"));
        wordsButton.addActionListener(e -> cardLayout.show(contentPanel, "Words"));
        englishLearningGameButton.addActionListener(e -> cardLayout.show(contentPanel, "EnglishLearningGame"));
        wordMatchButton.addActionListener(e -> cardLayout.show(contentPanel, "WordMatch"));
        hangmanWindowButton.addActionListener(e -> cardLayout.show(contentPanel, "HangmanWindow"));
        wordQuizButton.addActionListener(e -> cardLayout.show(contentPanel, "WordQuiz")); // WordQuiz butonunun aksiyonu

        // Butonları menü paneline ekle
        JButton[] menuButtons = {chatWindowButton, wordsButton, englishLearningGameButton,
                wordMatchButton, hangmanWindowButton, wordQuizButton};
        for (JButton btn : menuButtons) {
            btn.setMaximumSize(new Dimension(200, 48));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(4));
        }

        menuPanel.add(Box.createVerticalGlue());

        // Alt kısma versiyon bilgisi
        JLabel footerLabel = new JLabel("  v1.0 Modern");
        footerLabel.setFont(ModernTheme.FONT_SMALL);
        footerLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        footerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(footerLabel);

        // Panelleri frame'e ekle
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true); // Frame görünür yap
    }
}
