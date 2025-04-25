/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistemasdedenuncias.pkg2;

/**
 *
 * @author Muril
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemasDeDenuncias {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_denuncias";
    private static final String USUARIO = "Murilobo";
    private static final String SENHA = "Murilo2012";

    private static ArrayList<Denuncia> denuncias = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        conectarBancoDados();

        while (true) {
            exibirMenu();
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpar a quebra de linha

            switch (escolha) {
                case 1:
                    reportarDenuncia();
                    break;
                case 2:
                    listarDenuncias();
                    break;
                case 3:
                    marcarComoResolvida();
                    break;
                case 4:
                    sairDoSistema();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void conectarBancoDados() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida.");
            criarTabela(conexao);
            carregarDenunciasDoBanco(conexao);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void criarTabela(Connection conexao) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS denuncias (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "descricao VARCHAR(255) NOT NULL," +
                "local VARCHAR(255) NOT NULL," +
                "resolvida BOOLEAN NOT NULL)";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private static void carregarDenunciasDoBanco(Connection conexao) throws SQLException {
        String sql = "SELECT * FROM denuncias";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String descricao = resultSet.getString("descricao");
                String local = resultSet.getString("local");
                boolean resolvida = resultSet.getBoolean("resolvida");
                Denuncia denuncia = new Denuncia(1, "Descrição da denúncia", "Local da denúncia", false);
                denuncias.add(denuncia);
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("Menu:");
        System.out.println("1. Reportar falha no saneamento básico");
        System.out.println("2. Listar denúncias");
        System.out.println("3. Marcar denúncia como resolvida");
        System.out.println("4. Sair");
    }

    private static void reportarDenuncia() {
        System.out.println("Descreva a falha:");
        String descricao = scanner.nextLine();
        System.out.println("Local da falha:");
        String local = scanner.nextLine();
        Denuncia denuncia = new Denuncia(1,descricao, local, false);
        denuncias.add(denuncia);
        salvarDenunciaNoBanco(denuncia);
        int numeroDenuncia = denuncias.indexOf(denuncia) + 1;
        System.out.println("Denúncia registrada com sucesso. Número da denúncia: " + numeroDenuncia);
    }

    private static void salvarDenunciaNoBanco(Denuncia denuncia) {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "INSERT INTO denuncias (descricao, local, resolvida) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.setString(1, denuncia.getDescricao());
                statement.setString(2, denuncia.getLocal());
                statement.setBoolean(3, denuncia.isResolvida());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar denúncia no banco de dados: " + e.getMessage());
        }
    }

    private static void listarDenuncias() {
        System.out.println("Lista de denúncias:");
        for (int i = 0; i < denuncias.size(); i++) {
            Denuncia d = denuncias.get(i);
            System.out.println((i + 1) + ". " + d.getDescricao() + " - " + d.getLocal() + " - Resolvida: " + d.isResolvida());
        }
    }

    private static void marcarComoResolvida() {
        System.out.println("Digite o número da denúncia a ser marcada como resolvida:");
        int numeroDenuncia = scanner.nextInt();
        if (numeroDenuncia > 0 && numeroDenuncia <= denuncias.size()) {
            Denuncia denunciaResolvida = denuncias.get(numeroDenuncia - 1);
            denunciaResolvida.resolver();
            atualizarDenunciaNoBanco(denunciaResolvida);
            System.out.println("Denúncia marcada como resolvida.");
        } else {
            System.out.println("Número de denúncia inválido.");
        }
    }

    private static void atualizarDenunciaNoBanco(Denuncia denuncia) {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            String sql = "UPDATE denuncias SET resolvida = true WHERE id = ?";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.setInt(1, denuncia.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar denúncia no banco de dados: " + e.getMessage());
        }
    }

    private static void sairDoSistema() {
        System.out.println("Saindo do sistema.");
        scanner.close();
        System.exit(0);
    }
}

        
