/*
ClienteDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer as insersões, verificação 
de parametros, busca de clientes, listagem dos clientes

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void cadastrarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nome, cep, email, telefone) VALUES (?, ?, ?, ?)";

        // conexão e acesso ao BD        
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCep());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT * FROM Cliente WHERE id = ?"; // seleciona tudo da tabela Cliente onde o id é igual ao informado
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente(rs.getString("nome"),
                                        rs.getString("cep"),
                                        rs.getString("email"),
                                        rs.getString("telefone"));
                c.setId(rs.getInt("id"));
                return c;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Erro nos dados do cliente: " + ex.getMessage());
        }
        return null;
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = BancoDeDados.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(rs.getString("nome"),
                                        rs.getString("cep"),
                                        rs.getString("email"),
                                        rs.getString("telefone"));
                c.setId(rs.getInt("id"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Erro nos dados de algum cliente: " + ex.getMessage());
        }
        return lista;
    }
}
