package livros.modelo;

/**
 * Forma como um livro foi adquirido.
 */
public enum FormaAquisicao {
    COMPRA("Compra"),
    OFERTA("Oferta"),
    TROCA("Troca"),
    HERANCA("Herança");

    private final String descricao;

    FormaAquisicao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
