/*
DroneDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer as insersões, listagem dos drones e atualização
de dados de status no bd.

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DroneDAO {

    public void cadastrarDrone(Drone drone) {
        String sql = "INSERT INTO Drone (id, statusBateria, capacidade, disponivel) VALUES (?, ?, ?, ?)";

        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, drone.getId());
            stmt.setInt(2, drone.getStatusBateria());
            stmt.setInt(3, drone.getCapacidade());
            stmt.setBoolean(4, drone.isDisponivel());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar drone: " + e.getMessage());
        }
    }

    public Drone buscarDroneDisponivel(int pesoNecessario) {
        String sql = "SELECT * FROM Drone WHERE disponivel = true AND capacidade >= ? ORDER BY capacidade ASC LIMIT 1";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pesoNecessario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Drone(rs.getInt("id"),
                                 rs.getInt("statusBateria"),
                                 rs.getInt("capacidade"),
                                 rs.getBoolean("disponivel"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar drone disponível: " + e.getMessage());
        }
        return null;
    }

    public List<Drone> listarDrones() {
        List<Drone> lista = new ArrayList<>();
        String sql = "SELECT * FROM Drone";
        try (Connection conn = BancoDeDados.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Drone d = new Drone(rs.getInt("id"),
                                    rs.getInt("statusBateria"),
                                    rs.getInt("capacidade"),
                                    rs.getBoolean("disponivel"));
                lista.add(d);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar drones: " + e.getMessage());
        }
        return lista;
    }

    public void atualizarDisponibilidade(int idDrone, boolean disponivel) {
        String sql = "UPDATE Drone SET disponivel = ? WHERE id = ?";
        try (Connection conn = BancoDeDados.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, idDrone);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar disponibilidade do drone: " + e.getMessage());
        }
    }
    public void atualizarDrone(Drone drone) {
        String sql = "UPDATE Drone SET statusBateria = ?, disponivel = ? WHERE id = ?";
        try (Connection conn = BancoDeDados.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, drone.getStatusBateria());
            stmt.setBoolean(2, drone.isDisponivel());
            stmt.setInt(3, drone.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar drone: " + e.getMessage());
        }
    }


}
