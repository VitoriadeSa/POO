package livros.fx;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import livros.modelo.Amigo;
import livros.modelo.Biblioteca;
import livros.modelo.BibliotecaException;
import livros.modelo.Categoria;
import livros.modelo.Datas;
import livros.modelo.Desaparecimento;
import livros.modelo.Emprestimo;
import livros.modelo.EstadoItem;
import livros.modelo.Livro;
import livros.modelo.Localizacao;
import livros.persistencia.Persistencia;

/**
 * Controlador da janela principal (Principal.fxml).
 * Depois de cada alteração os dados são gravados e as tabelas são atualizadas.
 */
public class PrincipalController {

    private Biblioteca biblioteca;

    @FXML private TabPane tabPane;

    // Livros
    @FXML private TextField txtPesquisa;
    @FXML private ComboBox<Categoria> cbFiltroCategoria;
    @FXML private ComboBox<Localizacao> cbFiltroLocalizacao;
    @FXML private ComboBox<EstadoItem> cbFiltroEstado;
    @FXML private TableView<Livro> tabelaLivros;
    @FXML private TableColumn<Livro, Integer> colId;
    @FXML private TableColumn<Livro, String> colTipo;
    @FXML private TableColumn<Livro, String> colTitulo;
    @FXML private TableColumn<Livro, String> colAutor;
    @FXML private TableColumn<Livro, String> colCategoria;
    @FXML private TableColumn<Livro, String> colLocalizacao;
    @FXML private TableColumn<Livro, EstadoItem> colEstado;
    @FXML private TableColumn<Livro, LocalDate> colDataAquisicao;
    @FXML private TextArea txtDetalhes;
    @FXML private Label lblContagem;

    // Empréstimos
    @FXML private CheckBox chkSoAtivos;
    @FXML private TableView<Emprestimo> tabelaEmprestimos;
    @FXML private TableColumn<Emprestimo, String> colEmpLivro;
    @FXML private TableColumn<Emprestimo, String> colEmpAmigo;
    @FXML private TableColumn<Emprestimo, LocalDate> colEmpData;
    @FXML private TableColumn<Emprestimo, LocalDate> colEmpPrevista;
    @FXML private TableColumn<Emprestimo, LocalDate> colEmpDevolucao;
    @FXML private TableColumn<Emprestimo, String> colEmpSituacao;

    // Amigos
    @FXML private TableView<Amigo> tabelaAmigos;
    @FXML private TableColumn<Amigo, String> colAmigoNome;
    @FXML private TableColumn<Amigo, String> colAmigoTelefone;
    @FXML private TableColumn<Amigo, String> colAmigoEmail;
    @FXML private TextField txtAmigoNome;
    @FXML private TextField txtAmigoTelefone;
    @FXML private TextField txtAmigoEmail;
    @FXML private ListView<Emprestimo> listaEmprestimosAmigo;

    // Categorias e localizações
    @FXML private ListView<Categoria> listaCategorias;
    @FXML private TextField txtCategoriaNome;
    @FXML private TextField txtCategoriaDescricao;
    @FXML private ListView<Localizacao> listaLocalizacoes;
    @FXML private TextField txtDivisao;
    @FXML private TextField txtEstante;
    @FXML private TextField txtPrateleira;

    // Aquisições e desaparecimentos
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private Label lblTotalAquisicoes;
    @FXML private TableView<Livro> tabelaAquisicoes;
    @FXML private TableColumn<Livro, LocalDate> colAqData;
    @FXML private TableColumn<Livro, String> colAqTitulo;
    @FXML private TableColumn<Livro, String> colAqForma;
    @FXML private TableColumn<Livro, Double> colAqPreco;
    @FXML private TableView<Desaparecimento> tabelaDesaparecimentos;
    @FXML private TableColumn<Desaparecimento, String> colDesLivro;
    @FXML private TableColumn<Desaparecimento, LocalDate> colDesData;
    @FXML private TableColumn<Desaparecimento, String> colDesDescricao;
    @FXML private TableColumn<Desaparecimento, LocalDate> colDesRecuperacao;

    // Estatísticas
    @FXML private TextArea txtEstatisticas;
    @FXML private PieChart graficoCategorias;
    @FXML private BarChart<String, Number> graficoAquisicoes;

    /** Chamado automaticamente pelo FXMLLoader depois de ligar os campos @FXML. */
    @FXML
    private void initialize() {
        // --- tabela de livros
        ligar(colId, Livro::getId);
        ligar(colTipo, Livro::getTipo);
        ligar(colTitulo, Livro::getTitulo);
        ligar(colAutor, Livro::getAutor);
        ligar(colCategoria, l -> l.getCategoria() == null ? "" : l.getCategoria().getNome());
        ligar(colLocalizacao, l -> l.getLocalizacao() == null ? "" : l.getLocalizacao().toString());
        ligar(colEstado, Livro::getEstado);
        ligar(colDataAquisicao, Livro::getDataAquisicao);
        formatarData(colDataAquisicao);
        tabelaLivros.setRowFactory(t -> new TableRow<>() {
            @Override
            protected void updateItem(Livro livro, boolean vazio) {
                super.updateItem(livro, vazio);
                getStyleClass().remove("desaparecido");
                if (!vazio && livro != null && livro.getEstado() == EstadoItem.DESAPARECIDO) {
                    getStyleClass().add("desaparecido");
                }
            }
        });
        tabelaLivros.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> mostrarDetalhes(novo));
        tabelaLivros.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tabelaLivros.getSelectionModel().getSelectedItem() != null) {
                editarLivro();
            }
        });

        cbFiltroEstado.getItems().setAll(EstadoItem.values());
        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> filtrarLivros());
        cbFiltroCategoria.valueProperty().addListener((obs, antigo, novo) -> filtrarLivros());
        cbFiltroLocalizacao.valueProperty().addListener((obs, antigo, novo) -> filtrarLivros());
        cbFiltroEstado.valueProperty().addListener((obs, antigo, novo) -> filtrarLivros());

        // --- tabela de empréstimos
        ligar(colEmpLivro, e -> e.getLivro().toString());
        ligar(colEmpAmigo, e -> e.getAmigo().getNome());
        ligar(colEmpData, Emprestimo::getDataEmprestimo);
        ligar(colEmpPrevista, Emprestimo::getDataPrevistaDevolucao);
        ligar(colEmpDevolucao, Emprestimo::getDataDevolucao);
        ligar(colEmpSituacao, e -> e.getSituacao(LocalDate.now()));
        formatarData(colEmpData);
        formatarData(colEmpPrevista);
        formatarData(colEmpDevolucao);
        tabelaEmprestimos.setRowFactory(t -> new TableRow<>() {
            @Override
            protected void updateItem(Emprestimo emp, boolean vazio) {
                super.updateItem(emp, vazio);
                getStyleClass().remove("atrasado");
                if (!vazio && emp != null && emp.isAtrasado(LocalDate.now())) {
                    getStyleClass().add("atrasado");
                }
            }
        });

        // --- amigos
        ligar(colAmigoNome, Amigo::getNome);
        ligar(colAmigoTelefone, Amigo::getTelefone);
        ligar(colAmigoEmail, Amigo::getEmail);
        tabelaAmigos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> mostrarAmigo(novo));

        // --- aquisições e desaparecimentos
        dpInicio.setValue(LocalDate.now().withDayOfYear(1));
        dpFim.setValue(LocalDate.now());
        ligar(colAqData, Livro::getDataAquisicao);
        formatarData(colAqData);
        ligar(colAqTitulo, Livro::toString);
        ligar(colAqForma, l -> l.getFormaAquisicao() == null ? "" : l.getFormaAquisicao().toString());
        ligar(colAqPreco, Livro::getPreco);
        colAqPreco.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Double preco, boolean vazio) {
                super.updateItem(preco, vazio);
                setText(vazio || preco == null ? "" : String.format("%.2f €", preco));
            }
        });
        ligar(colDesLivro, d -> d.getLivro().toString());
        ligar(colDesData, Desaparecimento::getData);
        ligar(colDesDescricao, Desaparecimento::getDescricao);
        ligar(colDesRecuperacao, Desaparecimento::getDataRecuperacao);
        formatarData(colDesData);
        formatarData(colDesRecuperacao);

        // As estatísticas são recalculadas sempre que se abre esse separador.
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null && novo.getText().equals("Estatísticas")) {
                atualizarEstatisticas();
            }
        });
    }

    public void setBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        atualizarTudo();
    }

    // ================================================================ Auxiliares

    /** Liga uma coluna a um método que obtém o valor a mostrar a partir do objeto da linha. */
    private static <T, V> void ligar(TableColumn<T, V> coluna, Function<T, V> getter) {
        coluna.setCellValueFactory(c -> new SimpleObjectProperty<>(getter.apply(c.getValue())));
    }

    /** Mostra as datas da coluna no formato dd/MM/aaaa (continuando a ordenar como datas). */
    private static <T> void formatarData(TableColumn<T, LocalDate> coluna) {
        coluna.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate data, boolean vazio) {
                super.updateItem(data, vazio);
                setText(vazio || data == null ? "" : Datas.formatar(data));
            }
        });
    }

    private void mostrarErro(String mensagem) {
        Alert a = new Alert(Alert.AlertType.ERROR, mensagem);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, mensagem);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private boolean confirmar(String mensagem) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    /** Abre uma pequena janela com um calendário para escolher uma data. */
    private Optional<LocalDate> pedirData(String titulo, String texto, LocalDate omissao) {
        Dialog<LocalDate> dialogo = new Dialog<>();
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(texto);
        DatePicker dp = new DatePicker(omissao);
        dialogo.getDialogPane().setContent(dp);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogo.setResultConverter(b -> b == ButtonType.OK ? dp.getValue() : null);
        return dialogo.showAndWait();
    }

    private Livro livroSelecionado() {
        Livro l = tabelaLivros.getSelectionModel().getSelectedItem();
        if (l == null) {
            mostrarErro("Selecione primeiro um livro na tabela.");
        }
        return l;
    }

    /** Grava os dados no ficheiro. */
    public void guardar() {
        try {
            Persistencia.guardar(biblioteca, Persistencia.FICHEIRO);
        } catch (IOException e) {
            mostrarErro("Erro ao guardar os dados: " + e.getMessage());
        }
    }

    /** Depois de uma alteração: grava e atualiza todas as vistas. */
    private void alterado() {
        guardar();
        atualizarTudo();
    }

    private void atualizarTudo() {
        Categoria cat = cbFiltroCategoria.getValue();
        Localizacao loc = cbFiltroLocalizacao.getValue();
        cbFiltroCategoria.getItems().setAll(biblioteca.getCategorias());
        cbFiltroLocalizacao.getItems().setAll(biblioteca.getLocalizacoes());
        cbFiltroCategoria.setValue(biblioteca.getCategorias().contains(cat) ? cat : null);
        cbFiltroLocalizacao.setValue(biblioteca.getLocalizacoes().contains(loc) ? loc : null);

        filtrarLivros();
        atualizarEmprestimos();
        tabelaAmigos.getItems().setAll(biblioteca.getAmigos());
        tabelaAmigos.refresh();
        mostrarAmigo(tabelaAmigos.getSelectionModel().getSelectedItem());
        listaCategorias.getItems().setAll(biblioteca.getCategorias());
        listaLocalizacoes.getItems().setAll(biblioteca.getLocalizacoes());
        filtrarAquisicoes();
        tabelaDesaparecimentos.getItems().setAll(biblioteca.getDesaparecimentos());
        tabelaDesaparecimentos.refresh();
        atualizarEstatisticas();
    }

    public void mostrarAvisoAtrasos() {
        List<Emprestimo> atrasados = biblioteca.getEmprestimosAtrasados(LocalDate.now());
        if (atrasados.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder("Há empréstimos com a devolução em atraso:\n\n");
        for (Emprestimo e : atrasados) {
            sb.append("• ").append(e.getLivro().getTitulo()).append(" — ").append(e.getAmigo().getNome())
                    .append(" (").append(e.getDiasAtraso(LocalDate.now())).append(" dias)\n");
        }
        Alert a = new Alert(Alert.AlertType.WARNING, sb.toString());
        a.setHeaderText("Empréstimos atrasados");
        a.showAndWait();
    }

    // ==================================================================== Menu

    @FXML
    private void guardarMenu() {
        guardar();
        mostrarInfo("Dados guardados em " + Persistencia.FICHEIRO.getAbsolutePath());
    }

    @FXML
    private void sair() {
        guardar();
        Platform.exit();
    }

    @FXML
    private void sobre() {
        mostrarInfo("""
                Os meus livros — G99
                Trabalho prático de Programação Orientada a Objetos 2026/2027
                Universidade da Beira Interior""");
    }

    // ================================================================== Livros

    private void filtrarLivros() {
        Livro selecionado = tabelaLivros.getSelectionModel().getSelectedItem();
        List<Livro> lista = biblioteca.pesquisar(txtPesquisa.getText(), cbFiltroCategoria.getValue(),
                cbFiltroLocalizacao.getValue(), cbFiltroEstado.getValue());
        tabelaLivros.getItems().setAll(lista);
        tabelaLivros.refresh();
        if (selecionado != null && lista.contains(selecionado)) {
            tabelaLivros.getSelectionModel().select(selecionado);
        }
        mostrarDetalhes(tabelaLivros.getSelectionModel().getSelectedItem());
        lblContagem.setText(lista.size() + " de " + biblioteca.getLivros().size() + " livros");
    }

    @FXML
    private void limparFiltros() {
        txtPesquisa.clear();
        cbFiltroCategoria.setValue(null);
        cbFiltroLocalizacao.setValue(null);
        cbFiltroEstado.setValue(null);
    }

    private void mostrarDetalhes(Livro livro) {
        if (livro == null) {
            txtDetalhes.clear();
            return;
        }
        StringBuilder sb = new StringBuilder(livro.getDescricaoDetalhada());
        Emprestimo ativo = biblioteca.getEmprestimoAtivo(livro);
        if (ativo != null) {
            sb.append("\nEmprestado a ").append(ativo.getAmigo().getNome())
                    .append(" desde ").append(Datas.formatar(ativo.getDataEmprestimo()))
                    .append("\nDevolução prevista: ").append(Datas.formatar(ativo.getDataPrevistaDevolucao()))
                    .append("\n");
        }
        List<Emprestimo> historico = biblioteca.getEmprestimosDoLivro(livro);
        if (!historico.isEmpty()) {
            sb.append("\nHistórico de empréstimos (").append(historico.size()).append("):\n");
            for (Emprestimo e : historico) {
                sb.append("• ").append(e.getAmigo().getNome()).append(", ")
                        .append(Datas.formatar(e.getDataEmprestimo())).append(" — ")
                        .append(e.getSituacao(LocalDate.now())).append("\n");
            }
        }
        txtDetalhes.setText(sb.toString());
    }

    @FXML
    private void novoLivro() {
        abrirFormulario(null);
    }

    @FXML
    private void editarLivro() {
        Livro l = livroSelecionado();
        if (l != null) {
            abrirFormulario(l);
        }
    }

    /** Abre a janela LivroForm.fxml para criar (livro == null) ou editar um livro. */
    private void abrirFormulario(Livro livro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LivroForm.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("estilo.css").toExternalForm());
            LivroFormController controller = loader.getController();
            controller.inicializar(biblioteca, livro);

            Stage stage = new Stage();
            stage.setTitle(livro == null ? "Novo livro" : "Editar " + livro.getTitulo());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tabelaLivros.getScene().getWindow());
            stage.setScene(scene);
            stage.showAndWait();

            if (controller.isGuardado()) {
                alterado();
            }
        } catch (IOException e) {
            mostrarErro("Não foi possível abrir o formulário: " + e.getMessage());
        }
    }

    @FXML
    private void removerLivro() {
        Livro l = livroSelecionado();
        if (l == null || !confirmar("Remover definitivamente \"" + l.getTitulo() + "\"?")) {
            return;
        }
        try {
            biblioteca.removerLivro(l);
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void emprestarLivro() {
        Livro l = livroSelecionado();
        if (l == null) {
            return;
        }
        if (l.getEstado() != EstadoItem.DISPONIVEL) {
            mostrarErro("O livro não está disponível (" + l.getEstado() + ").");
            return;
        }
        if (biblioteca.getAmigos().isEmpty()) {
            mostrarErro("Ainda não há amigos registados. Adicione um no separador \"Amigos\".");
            return;
        }

        ComboBox<Amigo> cbAmigo = new ComboBox<>();
        cbAmigo.getItems().setAll(biblioteca.getAmigos());
        cbAmigo.setMaxWidth(Double.MAX_VALUE);
        DatePicker dpData = new DatePicker(LocalDate.now());
        DatePicker dpPrevista = new DatePicker(LocalDate.now().plusWeeks(2));
        GridPane grelha = new GridPane();
        grelha.setHgap(10);
        grelha.setVgap(8);
        grelha.addRow(0, new Label("Amigo"), cbAmigo);
        grelha.addRow(1, new Label("Data do empréstimo"), dpData);
        grelha.addRow(2, new Label("Devolução prevista"), dpPrevista);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Emprestar livro");
        dialogo.setHeaderText("Emprestar \"" + l.getTitulo() + "\"");
        dialogo.getDialogPane().setContent(grelha);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        if (dialogo.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                biblioteca.emprestar(l, cbAmigo.getValue(), dpData.getValue(), dpPrevista.getValue());
                alterado();
            } catch (BibliotecaException e) {
                mostrarErro(e.getMessage());
            }
        }
    }

    @FXML
    private void registarDesaparecimento() {
        Livro l = livroSelecionado();
        if (l == null) {
            return;
        }
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Registar desaparecimento");
        dialogo.setHeaderText("\"" + l.getTitulo() + "\" desapareceu."
                + (l.getEstado() == EstadoItem.EMPRESTADO ? "\nO empréstimo ativo ficará marcado como perdido." : ""));
        dialogo.setContentText("Descrição:");
        Optional<String> descricao = dialogo.showAndWait();
        if (descricao.isEmpty()) {
            return;
        }
        try {
            biblioteca.registarDesaparecimento(l, LocalDate.now(), descricao.get());
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void registarRecuperacao() {
        Livro l = livroSelecionado();
        if (l == null) {
            return;
        }
        Optional<LocalDate> data = pedirData("Registar recuperação", "Quando foi encontrado \"" + l.getTitulo() + "\"?",
                LocalDate.now());
        if (data.isEmpty()) {
            return;
        }
        try {
            biblioteca.registarRecuperacao(l, data.get());
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ============================================================= Empréstimos

    @FXML
    private void atualizarEmprestimos() {
        tabelaEmprestimos.getItems().setAll(chkSoAtivos.isSelected()
                ? biblioteca.getEmprestimosAtivos()
                : biblioteca.getEmprestimos());
        tabelaEmprestimos.refresh();
    }

    private Emprestimo emprestimoAtivoSelecionado() {
        Emprestimo e = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (e == null || !e.isAtivo()) {
            mostrarErro("Selecione um empréstimo ativo.");
            return null;
        }
        return e;
    }

    @FXML
    private void devolverEmprestimo() {
        Emprestimo emp = emprestimoAtivoSelecionado();
        if (emp == null) {
            return;
        }
        Optional<LocalDate> data = pedirData("Registar devolução",
                emp.getAmigo().getNome() + " devolveu \"" + emp.getLivro().getTitulo() + "\" em:", LocalDate.now());
        if (data.isEmpty()) {
            return;
        }
        try {
            biblioteca.devolver(emp, data.get());
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void prolongarEmprestimo() {
        Emprestimo emp = emprestimoAtivoSelecionado();
        if (emp == null) {
            return;
        }
        Optional<LocalDate> data = pedirData("Alterar data prevista", "Nova data prevista de devolução:",
                emp.getDataPrevistaDevolucao().plusWeeks(1));
        if (data.isEmpty()) {
            return;
        }
        if (data.get().isBefore(emp.getDataEmprestimo())) {
            mostrarErro("A data prevista não pode ser anterior à data do empréstimo.");
            return;
        }
        emp.setDataPrevistaDevolucao(data.get());
        alterado();
    }

    // ================================================================== Amigos

    private void mostrarAmigo(Amigo amigo) {
        if (amigo == null) {
            listaEmprestimosAmigo.getItems().clear();
            return;
        }
        txtAmigoNome.setText(amigo.getNome());
        txtAmigoTelefone.setText(amigo.getTelefone());
        txtAmigoEmail.setText(amigo.getEmail());
        listaEmprestimosAmigo.getItems().setAll(biblioteca.getEmprestimosDe(amigo));
    }

    @FXML
    private void adicionarAmigo() {
        try {
            Amigo a = biblioteca.adicionarAmigo(txtAmigoNome.getText(), txtAmigoTelefone.getText().trim(),
                    txtAmigoEmail.getText().trim());
            alterado();
            tabelaAmigos.getSelectionModel().select(a);
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void editarAmigo() {
        Amigo a = tabelaAmigos.getSelectionModel().getSelectedItem();
        if (a == null) {
            mostrarErro("Selecione um amigo na tabela.");
            return;
        }
        if (txtAmigoNome.getText().isBlank()) {
            mostrarErro("O amigo tem de ter nome.");
            return;
        }
        a.setNome(txtAmigoNome.getText().trim());
        a.setTelefone(txtAmigoTelefone.getText().trim());
        a.setEmail(txtAmigoEmail.getText().trim());
        alterado();
    }

    @FXML
    private void removerAmigo() {
        Amigo a = tabelaAmigos.getSelectionModel().getSelectedItem();
        if (a == null || !confirmar("Remover " + a.getNome() + " e o seu histórico de empréstimos?")) {
            return;
        }
        try {
            biblioteca.removerAmigo(a);
            txtAmigoNome.clear();
            txtAmigoTelefone.clear();
            txtAmigoEmail.clear();
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ================================================ Categorias e localizações

    @FXML
    private void adicionarCategoria() {
        try {
            biblioteca.adicionarCategoria(txtCategoriaNome.getText(), txtCategoriaDescricao.getText().trim());
            txtCategoriaNome.clear();
            txtCategoriaDescricao.clear();
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void removerCategoria() {
        Categoria c = listaCategorias.getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }
        try {
            biblioteca.removerCategoria(c);
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void adicionarLocalizacao() {
        int prateleira;
        try {
            prateleira = Integer.parseInt(txtPrateleira.getText().trim());
        } catch (NumberFormatException e) {
            mostrarErro("A prateleira tem de ser um número.");
            return;
        }
        try {
            biblioteca.adicionarLocalizacao(txtDivisao.getText(), txtEstante.getText(), prateleira);
            txtPrateleira.clear();
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void removerLocalizacao() {
        Localizacao loc = listaLocalizacoes.getSelectionModel().getSelectedItem();
        if (loc == null) {
            return;
        }
        try {
            biblioteca.removerLocalizacao(loc);
            alterado();
        } catch (BibliotecaException e) {
            mostrarErro(e.getMessage());
        }
    }

    // ============================================ Aquisições e desaparecimentos

    @FXML
    private void filtrarAquisicoes() {
        LocalDate inicio = dpInicio.getValue() == null ? LocalDate.MIN : dpInicio.getValue();
        LocalDate fim = dpFim.getValue() == null ? LocalDate.MAX : dpFim.getValue();
        List<Livro> lista = biblioteca.getAquisicoesEntre(inicio, fim);
        tabelaAquisicoes.getItems().setAll(lista);
        double total = 0;
        for (Livro l : lista) {
            total += l.getPreco();
        }
        lblTotalAquisicoes.setText(String.format("%d livros, %.2f €", lista.size(), total));
    }

    // ============================================================ Estatísticas

    private void atualizarEstatisticas() {
        txtEstatisticas.setText(biblioteca.getResumoEstatisticas());

        graficoCategorias.getData().clear();
        for (Map.Entry<String, Integer> e : biblioteca.getNumLivrosPorCategoria().entrySet()) {
            graficoCategorias.getData().add(new PieChart.Data(e.getKey() + " (" + e.getValue() + ")", e.getValue()));
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (Map.Entry<Integer, Integer> e : biblioteca.getAquisicoesPorAno().entrySet()) {
            serie.getData().add(new XYChart.Data<>(String.valueOf(e.getKey()), e.getValue()));
        }
        graficoAquisicoes.getData().clear();
        graficoAquisicoes.getData().add(serie);
    }
}
