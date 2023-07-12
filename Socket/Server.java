import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String args[])
    {
        try{
            ServerSocket serverSocket = new ServerSocket(5072);
            Socket client = serverSocket.accept();
            InputStream is = client.getInputStream();
            byte[] b = new byte[1024];
            int left = is.read(b);
            
            String s = new String(b);
            String ap = "";
            int i = 0;
            while(i < left)
            {
                ap = ap + s.charAt(left - 1 - i);
                i++;
            }
            OutputStream string = client.getOutputStream();
            string.write(ap.getBytes());
            string.close();
            is.close();
            client.close();
            serverSocket.close();
        }
        catch(IOException e)
        {
            System.out.println("Error");
        }

    }
}
