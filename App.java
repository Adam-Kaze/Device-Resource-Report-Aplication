public class App {
    public static void main(String[] args) {
        Config config = new Config(); // Buat objek Config
        UI ui = new UI(config); // Sediakan objek Config saat membuat objek UI
        ui.show();
    }
}