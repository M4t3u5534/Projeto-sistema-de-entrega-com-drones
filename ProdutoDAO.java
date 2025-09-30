/*
ProdutoDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Nela é feito o cadastro dos produtos e sua listagem. Esta classe também consta
com a busca do produto por ID (função auxilir para listagems) 

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO Produto (id, nome, peso, preco) VALUES (?, ?, ?, ?)";

        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getId());
            stmt.setString(2, produto.getNome());
            stmt.setInt(3, produto.getPeso());
            stmt.setDouble(4, produto.getPreco());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public Produto buscarProdutoPorId(int id) {
        String sql = "SELECT * FROM Produto WHERE id = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Produto(rs.getInt("id"),
                                   rs.getString("nome"),
                                   rs.getInt("peso"),
                                   rs.getDouble("preco"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }

    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Produto";
        try (Connection conn = BancoDeDados.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto p = new Produto(rs.getInt("id"),
                                        rs.getString("nome"),
                                        rs.getInt("peso"),
                                        rs.getDouble("preco"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }
}