# 📘 LearnIng — Kelime Öğrenme Uygulaması

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?logo=openjdk&logoColor=white) ![Swing](https://img.shields.io/badge/UI-Java%20Swing-007396) ![OpenAI](https://img.shields.io/badge/OpenAI-API-412991?logo=openai&logoColor=white) ![Jackson](https://img.shields.io/badge/JSON-Jackson-D22128)

LearnIng, İngilizce–Türkçe kelime öğrenmeyi eğlenceli hale getirmek için geliştirilmiş bir Java Swing masaüstü uygulamasıdır. Uygulama; kelime eşleştirme oyunu, mini quiz, adam asmaca (hangman), favori kelimeler listesi, bir OpenAI destekli sohbet paneli ve hoş bir karşılama ekranı içerir.

## 🧩 Özellikler

### 1. WelcomePanel — Karşılama Ekranı
- Modern görünümlü bir açılış paneli
- Orta ekranda büyük "LearnIng" başlığı
- Lacivert tema, özel yazı tipi ve responsive yapı

### 2. WordMatch — Kelime Eşleştirme Oyunu
- İngilizce kelimeler ile Türkçe anlamlarını eşleştirmeye dayalı oyun
- Her eşleşmede anlık skor artışı
- Doğru eşleşmede yeşil, yanlışta kırmızı renk geri bildirimi
- Kelime anlamları her açılışta karıştırılır (shuffle)

### 3. WordQuiz — Mini Quiz
- Rastgele seçilen kelimenin doğru anlamını bulma oyunu
- Her soru için 2 şıklı cevap
- Doğru/yanlış popup bildirimleri
- Kelimeler "Favorilere Ekle" butonu ile favori listesine eklenebilir

### 4. HangmanWindow — Adam Asmaca
- Klasik adam asmaca mekaniğiyle kelime tahmin oyunu

### 5. WordsPanel — Favori Kelimeler
- Kullanıcının quiz ekranındaki kelimeleri kaydedebileceği panel
- Scrollable liste yapısı

### 6. ChatWindow — Sohbet Paneli
- OpenAI Chat Completions API'sine bağlanan basit bir sohbet arayüzü
- API anahtarı ortam değişkeninden okunur (bkz. Kurulum)

## 🛠️ Kullanılan Teknolojiler

| Teknoloji | Açıklama |
|---|---|
| Java 8+ | Ana geliştirme dili |
| Swing | Arayüz bileşenleri |
| AWT | Renk, font, mouse event işleme |
| Jackson (databind) | OpenAI API yanıtlarının JSON parse işlemi |
| java.net.http | HTTP istekleri (OpenAI API çağrıları) |
| OOP Tasarımı | Panel bazlı modüler mimari |

Her panel ayrı bir sınıf olarak tasarlanmıştır ve kolay genişletilebilir.

## ▶️ Kurulum ve Çalıştırma

1. Projeyi bir IDE'ye (IntelliJ / Eclipse / NetBeans) ekleyin.
2. Chat paneli için ortam değişkenlerini tanımlayın:
   ```
   API_KEY=<openai-api-anahtarınız>
   CHATGPT_API_URL=https://api.openai.com/v1/chat/completions
   ```
3. Main sınıfını oluşturarak panelleri bir `JFrame` içine ekleyin:
   ```java
   public class Main {
       public static void main(String[] args) {
           JFrame frame = new JFrame("LearnIng App");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setSize(600, 600);
           frame.add(new WelcomePanel());
           frame.setVisible(true);
       }
   }
   ```
4. Uygulama başarılı şekilde açılacaktır.

## 📌 Not

Bu proje bir üniversite dersi kapsamında geliştirilmeye başlanmış, geliştirme süreci bireysel olarak yürütülmüştür.
