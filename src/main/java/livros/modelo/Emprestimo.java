package livros.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Empréstimo de um livro a um amigo.
 * Enquanto não tiver data de devolução, o empréstimo está ativo.
 */
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Livro livro;
    private final Amigo amigo;
    private final LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private boolean perdido;

    public Emprestimo(Livro livro, Amigo amigo, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        this.livro = livro;
        this.amigo = amigo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public boolean isAtivo() {
        return dataDevolucao == null;
    }

    public boolean isAtrasado(LocalDate hoje) {
        return isAtivo() && hoje.isAfter(dataPrevistaDevolucao);
    }

    /** Número de dias de atraso em relação à data prevista (0 se não estiver atrasado). */
    public long getDiasAtraso(LocalDate hoje) {
        if (!isAtrasado(hoje)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, hoje);
    }

    /** Termina o empréstimo. Se perdido for true, o livro não foi devolvido porque se perdeu. */
    void terminar(LocalDate data, boolean perdido) {
        this.dataDevolucao = data;
        this.perdido = perdido;
    }

    public String getSituacao(LocalDate hoje) {
        if (perdido) {
            return "Perdido";
        }
        if (!isAtivo()) {
            return "Devolvido";
        }
        if (isAtrasado(hoje)) {
            return "Atrasado (" + getDiasAtraso(hoje) + " dias)";
        }
        return "Ativo";
    }

    public Livro getLivro() {
        return livro;
    }

    public Amigo getAmigo() {
        return amigo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public boolean isPerdido() {
        return perdido;
    }

    @Override
    public String toString() {
        return livro.getTitulo() + " -> " + amigo.getNome()
                + " | " + Datas.formatar(dataEmprestimo)
                + " a " + Datas.formatar(dataPrevistaDevolucao)
                + " | " + getSituacao(LocalDate.now());
    }
}
