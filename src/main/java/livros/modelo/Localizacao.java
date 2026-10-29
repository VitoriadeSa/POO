package livros.modelo;

import java.io.Serializable;

/**
 * Local onde um livro está guardado: divisão da casa, estante e prateleira.
 */
public class Localizacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String divisao;
    private String estante;
    private int prateleira;

    public Localizacao(String divisao, String estante, int prateleira) {
        this.divisao = divisao;
        this.estante = estante;
        this.prateleira = prateleira;
    }

    public String getDivisao() {
        return divisao;
    }

    public void setDivisao(String divisao) {
        this.divisao = divisao;
    }

    public String getEstante() {
        return estante;
    }

    public void setEstante(String estante) {
        this.estante = estante;
    }

    public int getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(int prateleira) {
        this.prateleira = prateleira;
    }

    /** Duas localizações são iguais se tiverem a mesma divisão, estante e prateleira. */
    public boolean mesmoLocal(Localizacao outra) {
        return divisao.equalsIgnoreCase(outra.divisao)
                && estante.equalsIgnoreCase(outra.estante)
                && prateleira == outra.prateleira;
    }

    @Override
    public String toString() {
        return divisao + " / " + estante + " / prat. " + prateleira;
    }
}
