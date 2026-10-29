package livros.modelo;

import java.time.LocalDate;

/**
 * Um livro da coleção.
 */
public class Livro extends Item {
    private static final long serialVersionUID = 1L;

    private String autor;
    private String editora;
    private String isbn;
    private int anoPublicacao;
    private int numPaginas;
    private String idioma;

    public Livro(String titulo, String autor, String editora, String isbn, int anoPublicacao,
                 int numPaginas, String idioma, Categoria categoria, Localizacao localizacao,
                 LocalDate dataAquisicao, FormaAquisicao formaAquisicao, double preco) {
        super(titulo, categoria, localizacao, dataAquisicao, formaAquisicao, preco);
        this.autor = autor;
        this.editora = editora;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.numPaginas = numPaginas;
        this.idioma = idioma;
    }

    @Override
    public String getTipo() {
        return "Livro";
    }

    @Override
    public String getDescricaoDetalhada() {
        return super.getDescricaoDetalhada()
                + "Autor: " + autor + "\n"
                + "Editora: " + editora + "\n"
                + "ISBN: " + isbn + "\n"
                + "Ano de publicação: " + (anoPublicacao > 0 ? anoPublicacao : "-") + "\n"
                + "Nº de páginas: " + (numPaginas > 0 ? numPaginas : "-") + "\n"
                + "Idioma: " + idioma + "\n";
    }

    /** Verifica se o texto aparece no título, autor, editora ou ISBN (sem distinguir maiúsculas). */
    public boolean contemTexto(String texto) {
        String t = texto.toLowerCase();
        return getTitulo().toLowerCase().contains(t)
                || autor.toLowerCase().contains(t)
                || editora.toLowerCase().contains(t)
                || isbn.toLowerCase().contains(t);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + autor;
    }
}
