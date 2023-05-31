
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
;
public class Main {

    private static final int PORT = 8989;

            public static void main(String[] args) throws Exception {


                try (ServerSocket serverSocket = new ServerSocket(PORT);) {
                    BooleanSearchEngine searchEngine = new BooleanSearchEngine(new File("pdfs"));
                    Gson gson = new GsonBuilder().create();

                    while (true) {
                        try (
                                Socket clientSocket = serverSocket.accept();
                                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        ) {
                            String word = in.readLine();

                            var json = gson.toJson(searchEngine.search(word));
                            out.println(json);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }


