package hw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.lang.Integer.parseInt;

// java Gateway.java 8080 & java CatServer.java 8081 &
// java Gateway.java 8888 cat localhost:8080,localhost:8081 lc localhost:9090 &
class Gateway {

    record CommandServer(String host, int port) {}

    static Map<String, List<CommandServer>> commandServers;

    public static void main(String[] args) throws IOException {
        if (args.length <= 5)
            System.err.println("Usage: java Gateway.java port command host:port,host:port command2 host:port,host:port");

        commandServers = parseServers(args);

        int port = parseInt(args[0]);
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
            String cmd = reader.readLine().trim();
            List<String> split = List.of(cmd.split("\\s+"));
            System.err.println("got: " + split);
            var command = split.get(0);
            var path = split.get(1);
            var server = some(commandServers.get(command));

            var client = new Socket(server.host(), server.port());
            var out = new PrintWriter(client.getOutputStream(), true);
            var in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println(path);

            in.lines().forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            out.close();
            in.close();
            client.close();
            System.out.println("Writing finished");
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("Error");
            writer.newLine();
        }
        writer.flush();
        socket.close();
    }

    private static Map<String, List<CommandServer>> parseServers(String[] args) {
        Map<String, List<CommandServer>> commandServers = new HashMap<>();
        for (int i = 2; i < args.length; i += 2) {
            var cmd = args[i - 1];
            List<CommandServer> servers = Arrays.stream(args[i].split(","))
                    .map(hp -> {
                        String[] split = hp.split(":");
                        return new CommandServer(split[0], parseInt(split[1]));
                    }).toList();
            commandServers.put(cmd, servers);
        }
        return commandServers;
    }

    private static CommandServer some(List<CommandServer> commandServers) {
        Random rand = new Random();
        return commandServers.get(rand.nextInt(commandServers.size()));
    }
}
