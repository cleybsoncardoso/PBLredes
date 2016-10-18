
import java.rmi.*;
import java.util.ArrayList;

/**
 * Remote Interface for the "Hello, world!" example.
 */
public interface HelloInterface extends Remote {

    /**
     * Remotely invocable method.
     *
     * @return the message of the remote object, such as "Hello, world!".
     * @exception RemoteException if the remote invocation fails.
     */
    public String say() throws RemoteException;

    public void criarArquivo(String nome) throws RemoteException;

    public String listarArquivos() throws RemoteException;

    public String abrirArquivo(String nome) throws RemoteException;
}
