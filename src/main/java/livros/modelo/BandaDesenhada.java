package livros.modelo;

import java.time.LocalDate;

/**
 * Banda desenhada: é um livro com ilustrador e que pode pertencer a uma série.
 */
public class BandaDesenhada extends Livro {
    private static final long serialVersionUID = 1L;

    private String ilustrador;
    private String serie;
    private int volume;

    public BandaDesenhada(String titulo, String autor, String ilustrador, String serie, int volume,
                          String editora, String isbn, int anoPublicacao, int numPaginas, String idioma,
                          Categoria categoria, Localizacao localizacao,
                          LocalDate dataAquisicao, FormaAquisicao formaAquisicao, double preco) {
        super(titulo, autor, editora, isbn, anoPublicacao, numPaginas, idioma,
                categoria, localizacao, dataAquisicao, formaAquisicao, preco);
        this.ilustrador = ilustrador;
        this.serie = serie;
        this.volume = volume;
    }

    @Override
    public String getTipo() {
        return "Banda desenhada";
    }

    @Override
    public String getDescricaoDetalhada() {
        return super.getDescricaoDetalhada()
                + "Ilustrador: " + ilustrador + "\n"
                + "Série: " + (serie == null || serie.isBlank() ? "-" : serie + " (vol. " + volume + ")") + "\n";
    }

    @Override
    public boolean contemTexto(String texto) {
        String t = texto.toLowerCase();
        return super.contemTexto(texto)
                || ilustrador.toLowerCase().contains(t)
                || serie.toLowerCase().contains(t);
    }

    public String getIlustrador() {
        return ilustrador;
    }

    public void setIlustrador(String ilustrador) {
        this.ilustrador = ilustrador;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
