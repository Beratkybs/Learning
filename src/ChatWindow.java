import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatWindow extends JPanel { // Ana sınıf ChatWindow

    // Arayüz bileşenleri
    private JPanel chatPanel; // Mesaj balonları paneli
    private JScrollPane scrollPane;
    private JTextField inputField; // Kullanıcı girişi için text alanı
    private JButton sendButton; // Gönder butonu

    // Ana metod, GUI oluşturma
    public static void main(String[] args) {
        ModernTheme.applyTheme();
        SwingUtilities.invokeLater(ChatWindow::createAndShowGUI); // GUI'yi oluştur
    }

    // GUI'yi oluşturma
    private static void createAndShowGUI() {
        // Çerçeve oluşturma
        JFrame frame = new JFrame("Chatbot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // ChatWindow penceresini oluştur
        ChatWindow chatWindow = new ChatWindow();

        // ChatWindow'u çerçeveye ekle
        frame.add(chatWindow);
        frame.setVisible(true);
    }

    // ChatWindow constructor: Bileşenleri başlat
    public ChatWindow() {
        setLayout(new BorderLayout(0, 0));
        setBackground(ModernTheme.BG_DARK);

        // Üst bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ModernTheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel titleLabel = new JLabel("💬  AI ChatBot");
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        topBar.add(titleLabel, BorderLayout.WEST);

        JLabel statusLabel = new JLabel("● Çevrimiçi");
        statusLabel.setFont(ModernTheme.FONT_SMALL);
        statusLabel.setForeground(ModernTheme.SUCCESS);
        topBar.add(statusLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Sohbet alanı — mesaj balonları
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(ModernTheme.BG_DARK);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.BG_DARK);
        add(scrollPane, BorderLayout.CENTER);

        // Hoş geldin mesajı
        addBotMessage("Merhaba! 👋 Ben dil öğrenme asistanınım. İngilizce bir metin yazın, gramer ve yazım hatalarını kontrol edeyim.");

        // Alt panel — input alanı
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(ModernTheme.BG_PANEL);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        inputField = ModernTheme.createStyledTextField("Mesajınızı yazın...");
        inputField.setPreferredSize(new Dimension(0, 42));
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = ModernTheme.createStyledButton("Gönder  ➤", ModernTheme.ACCENT_PINK);
        sendButton.setPreferredSize(new Dimension(130, 42));
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Gönder butonuna aksiyon ekle
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Enter tuşuna basıldığında, send butonunu tıklat
        inputField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        if (!userMessage.isEmpty()) {
            addUserMessage(userMessage);
            inputField.setText("");

            // ChatGPT'den yanıt almak için yeni bir thread başlat
            new Thread(() -> {
                try {
                    String botResponse = getChatGptResponse(userMessage);
                    SwingUtilities.invokeLater(() -> addBotMessage(botResponse));
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> addBotMessage("⚠️ Hata: " + ex.getMessage()));
                }
            }).start();
        }
    }

    /** Kullanıcı mesaj balonu ekler (sağda, pembe arka plan). */
    private void addUserMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel bubble = createBubble(text, ModernTheme.ACCENT_PINK, Color.WHITE);
        row.add(bubble);

        chatPanel.add(row);
        chatPanel.revalidate();
        scrollToBottom();
    }

    /** Bot mesaj balonu ekler (solda, koyu arka plan). */
    private void addBotMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel avatar = new JLabel("🤖 ");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        row.add(avatar);

        JLabel bubble = createBubble(text, ModernTheme.BG_CARD, ModernTheme.TEXT_PRIMARY);
        row.add(bubble);

        chatPanel.add(row);
        chatPanel.revalidate();
        scrollToBottom();
    }

    /** Mesaj balonu oluşturur. */
    private JLabel createBubble(String text, Color bgColor, Color fgColor) {
        // HTML ile metin sarma
        String htmlText = "<html><body style='width: 280px; padding: 4px;'>" + text + "</body></html>";

        JLabel bubble = new JLabel(htmlText) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

                g2.dispose();
                super.paintComponent(g);
            }
        };
        bubble.setFont(ModernTheme.FONT_BODY);
        bubble.setForeground(fgColor);
        bubble.setOpaque(false);
        bubble.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return bubble;
    }

    /** Sohbeti en alta kaydırır. */
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    // ChatGPT'ye mesaj gönderip yanıt almayı sağlayan metod
    private static String getChatGptResponse(String message) throws Exception {
        // API çağrısı için JSON nesneleri oluşturuyoruz
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", "gpt-3.5-turbo"); // Model seçimi

        // Kullanıcı mesajını JSON formatına dönüştür
        ObjectNode userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", "Check the following text for grammar and spelling errors. Provide the corrected version and explain the errors in English and Turkish:\n" + message);

        requestBody.putArray("messages").add(userMessage); // Mesajları ekle
        requestBody.put("max_tokens", 200); // Maksimum token sayısı

        // HTTP client oluştur
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(System.getenv("CHATGPT_API_URL"))) // API URL'si
                .header("Content-Type", "application/json") // İçerik tipi
                .header("Authorization", "Bearer " + System.getenv("API_KEY")) // API Anahtarı
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody))) // POST isteği
                .build();

        // HTTP isteğini gönder ve yanıt al
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Eğer yanıt başarılı değilse hata mesajı döndür
        if (response.statusCode() != 200) {
            System.out.println("Response Body: " + response.body());
            throw new Exception("API request failed with status code: " + response.statusCode());
        }

        // Yanıt JSON'u işle
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode choices = rootNode.get("choices");

        // Yanıtı kontrol et ve mesajı al
        if (choices != null && choices.size() > 0) {
            JsonNode messageNode = choices.get(0).get("message");
            if (messageNode != null && messageNode.has("content")) {
                return messageNode.get("content").asText(); // Bot yanıtını döndür
            }
        }

        // Yanıt geçersizse hata
        throw new Exception("Invalid response format from ChatGPT.");
    }
}
