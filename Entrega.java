/*
Implementação dos dados da solicitação de entrega. Cliente passa dados e seleciona produto, 
passando direta ou indiretamente:
-> Drone e seus dados utilizado na entrega do pedido
-> Cliente e seus dados que fez o pedido
-> Peso do pacote (o peso foi definido a partir da quantidade de itens indo de 0-3 que o cliente selecionou)
 */

import java.time.LocalDateTime; // salvar dada e horario do pedido

public class Entrega {
    // Contém dados chaves estrangeiras
    private int id;
    private Cliente cliente;
    private Drone drone;
    private double pesoPacote;
    private String enderecoDestino;
    private String statusEntrega; // "PENDENTE", "FINALIZADA"
    private LocalDateTime dataHora;

    // Construtor
    public Entrega(Cliente cliente, double pesoPacote, String enderecoDestino) {
        this.cliente = cliente;
        this.pesoPacote = pesoPacote;
        this.enderecoDestino = enderecoDestino;
        this.statusEntrega = "PENDENTE";
        this.dataHora = LocalDateTime.now();
    }

    // Getters e Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }

    public Drone getDrone() { return drone; }

    public void setDrone(Drone drone) { this.drone = drone; }

    public double getPesoPacote() { return pesoPacote; }

    public String getEnderecoDestino() { return enderecoDestino; }

    public String getStatusEntrega() { return statusEntrega; }

    public void setStatusEntrega(String statusEntrega) { this.statusEntrega = statusEntrega; }

    public LocalDateTime getDataHora() { return dataHora; }
}