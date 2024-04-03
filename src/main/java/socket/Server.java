package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.stream.Collectors.joining;

class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket socket = server.accept();
            try {
                process(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void process(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
socket.close();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


        while (true) {
            writer.write("> ");
            writer.flush();
            String input = reader.readLine();

            if (input.startsWith("cat ")) {
                String path = input.substring(4).trim();
                String content = Files.lines(Path.of(path)).collect(joining("\n"));
                writer.write(content);

            } else if (input.startsWith("lc ")) {
                String path = input.substring(3).trim();
                int size = Files.lines(Path.of(path)).toList().size();
                writer.write(String.valueOf(size));

            } else if (input.startsWith("help")) {
                writer.write("cat\nhelp\nlc");
            } else if (input.startsWith("exit")) {
                break;
            } else {
                writer.write("Unknown command, please use 'help'");
            }
            writer.newLine();
            writer.flush();
        }

        socket.close();
    }
}
