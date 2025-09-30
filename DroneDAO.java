/*
DroneDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer as insersões (inserirDrone), listagem dos drones
(listarDrones)
Nesse caso, os drones vão ser cadastrados automaticamente na main (caso não estiverem cadastrados).
Foi definido um limite de 5 drones. O status do drone fica 'PENDENTE' até o quinto drone receber um pedido,
então os drone são 'enviados' e seu status atualiza para 'FINALIZADO'.

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DroneDAO {
    private Connection conexao;

    // construtor que recebe a conexão comum com as outras DAOs
    public DroneDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // faz o cadastro do drone no banco de dados
    public void inserirDrone(Drone drone) {
        // comunicação em linguagem sql a partir dos dados do Drone cadastrado
        String sql = "INSERT INTO Drone (status_bateria, capacidade_carga, disponivel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, drone.getStatusBateria());
            stmt.setDouble(2, drone.getCapacidadeCarga());
            stmt.setBoolean(3, drone.isDisponivel());
            stmt.executeUpdate();
            System.out.println("Drone cadastrado");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar drone: " + e.getMessage());
        }
    }

    // retorna lista completa dos drones
    public List<Drone> listarDrones() {
        List<Drone> drones = new ArrayList<>();
        String sql = "SELECT * FROM Drone";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {// retorna um ResultSet com todas as linhas do banco
            while (rs.next()) {
                Drone d = new Drone(
                        rs.getInt("status_bateria"),
                        rs.getDouble("capacidade_carga"),
                        rs.getBoolean("disponivel")
                );
                d.setId(rs.getInt("id_drone"));
                drones.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar drones: " + e.getMessage());
        }
        return drones;
    }
}