package livros.modelo;

/**
 * Estado em que um livro se encontra.
 */
public enum EstadoItem {
    DISPONIVEL("Disponível"),
    EMPRESTADO("Emprestado"),
    DESAPARECIDO("Desaparecido");

    private final String descricao;

    EstadoItem(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
