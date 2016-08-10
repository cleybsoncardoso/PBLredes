package ldserver;

import java.io.Serializable;

class Usuario implements Serializable {

    private String login;
    private String senha;
    transient private boolean logado;
    private boolean upload;

    public boolean isUpload() {
        return upload;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public Usuario(String login, String senha, boolean upload) {
        this.login = login;
        this.senha = senha;
        this.upload = upload;

    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    @Override
    public boolean equals(Object outro) {
        if (outro instanceof Usuario) {
            Usuario outroU = (Usuario) outro;
            return this.login.equals(outroU.getLogin()) && this.senha.equals(outroU.getSenha());
        }
        return false;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

}
