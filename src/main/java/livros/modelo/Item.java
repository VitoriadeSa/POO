package livros.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe abstrata com as características comuns a qualquer item da coleção:
 * identificação, categoria, localização, dados da aquisição e estado.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private Categoria categoria;
    private Localizacao localizacao;
    private LocalDate dataAquisicao;
    private FormaAquisicao formaAquisicao;
    private double preco;
    private EstadoItem estado;
    private String notas;

    public Item(String titulo, Categoria categoria, Localizacao localizacao,
                LocalDate dataAquisicao, FormaAquisicao formaAquisicao, double preco) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.dataAquisicao = dataAquisicao;
        this.formaAquisicao = formaAquisicao;
        this.preco = preco;
        this.estado = EstadoItem.DISPONIVEL;
        this.notas = "";
    }

    /** Nome do tipo de item (usado nas listagens). */
    public abstract String getTipo();

    /** Texto com todas as características do item. */
    public String getDescricaoDetalhada() {
        return "Nº: " + id + "\n"
                + "Tipo: " + getTipo() + "\n"
                + "Título: " + titulo + "\n"
                + "Categoria: " + (categoria == null ? "-" : categoria) + "\n"
                + "Localização: " + (localizacao == null ? "-" : localizacao) + "\n"
                + "Estado: " + estado + "\n"
                + "Data de aquisição: " + (dataAquisicao == null ? "-" : Datas.formatar(dataAquisicao)) + "\n"
                + "Forma de aquisição: " + (formaAquisicao == null ? "-" : formaAquisicao) + "\n"
                + String.format("Preço: %.2f €%n", preco)
                + (notas == null || notas.isBlank() ? "" : "Notas: " + notas + "\n");
    }

    public int getId() {
        return id;
    }

    /** O número é atribuído pela Biblioteca quando o item é adicionado. */
    void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public FormaAquisicao getFormaAquisicao() {
        return formaAquisicao;
    }

    public void setFormaAquisicao(FormaAquisicao formaAquisicao) {
        this.formaAquisicao = formaAquisicao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public EstadoItem getEstado() {
        return estado;
    }

    /** O estado só é alterado pela Biblioteca (empréstimos e desaparecimentos). */
    void setEstado(EstadoItem estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo;
    }
}
