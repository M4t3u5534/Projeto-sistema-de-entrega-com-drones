/*
EntregaDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer cadastrar entregas, atualização 
de status das esntregas, listagem das entregas comos dados dos clientes 
e drone associado ao pedido, busca por id (função auxiliar), mostrar pendentes 

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    // cadastrar entrega já existente
    public void cadastrarEntrega(Entrega entrega) {
        
        String sql = "INSERT INTO Entrega (idProduto, idCliente, idDrone, peso, status, dataCompra) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entrega.getIdProduto());
            stmt.setInt(2, entrega.getIdCliente());
            stmt.setInt(3, entrega.getDrone().getId());
            stmt.setInt(4, entrega.getPeso());
            stmt.setString(5, "Pendente");
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entrega.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar entrega: " + e.getMessage());
        }
    }

    // listar todas as entregas
    public List<Entrega> listarEntregas() {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.*, d.statusBateria, d.capacidade, d.disponivel FROM Entrega e " +
                     "JOIN Drone d ON e.idDrone = d.id";
        try (Connection conn = BancoDeDados.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Drone drone = new Drone(
                        rs.getInt("idDrone"),
                        rs.getInt("statusBateria"),
                        rs.getInt("capacidade"),
                        rs.getBoolean("disponivel")
                );

                Entrega entrega = new Entrega(
                        rs.getInt("idProduto"),
                        rs.getInt("idCliente"),
                        drone,
                        rs.getInt("peso")
                );
                entrega.setId(rs.getInt("id"));
                entrega.setStatus(rs.getString("status"));
                entregas.add(entrega);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar entregas: " + e.getMessage());
        }
        return entregas;
    }

    // listar apenas entregas pendentes
    public List<Entrega> listarEntregasPendentes() {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.*, d.statusBateria, d.capacidade, d.disponivel FROM Entrega e " +
                     "JOIN Drone d ON e.idDrone = d.id WHERE e.status = 'Pendente'";
        try (Connection conn = BancoDeDados.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Drone drone = new Drone(
                        rs.getInt("idDrone"),
                        rs.getInt("statusBateria"),
                        rs.getInt("capacidade"),
                        rs.getBoolean("disponivel")
                );

                Entrega entrega = new Entrega(
                        rs.getInt("idProduto"),
                        rs.getInt("idCliente"),
                        drone,
                        rs.getInt("peso")
                );
                entrega.setId(rs.getInt("id"));
                entrega.setStatus(rs.getString("status"));
                entregas.add(entrega);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar entregas pendentes: " + e.getMessage());
        }
        return entregas;
    }

    // buscar entrega por ID
    public Entrega buscarEntregaPorId(int id) {
        String sql = "SELECT e.*, d.statusBateria, d.capacidade, d.disponivel FROM Entrega e " +
                     "JOIN Drone d ON e.idDrone = d.id WHERE e.id = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Drone drone = new Drone(
                        rs.getInt("idDrone"),
                        rs.getInt("statusBateria"),
                        rs.getInt("capacidade"),
                        rs.getBoolean("disponivel")
                );

                Entrega entrega = new Entrega(
                        rs.getInt("idProduto"),
                        rs.getInt("idCliente"),
                        drone,
                        rs.getInt("peso")
                );
                entrega.setId(rs.getInt("id"));
                entrega.setStatus(rs.getString("status"));
                return entrega;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar entrega: " + e.getMessage());
        }
        return null;
    }

    // atualizar entrega (status)
    public void atualizarEntrega(Entrega entrega) {
        String sql = "UPDATE Entrega SET status = ? WHERE id = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrega.getStatus());
            stmt.setInt(2, entrega.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar entrega: " + e.getMessage());
        }
    }
}
