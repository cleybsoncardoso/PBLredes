
import java.rmi.*;
import java.util.Scanner;

public class HelloClient {

    /**
     * Client program for the "Hello, world!" example.
     *
     * @param argv The command line arguments which are ignored.
     */
    public static void main(String[] argv) {
        try {
            HelloInterface hello = (HelloInterface) Naming.lookup("rmi://127.0.0.1/Hello");
            //System.out.println(hello.say());
            System.out.println("Bem vindo\n");
            System.out.println("Lista de arquivos:\n\n");
            System.out.println(hello.listarArquivos());
            System.out.print("\n\nAbrir arquivo com nome: ");
            Scanner ler = new Scanner(System.in);
            String nomedoarquivo = ler.nextLine();
            System.out.println(hello.abrirArquivo(nomedoarquivo));
            
            
            
            
            
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e);
        }

    }
}
