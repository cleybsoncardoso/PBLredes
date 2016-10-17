
import java.rmi.*;

public class HelloServer {

    /**
     * Server program for the "Hello, world!" example.
     *
     * @param argv The command line arguments which are ignored.
     */
    public static void main(String[] argv) {
        try {
            Naming.rebind("Hello", new Hello("Hello, world!"));
            System.out.println("Servidor esta pronto!");
        } catch (Exception e) {
            System.out.println("Servidor falhou: " + e);
        }
    }
}
