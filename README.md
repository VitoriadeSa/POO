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
Linux / macOS / Git Bash:
```bash
mvn compile && java -cp target/classes livros.Main texto
```
Windows (PowerShell):
```powershell
mvn compile; java -cp target/classes livros.Main texto
```

### Como instalar os requisitos no Windows (PowerShell)

Caso não tenha o **Maven** ou o **JDK 17** instalados, pode instalar tudo diretamente pelo **PowerShell** usando o gestor de pacotes **Scoop**:

1. **Instalar o Scoop** (caso ainda não o tenha):
   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser -Force
   irm get.scoop.sh | iex
   ```

2. **Instalar o Apache Maven:**
   ```powershell
   scoop install maven
   ```

3. **Instalar o JDK 17 (Temurin):**
   ```powershell
   scoop bucket add java
   scoop install temurin17-jdk
   ```

4. **Confirmar a instalação:**
   Feche e volte a abrir o PowerShell e verifique as versões (o Java deve mostrar `17.x.x`):
   ```powershell
   java -version
   mvn -version
   ```
5. **Por fim, execute o projeto:**
   Entre na pasta raiz do projeto (onde está o `pom.xml`) e inicie a aplicação:
   ```powershell
   cd Desktop\Livros
   mvn javafx:run
   ```
   Ou, para a interface de texto:
   ```powershell
   mvn compile; java -cp target/classes livros.Main texto
   ```
