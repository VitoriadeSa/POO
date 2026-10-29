package livros.modelo;

import java.io.Serializable;

/**
 * Amigo ou conhecido a quem se podem emprestar livros.
 */
public class Amigo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String telefone;
    private String email;

    public Amigo(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nome;
    }
}
