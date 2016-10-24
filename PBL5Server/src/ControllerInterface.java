
import java.rmi.*;

/**
 * Remote Interface for the "Hello, world!" example.
 */
public interface ControllerInterface extends Remote {

    /**
     * Remotely invocable method.
     *
     * @param text
     * @exception RemoteException if the remote invocation fails.
     */
    public void setText(String text) throws RemoteException;

    public String getText() throws RemoteException;
    
    public void criarArquivo(String nome) throws RemoteException;

    public String[] listarArquivos() throws RemoteException;
    
    public String abrirArquivo(String nome) throws RemoteException;
    
    public void editarArquivo(String nome, String texto) throws RemoteException;
    
    public void insert(char c, int pos) throws RemoteException;
}
