
import java.io.File;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

public class Hello extends UnicastRemoteObject implements HelloInterface {

    private String message;
    private ManipulaArquivo arquivo;

    public Hello(String msg) throws RemoteException {
        arquivo = new ManipulaArquivo();
        message = msg;
    }

    @Override
    public String say() throws RemoteException {
        return message;
    }

    /**
     * Método responsável por criar o arquivo no servidor.
     *
     * @param nome
     * @throws java.rmi.RemoteException
     */
    @Override
    public void criarArquivo(String nome) throws RemoteException {
        arquivo.criaArquivo(nome);
    }

    /**
     * Método responsável por listar arquivos disponíveis no servidor.
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public String listarArquivos() throws RemoteException {
        String lista = "";
        File file = new File(System.getProperty("user.dir") + "/repositorio");
        File afile[] = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            lista += arquivos.getName() + "\n";
            System.out.println(arquivos.getName());
        }
        return lista;
    }

    @Override
    public String abrirArquivo(String nome) throws RemoteException {
        return arquivo.abrirArquivo(nome);
    }
}
