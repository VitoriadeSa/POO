package livros;

import java.io.Serializable;
import java.time.LocalDate;

public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Livro livro;
    private String amigo;
    private LocalDate data;
    private LocalDate dataLimite;
    private LocalDate dataDevolucao; // null enquanto não for devolvido

    public Emprestimo(Livro livro, String amigo, LocalDate data, LocalDate dataLimite) {
        this.livro = livro;
        this.amigo = amigo;
        this.data = data;
        this.dataLimite = dataLimite;
    }

    public Livro getLivro() { return livro; }
    public String getAmigo() { return amigo; }

    public boolean isAtivo() {
        return dataDevolucao == null;
    }

    public boolean isAtrasado() {
        return isAtivo() && LocalDate.now().isAfter(dataLimite);
    }

    public void devolver(LocalDate data) {
        dataDevolucao = data;
    }

    @Override
    public String toString() {
        String s = livro.getTitulo() + " -> " + amigo + " (" + data + " até " + dataLimite + ")";
        if (!isAtivo()) return s + " devolvido em " + dataDevolucao;
        if (isAtrasado()) return s + " ATRASADO";
        return s;
    }
}
