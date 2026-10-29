package livros.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Registo do desaparecimento de um livro (e da sua eventual recuperação).
 */
public class Desaparecimento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Livro livro;
    private final LocalDate data;
    private final String descricao;
    private LocalDate dataRecuperacao;

    public Desaparecimento(Livro livro, LocalDate data, String descricao) {
        this.livro = livro;
        this.data = data;
        this.descricao = descricao;
    }

    public boolean isRecuperado() {
        return dataRecuperacao != null;
    }

    void recuperar(LocalDate data) {
        this.dataRecuperacao = data;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataRecuperacao() {
        return dataRecuperacao;
    }

    @Override
    public String toString() {
        return livro.getTitulo() + " | desapareceu em " + Datas.formatar(data)
                + " | " + descricao
                + (isRecuperado() ? " | recuperado em " + Datas.formatar(dataRecuperacao) : "");
    }
}
