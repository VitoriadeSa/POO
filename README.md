# G16 — Os meus livros (T03)

Trabalho prático de POO 2026/2027 (UBI). Aplicação para gerir uma coleção de livros.

## Funcionalidades

- Adicionar livros e bandas desenhadas (`BandaDesenhada extends Livro`) com categoria, localização, data de aquisição (`LocalDate`) e preço.
- Remover e pesquisar livros (por título, autor ou categoria).
- Emprestar livros a amigos, registar devoluções e ver os empréstimos atrasados.
- Registar desaparecimentos e recuperações.
- Estatísticas simples.
- Os dados são guardados no ficheiro `biblioteca.dat` com `ObjectOutputStream`/`ObjectInputStream`.

## Ficheiros

```
src/main/java/livros/
  Main.java           ponto de entrada (identificação do grupo)
  Livro.java          livro (com o enum Estado)
  BandaDesenhada.java subclasse de Livro
  Emprestimo.java     empréstimo a um amigo
  Biblioteca.java     listas, operações e persistência
  MenuTexto.java      interface de texto
  AppFX.java          arranque da interface gráfica
  Controller.java     controlador do Principal.fxml
src/main/resources/livros/Principal.fxml   janela (abre no Scene Builder)
```

## Como executar

Precisa de JDK 17+ e Maven.

```bash
mvn javafx:run
```

```bash
mvn compile && java -cp target/classes livros.Main texto
```
