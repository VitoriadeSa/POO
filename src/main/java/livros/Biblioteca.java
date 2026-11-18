package livros;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final File FICHEIRO = new File("biblioteca.dat");

    private ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    public ArrayList<Livro> getLivros() { return livros; }
    public ArrayList<Emprestimo> getEmprestimos() { return emprestimos; }

    // ---------- Livros (aquisição e remoção) ----------

    public void adicionar(Livro livro) {
        if (livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O livro tem de ter título.");
        }
        livros.add(livro);
    }

    public void remover(Livro livro) {
        if (livro.getEstado() == Livro.Estado.EMPRESTADO) {
            throw new IllegalArgumentException("Não pode remover um livro emprestado.");
        }
        livros.remove(livro);
    }

    /** Procura pelo título, autor ou categoria. */
    public ArrayList<Livro> pesquisar(String texto) {
        ArrayList<Livro> resultado = new ArrayList<>();
        String t = texto.toLowerCase();
        for (Livro l : livros) {
            if (l.getTitulo().toLowerCase().contains(t)
                    || l.getAutor().toLowerCase().contains(t)
                    || l.getCategoria().toLowerCase().contains(t)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    // ---------- Empréstimos ----------

    public void emprestar(Livro livro, String amigo, LocalDate dataLimite) {
        if (livro.getEstado() != Livro.Estado.DISPONIVEL) {
            throw new IllegalArgumentException("O livro não está disponível.");
        }
        if (amigo.isBlank()) {
            throw new IllegalArgumentException("Indique o nome do amigo.");
        }
        if (dataLimite == null || dataLimite.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data limite não pode ser antes de hoje.");
        }
        emprestimos.add(new Emprestimo(livro, amigo, LocalDate.now(), dataLimite));
        livro.setEstado(Livro.Estado.EMPRESTADO);
    }

    public void devolver(Livro livro) {
        for (Emprestimo e : emprestimos) {
            if (e.getLivro() == livro && e.isAtivo()) {
                e.devolver(LocalDate.now());
                livro.setEstado(Livro.Estado.DISPONIVEL);
                return;
            }
        }
        throw new IllegalArgumentException("O livro não está emprestado.");
    }

    // ---------- Desaparecimentos ----------

    public void desaparecer(Livro livro) {
        if (livro.getEstado() == Livro.Estado.EMPRESTADO) {
            devolver(livro); // o empréstimo termina (o amigo perdeu-o)
        }
        livro.setEstado(Livro.Estado.DESAPARECIDO);
    }

    public void recuperar(Livro livro) {
        if (livro.getEstado() != Livro.Estado.DESAPARECIDO) {
            throw new IllegalArgumentException("O livro não está desaparecido.");
        }
        livro.setEstado(Livro.Estado.DISPONIVEL);
    }

    // ---------- Estatísticas ----------

    public String estatisticas() {
        int emprestados = 0, desaparecidos = 0, atrasados = 0;
        double valor = 0;
        for (Livro l : livros) {
            if (l.getEstado() == Livro.Estado.EMPRESTADO) emprestados++;
            if (l.getEstado() == Livro.Estado.DESAPARECIDO) desaparecidos++;
            valor += l.getPreco();
        }
        for (Emprestimo e : emprestimos) {
            if (e.isAtrasado()) atrasados++;
        }
        return "Livros: " + livros.size() + " | Emprestados: " + emprestados
                + " | Atrasados: " + atrasados + " | Desaparecidos: " + desaparecidos
                + " | Valor: " + valor + " €";
    }

    // ---------- Persistência (ObjectStreams) ----------

    public void guardar() throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHEIRO))) {
            out.writeObject(this);
        }
    }

    public static Biblioteca carregar() throws Exception {
        if (!FICHEIRO.exists()) {
            return new Biblioteca();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FICHEIRO))) {
            return (Biblioteca) in.readObject();
        }
    }
}
