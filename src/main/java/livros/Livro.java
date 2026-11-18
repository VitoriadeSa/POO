package livros;

import java.io.Serializable;
import java.time.LocalDate;

public class Livro implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Estado { DISPONIVEL, EMPRESTADO, DESAPARECIDO }

    private String titulo;
    private String autor;
    private String categoria;
    private String localizacao;
    private LocalDate dataAquisicao;
    private double preco;
    private Estado estado = Estado.DISPONIVEL;

    public Livro(String titulo, String autor, String categoria, String localizacao,
                 LocalDate dataAquisicao, double preco) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.dataAquisicao = dataAquisicao;
        this.preco = preco;
    }

    public String getTipo() {
        return "Livro";
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getCategoria() { return categoria; }
    public String getLocalizacao() { return localizacao; }
    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public double getPreco() { return preco; }
    public Estado getEstado() { return estado; }

    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return getTipo() + ": " + titulo + " - " + autor + " [" + categoria + ", " + localizacao
                + ", adquirido em " + dataAquisicao + ", " + preco + " €] " + estado;
    }
}
