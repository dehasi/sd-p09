package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("::1", 8080);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o', '!', '\n'});
    }
}
