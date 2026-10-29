package livros.modelo;

/**
 * Exceção lançada quando uma operação sobre a biblioteca não é válida
 * (por exemplo, emprestar um livro que já está emprestado).
 */
public class BibliotecaException extends Exception {
    private static final long serialVersionUID = 1L;

    public BibliotecaException(String mensagem) {
        super(mensagem);
    }
}
