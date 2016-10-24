
import java.rmi.*;

public class Server {

    /**
     * Server program for the "Hello, world!" example.
     *
     * @param argv The command line arguments which are ignored.
     */
    public static void main(String[] argv) {
        try {
            Naming.rebind("Hello", new Controller());
            System.out.println("Servidor esta pronto!");
        } catch (Exception e) {
            System.out.println("Servidor falhou: " + e);
        }
    }
}
