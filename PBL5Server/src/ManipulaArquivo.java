
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class ManipulaArquivo {

    public ManipulaArquivo() {

    }

    public void criaArquivo(String nome) {

        //seria bom ver se ja existe arquivo com esse nome pra nao sobreescrever arquivo dos outros
        FileWriter arquivo;

        try {
            arquivo = new FileWriter(new File("repositorio/" + nome + ".txt"));
            arquivo.close();
            System.out.println("Criado arquivo " + nome);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String abrirArquivo(String nome) {

        String texto = "";

        FileReader fr;

        try {
            fr = new FileReader("repositorio/" + nome + ".txt");
            //construtor que recebe o objeto do tipo FileReader
            BufferedReader br = new BufferedReader(fr);
            //equanto houver mais linhas
            while (br.ready()) {
                //lê a proxima linha
                String linha = br.readLine();
                
                texto += linha;
            }
            br.close();
            fr.close();
            
            return texto;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManipulaArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return texto;

    }

}
