package hw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

class CatServer {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) System.err.println("Usage: java CatServer.java port");

        int port = Integer.valueOf(args[0]);
        ServerSocket server = new ServerSocket(port);
        System.out.println("Listening port: " + port);
        while (true) {
            Socket socket = server.accept();
            processInput(socket);
        }
    }

    private static void processInput(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        try {
            String path = reader.readLine().trim();
            System.out.println("Got " + path);

            Files.lines(Path.of(path)).forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Writing finished");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("Error");
            writer.newLine();
        }
        writer.flush();
        socket.close();
    }
}
