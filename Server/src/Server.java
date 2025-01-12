import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static final int PORT = 12345;
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SendMessageTask(), 0, 8000);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент подключен: " + clientSocket);

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clients.add(writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class SendMessageTask extends TimerTask {
        private int messageCount = 0;

        @Override
        public void run() {
            messageCount++;
            String message = "Сообщение от сервера " + messageCount;
            for (PrintWriter client : clients) {
                client.println(message);
            }
            System.out.println("Сообщение отправлено всем клиентам: " + message);
        }
    }
}