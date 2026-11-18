package livros;

import java.time.LocalDate;

public class BandaDesenhada extends Livro {
    private static final long serialVersionUID = 1L;

    private String ilustrador;

    public BandaDesenhada(String titulo, String autor, String ilustrador, String categoria,
                          String localizacao, LocalDate dataAquisicao, double preco) {
        super(titulo, autor, categoria, localizacao, dataAquisicao, preco);
        this.ilustrador = ilustrador;
    }

    public String getIlustrador() { return ilustrador; }

    @Override
    public String getTipo() {
        return "BD";
    }

    @Override
    public String toString() {
        return super.toString() + " (ilustrador: " + ilustrador + ")";
    }
}
