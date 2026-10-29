# G99 — Os meus livros (T03)

Trabalho prático de POO 2026/2027 (UBI). Aplicação standalone para gerir uma coleção de livros.

## Funcionalidades

- **Livros e bandas desenhadas** (`BandaDesenhada extends Livro extends Item`): adicionar, editar, remover e ver detalhes.
- **Categorias** e **localizações** (divisão / estante / prateleira).
- **Pesquisa** por texto (título, autor, editora, ISBN) e filtros por categoria, localização e estado.
- **Empréstimos** a amigos: data prevista de devolução, devolução, alteração do prazo, aviso de atrasos e histórico por livro e por amigo.
- **Aquisições** (data `LocalDate`, forma e preço), com listagem por período.
- **Desaparecimentos** e recuperação (se o livro estava emprestado, o empréstimo fica marcado como perdido).
- **Estatísticas**: totais por estado, valor da coleção, livros por categoria e aquisições por ano (com gráficos).
- **Persistência** com `File` + `ObjectOutputStream`/`ObjectInputStream` no ficheiro `biblioteca.dat` (na primeira execução são criados dados de exemplo).

## Estrutura

```
src/main/java/livros/
  Main.java                 ponto de entrada (identificação do grupo)
  modelo/                   Item, Livro, BandaDesenhada, Categoria, Localizacao, Amigo,
                            Emprestimo, Desaparecimento, Biblioteca, enums, BibliotecaException
  persistencia/             Persistencia (ObjectStreams), DadosExemplo
  texto/                    MenuTexto, Leitor (interface de texto)
  fx/                       AppFX, PrincipalController, LivroFormController (JavaFX)
src/main/resources/livros/fx/
  Principal.fxml, LivroForm.fxml (abrem no Scene Builder), estilo.css
```

## Como executar

Precisa de JDK 17+ e Maven (o JavaFX é descarregado pelo Maven).

Interface gráfica:

```bash
mvn javafx:run
```

Interface de texto:

```bash
mvn compile && java -cp target/classes livros.Main texto
```

No IntelliJ / NetBeans basta abrir a pasta como projeto Maven e correr `livros.Main`
(ou o objetivo Maven `javafx:run`).
