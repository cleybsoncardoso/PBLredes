
import java.io.File;
import java.rmi.*;
import java.rmi.server.*;

public class Controller extends UnicastRemoteObject implements ControllerInterface {

    private String texto;
    private ManipulaArquivo arquivo;

    public Controller() throws RemoteException {
        arquivo = new ManipulaArquivo();
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
    public String[] listarArquivos() throws RemoteException {
        File file = new File(System.getProperty("user.dir") + "/repositorio");
        File afile[] = file.listFiles();
        String[] lista = new String[afile.length];
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivo = afile[i];
            lista[i] = arquivo.getName();
        }
        return lista;
    }

    @Override
    public String abrirArquivo(String nome) throws RemoteException {
        this.texto = arquivo.abrirArquivo(nome);
        return this.texto;
    }

    @Override
    public void insert(char c, int pos) throws RemoteException {
        System.out.println(pos);
        if (pos == 0) {
            this.texto = c + this.texto;
        }
        if (this.texto.length() <= pos) {
            this.texto = this.texto + c;
        } else {
            String aux = this.texto.substring(pos);
            System.out.println(this.texto);
            this.texto = this.texto.replaceFirst(aux, (c + aux));

        }

        System.out.println("Texto atual: " + this.texto);

    }

    @Override
    public void editarArquivo(String nome, String texto) throws RemoteException {
        arquivo.editarArquivo(nome, texto);
    }

    @Override
    public void setText(String text) throws RemoteException {
        this.texto = text;
    }

    @Override
    public String getText() throws RemoteException {
        return this.texto;
    }
}
