import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String s = "";
    public static void main(String args[])
    {

            try{
                Socket socket = new Socket("52.231.74.241", 5072);
                try
                {
                    Scanner sc = new Scanner(System.in);
                    OutputStream os = socket.getOutputStream();

                    System.out.print("Type: ");
                    s = sc.nextLine();
                    os.write(s.getBytes());
                    sc.close();
                    os.close();
                }
                catch(Exception e)
                {
                    System.out.println("socket is not initialized");
                    System.exit(-1);
                }
                try
                {
                    InputStream is = socket.getInputStream();
                    byte[] b = new byte[1024];
                    is.read(b);
                    s = new String(b);
                    System.out.println("Result: " + s);
                    is.close();
                }
                catch(IOException e)
                {
                    System.out.println("Can't get the Result");
                    System.exit(-1);
                }
                socket.close();
            }
            catch(SocketException e)
            {
                System.out.println("Could not connect to the server");
                System.exit(-1);
            }

    }
}