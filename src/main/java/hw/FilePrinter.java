package hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

// file: /Users/ravil/txt.csv
// This NOT the solution of the homework =P
class FilePrinter {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            String input = reader.readLine();

            if (input.startsWith("cat ")) {
                String path = input.substring(4).trim();
                Files.lines(Path.of(path)).toList().forEach(System.out::println);

            } else if (input.startsWith("lc ")) {
                String path = input.substring(3).trim();
                System.out.println(Files.lines(Path.of(path)).toList().size());

            } else if (input.startsWith("help")) {
                System.out.println("cat\nhelp\nlc");
            } else if (input.startsWith("exit")) {
                break;
            } else {
                System.err.println("Unknown command, please use 'help'");
            }
        }
    }
}
