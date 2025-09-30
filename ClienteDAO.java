/*
ClienteDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer as insersões (inserirCliente), verificação 
de parametros (clienteExiste), listagem de clientes (função listarClientes que retorna
a lista completa dos clientes)

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conexao;

    // construtor que recebe conexão compartilhada 
    // (evita abrir conexões desnecessárias com o banco de dados - todas DAOs usam a mesma conexão)
    public ClienteDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirCliente(Cliente cliente) {
        // verifica cliente pelo email
        if (clienteExiste(cliente.getEmail())) {
            System.out.println("Email já utilizado: " + cliente.getEmail());
            return; // retorna e não insere duplicata
        }

        // insersão do cliente
        String sql = "INSERT INTO Cliente (nome, email, endereco, telefone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getEndereco());
            stmt.setString(4, cliente.getTelefone());
            stmt.executeUpdate();

            // trecho para armazenar o id gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }

            System.out.println("Cliente cadastrado");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // verificação de existencia de cliente
    public boolean clienteExiste(String email) {
        // verificação em sql no banco
        String sql = "SELECT id_cliente FROM Cliente WHERE email = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true se já existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // retorna clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>(); // array list armazena dados (objetos) do cliente
        String sql = "SELECT * FROM Cliente";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // retorna um ResultSet com todas as linhas do banco
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                );
                c.setId(rs.getInt("id_cliente"));
                clientes.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro na listagem dos clientes: " + e.getMessage());
        }
        return clientes;
    }
}