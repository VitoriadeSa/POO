package livros.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe principal do modelo: guarda todos os livros, categorias, localizações,
 * amigos, empréstimos e desaparecimentos, e implementa as operações sobre eles.
 * É este objeto que é gravado em ficheiro.
 */
public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Livro> livros = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();
    private final List<Localizacao> localizacoes = new ArrayList<>();
    private final List<Amigo> amigos = new ArrayList<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private final List<Desaparecimento> desaparecimentos = new ArrayList<>();
    private int proximoId = 1;

    // ------------------------------------------------------------------ Livros

    /** Regista a aquisição de um novo livro, atribuindo-lhe um número. */
    public void adicionarLivro(Livro livro) throws BibliotecaException {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new BibliotecaException("O livro tem de ter título.");
        }
        if (livro.getDataAquisicao() != null && livro.getDataAquisicao().isAfter(LocalDate.now())) {
            throw new BibliotecaException("A data de aquisição não pode ser no futuro.");
        }
        livro.setId(proximoId++);
        livros.add(livro);
    }

    /** Remove definitivamente um livro (por exemplo, se foi vendido ou oferecido). */
    public void removerLivro(Livro livro) throws BibliotecaException {
        if (livro.getEstado() == EstadoItem.EMPRESTADO) {
            throw new BibliotecaException("Não é possível remover um livro que está emprestado.");
        }
        livros.remove(livro);
        emprestimos.removeIf(e -> e.getLivro() == livro);
        desaparecimentos.removeIf(d -> d.getLivro() == livro);
    }

    public Livro procurarLivro(int id) {
        for (Livro l : livros) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    /**
     * Pesquisa livros. Qualquer critério a null (ou texto vazio) é ignorado.
     */
    public List<Livro> pesquisar(String texto, Categoria categoria, Localizacao localizacao, EstadoItem estado) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro l : livros) {
            if (texto != null && !texto.isBlank() && !l.contemTexto(texto.trim())) {
                continue;
            }
            if (categoria != null && l.getCategoria() != categoria) {
                continue;
            }
            if (localizacao != null && l.getLocalizacao() != localizacao) {
                continue;
            }
            if (estado != null && l.getEstado() != estado) {
                continue;
            }
            resultado.add(l);
        }
        return resultado;
    }

    /** Livros adquiridos entre duas datas (inclusive), ordenados por data. */
    public List<Livro> getAquisicoesEntre(LocalDate inicio, LocalDate fim) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro l : livros) {
            LocalDate d = l.getDataAquisicao();
            if (d != null && !d.isBefore(inicio) && !d.isAfter(fim)) {
                resultado.add(l);
            }
        }
        resultado.sort((a, b) -> a.getDataAquisicao().compareTo(b.getDataAquisicao()));
        return resultado;
    }

    // ------------------------------------------------------------- Categorias

    public Categoria adicionarCategoria(String nome, String descricao) throws BibliotecaException {
        if (nome == null || nome.isBlank()) {
            throw new BibliotecaException("A categoria tem de ter nome.");
        }
        if (procurarCategoria(nome) != null) {
            throw new BibliotecaException("Já existe a categoria \"" + nome + "\".");
        }
        Categoria c = new Categoria(nome.trim(), descricao);
        categorias.add(c);
        return c;
    }

    public Categoria procurarCategoria(String nome) {
        for (Categoria c : categorias) {
            if (c.getNome().equalsIgnoreCase(nome.trim())) {
                return c;
            }
        }
        return null;
    }

    public void removerCategoria(Categoria categoria) throws BibliotecaException {
        for (Livro l : livros) {
            if (l.getCategoria() == categoria) {
                throw new BibliotecaException("A categoria está a ser usada pelo livro " + l + ".");
            }
        }
        categorias.remove(categoria);
    }

    // ---------------------------------------------------------- Localizações

    public Localizacao adicionarLocalizacao(String divisao, String estante, int prateleira) throws BibliotecaException {
        if (divisao == null || divisao.isBlank() || estante == null || estante.isBlank()) {
            throw new BibliotecaException("Indique a divisão e a estante.");
        }
        if (prateleira < 1) {
            throw new BibliotecaException("A prateleira tem de ser um número positivo.");
        }
        Localizacao nova = new Localizacao(divisao.trim(), estante.trim(), prateleira);
        for (Localizacao loc : localizacoes) {
            if (loc.mesmoLocal(nova)) {
                throw new BibliotecaException("Essa localização já existe.");
            }
        }
        localizacoes.add(nova);
        return nova;
    }

    public void removerLocalizacao(Localizacao localizacao) throws BibliotecaException {
        for (Livro l : livros) {
            if (l.getLocalizacao() == localizacao) {
                throw new BibliotecaException("A localização está a ser usada pelo livro " + l + ".");
            }
        }
        localizacoes.remove(localizacao);
    }

    // ----------------------------------------------------------------- Amigos

    public Amigo adicionarAmigo(String nome, String telefone, String email) throws BibliotecaException {
        if (nome == null || nome.isBlank()) {
            throw new BibliotecaException("O amigo tem de ter nome.");
        }
        Amigo a = new Amigo(nome.trim(), telefone, email);
        amigos.add(a);
        return a;
    }

    public void removerAmigo(Amigo amigo) throws BibliotecaException {
        for (Emprestimo e : emprestimos) {
            if (e.getAmigo() == amigo && e.isAtivo()) {
                throw new BibliotecaException(amigo.getNome() + " ainda tem livros emprestados.");
            }
        }
        amigos.remove(amigo);
        emprestimos.removeIf(e -> e.getAmigo() == amigo);
    }

    // ------------------------------------------------------------ Empréstimos

    public Emprestimo emprestar(Livro livro, Amigo amigo, LocalDate data, LocalDate dataPrevista)
            throws BibliotecaException {
        if (livro == null || amigo == null || data == null || dataPrevista == null) {
            throw new BibliotecaException("Faltam dados para registar o empréstimo.");
        }
        if (livro.getEstado() != EstadoItem.DISPONIVEL) {
            throw new BibliotecaException("O livro não está disponível (" + livro.getEstado() + ").");
        }
        if (dataPrevista.isBefore(data)) {
            throw new BibliotecaException("A data prevista de devolução é anterior à data do empréstimo.");
        }
        Emprestimo e = new Emprestimo(livro, amigo, data, dataPrevista);
        emprestimos.add(e);
        livro.setEstado(EstadoItem.EMPRESTADO);
        return e;
    }

    public void devolver(Emprestimo emprestimo, LocalDate data) throws BibliotecaException {
        if (!emprestimo.isAtivo()) {
            throw new BibliotecaException("Esse empréstimo já terminou.");
        }
        if (data.isBefore(emprestimo.getDataEmprestimo())) {
            throw new BibliotecaException("A data de devolução é anterior à data do empréstimo.");
        }
        emprestimo.terminar(data, false);
        emprestimo.getLivro().setEstado(EstadoItem.DISPONIVEL);
    }

    public Emprestimo getEmprestimoAtivo(Livro livro) {
        for (Emprestimo e : emprestimos) {
            if (e.getLivro() == livro && e.isAtivo()) {
                return e;
            }
        }
        return null;
    }

    public List<Emprestimo> getEmprestimosAtivos() {
        List<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.isAtivo()) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprestimo> getEmprestimosAtrasados(LocalDate hoje) {
        List<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.isAtrasado(hoje)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprestimo> getEmprestimosDe(Amigo amigo) {
        List<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.getAmigo() == amigo) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprestimo> getEmprestimosDoLivro(Livro livro) {
        List<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.getLivro() == livro) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    // ------------------------------------------------------- Desaparecimentos

    /**
     * Regista que um livro desapareceu. Se estava emprestado, o empréstimo
     * termina e fica marcado como perdido.
     */
    public Desaparecimento registarDesaparecimento(Livro livro, LocalDate data, String descricao)
            throws BibliotecaException {
        if (livro.getEstado() == EstadoItem.DESAPARECIDO) {
            throw new BibliotecaException("O livro já está registado como desaparecido.");
        }
        Emprestimo ativo = getEmprestimoAtivo(livro);
        if (ativo != null) {
            ativo.terminar(data, true);
        }
        Desaparecimento d = new Desaparecimento(livro, data, descricao);
        desaparecimentos.add(d);
        livro.setEstado(EstadoItem.DESAPARECIDO);
        return d;
    }

    public void registarRecuperacao(Livro livro, LocalDate data) throws BibliotecaException {
        if (livro.getEstado() != EstadoItem.DESAPARECIDO) {
            throw new BibliotecaException("O livro não está desaparecido.");
        }
        for (Desaparecimento d : desaparecimentos) {
            if (d.getLivro() == livro && !d.isRecuperado()) {
                if (data.isBefore(d.getData())) {
                    throw new BibliotecaException("A data de recuperação é anterior ao desaparecimento.");
                }
                d.recuperar(data);
            }
        }
        livro.setEstado(EstadoItem.DISPONIVEL);
    }

    // ------------------------------------------------------------ Estatísticas

    public Map<String, Integer> getNumLivrosPorCategoria() {
        Map<String, Integer> mapa = new TreeMap<>();
        for (Livro l : livros) {
            String nome = l.getCategoria() == null ? "(sem categoria)" : l.getCategoria().getNome();
            mapa.put(nome, mapa.getOrDefault(nome, 0) + 1);
        }
        return mapa;
    }

    public Map<Integer, Integer> getAquisicoesPorAno() {
        Map<Integer, Integer> mapa = new TreeMap<>();
        for (Livro l : livros) {
            if (l.getDataAquisicao() != null) {
                int ano = l.getDataAquisicao().getYear();
                mapa.put(ano, mapa.getOrDefault(ano, 0) + 1);
            }
        }
        return mapa;
    }

    public int contarPorEstado(EstadoItem estado) {
        int n = 0;
        for (Livro l : livros) {
            if (l.getEstado() == estado) {
                n++;
            }
        }
        return n;
    }

    public double getValorTotal() {
        double total = 0;
        for (Livro l : livros) {
            if (l.getEstado() != EstadoItem.DESAPARECIDO) {
                total += l.getPreco();
            }
        }
        return total;
    }

    /** Amigo com mais empréstimos registados (ou null se ainda não houver empréstimos). */
    public Amigo getAmigoComMaisEmprestimos() {
        Amigo melhor = null;
        int max = 0;
        for (Amigo a : amigos) {
            int n = getEmprestimosDe(a).size();
            if (n > max) {
                max = n;
                melhor = a;
            }
        }
        return melhor;
    }

    public String getResumoEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total de livros: ").append(livros.size()).append("\n");
        sb.append("  Disponíveis: ").append(contarPorEstado(EstadoItem.DISPONIVEL)).append("\n");
        sb.append("  Emprestados: ").append(contarPorEstado(EstadoItem.EMPRESTADO)).append("\n");
        sb.append("  Desaparecidos: ").append(contarPorEstado(EstadoItem.DESAPARECIDO)).append("\n");
        sb.append(String.format("Valor da coleção: %.2f €%n", getValorTotal()));
        sb.append("Empréstimos atrasados: ").append(getEmprestimosAtrasados(LocalDate.now()).size()).append("\n");
        Amigo a = getAmigoComMaisEmprestimos();
        if (a != null) {
            sb.append("Amigo com mais empréstimos: ").append(a.getNome()).append("\n");
        }
        sb.append("\nLivros por categoria:\n");
        for (Map.Entry<String, Integer> e : getNumLivrosPorCategoria().entrySet()) {
            sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        sb.append("\nAquisições por ano:\n");
        for (Map.Entry<Integer, Integer> e : getAquisicoesPorAno().entrySet()) {
            sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ Getters
    // Devolvem listas só de leitura para que as alterações passem pelos métodos acima.

    public List<Livro> getLivros() {
        return Collections.unmodifiableList(livros);
    }

    public List<Categoria> getCategorias() {
        return Collections.unmodifiableList(categorias);
    }

    public List<Localizacao> getLocalizacoes() {
        return Collections.unmodifiableList(localizacoes);
    }

    public List<Amigo> getAmigos() {
        return Collections.unmodifiableList(amigos);
    }

    public List<Emprestimo> getEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    public List<Desaparecimento> getDesaparecimentos() {
        return Collections.unmodifiableList(desaparecimentos);
    }
}
