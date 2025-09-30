/*
EntregaDAO.java é uma classe auxiliar que faz a comunicação necessária com o banco
de dados. Ela foi utilizada para fazer as insersões (registrarEntrega), atualização 
de status das esntregas (finalizarEntregasPendentes), listagem das entregas com
os dados dos clientes e drone associado ao pedido (listarEntregesComClienteEDrone) 

Fontes:
https://medium.com/@felipe.damasceno.b/padr%C3%B5es-de-projeto-e-o-data-access-object-dao-7d7e4818866c
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
    private Connection conexao;

    public EntregaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Registrar entrega com atribuição automática de drone
    public void registrarEntrega(Entrega entrega) throws SQLException {
        // busca drone disponível
        String sqlDrone = "SELECT * FROM Drone WHERE disponivel = TRUE AND status_bateria >= 50 AND capacidade_carga >= ? LIMIT 1";
        try (PreparedStatement stmtDrone = conexao.prepareStatement(sqlDrone)) {
            stmtDrone.setDouble(1, entrega.getPesoPacote());
            ResultSet rs = stmtDrone.executeQuery();


            /* TO DO: ao usar o mesmo cliente (mesmo nome e etc) para fazer varios pedidos
            ao fechar e abrir a interface ele bagunça a ordem de status pendente e finalizado
            e a quantidade de drones trava gerando essa exceção
               */
            if (!rs.next()) {
                throw new SQLException("Nenhum drone disponível");
            }

            Drone drone = new Drone(
                    rs.getInt("status_bateria"),
                    rs.getDouble("capacidade_carga"),
                    rs.getBoolean("disponivel")
            );
            drone.setId(rs.getInt("id_drone"));
            entrega.setDrone(drone);

            // atualiza status de disponibilidade de drone para indisponível
            String sqlAtualizaDrone = "UPDATE Drone SET disponivel = FALSE WHERE id_drone = ?";
            try (PreparedStatement stmtAtualiza = conexao.prepareStatement(sqlAtualizaDrone)) {
                stmtAtualiza.setInt(1, drone.getId());
                stmtAtualiza.executeUpdate();
            }

            // cadastra a entrega no banco de dados
            String sqlEntrega = "INSERT INTO Entrega (id_cliente, id_drone, peso_pacote, endereco_destino, status_entrega) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtEntrega = conexao.prepareStatement(sqlEntrega)) {
                stmtEntrega.setInt(1, entrega.getCliente().getId());
                stmtEntrega.setInt(2, drone.getId());
                stmtEntrega.setDouble(3, entrega.getPesoPacote());
                stmtEntrega.setString(4, entrega.getEnderecoDestino());
                stmtEntrega.setString(5, "PENDENTE");
                stmtEntrega.executeUpdate();
            }
        }
    }

    // finaliza todas entregas pendentes e libera os drones
    public void finalizarEntregasPendentes() throws SQLException {
        // atualiza status das entregas pendentes para 'FINALIZADA'
        String sqlAtualizaEntregas = "UPDATE Entrega SET status_entrega = 'FINALIZADA' WHERE status_entrega = 'PENDENTE'";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlAtualizaEntregas)) {
            stmt.executeUpdate();
        }

        // atualiza status de disponibilidade para disponível
        String sqlLiberarDrones = "UPDATE Drone SET disponivel = TRUE";
        try (PreparedStatement stmt2 = conexao.prepareStatement(sqlLiberarDrones)) {
            stmt2.executeUpdate();
        }
    }

    // lista entregas com dados completos de cliente e drone
    public List<Entrega> listarEntregesComClienteEDrone() throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.id_entrega, e.peso_pacote, e.endereco_destino, e.status_entrega, " +
                     "c.id_cliente, c.nome, c.email, c.endereco, c.telefone, " +
                     "d.id_drone, d.status_bateria, d.capacidade_carga, d.disponivel " +
                     "FROM Entrega e " +
                     "JOIN Cliente c ON e.id_cliente = c.id_cliente " +
                     "JOIN Drone d ON e.id_drone = d.id_drone";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(rs.getString("nome"),
                                        rs.getString("email"),
                                        rs.getString("endereco"),
                                        rs.getString("telefone"));
                c.setId(rs.getInt("id_cliente"));

                Drone d = new Drone(rs.getInt("status_bateria"),
                                    rs.getDouble("capacidade_carga"),
                                    rs.getBoolean("disponivel"));
                d.setId(rs.getInt("id_drone"));

                Entrega e = new Entrega(c, rs.getDouble("peso_pacote"), rs.getString("endereco_destino"));
                e.setId(rs.getInt("id_entrega"));
                e.setDrone(d);
                e.setStatusEntrega(rs.getString("status_entrega"));

                entregas.add(e);
            }
        }

        return entregas;
    }
}