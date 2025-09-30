/*
Implementação da classe entrega. Cliente passa seu id e seleciona id do
produto que deseja comprar.
-> Chaves estrangeiras (ids Produto e Cliente)
-> dados do drone
-> Peso do pacote (o peso foi definido a partir da quantidade de itens indo de 0-3 que o cliente selecionou)
-> Status da entrega (pendente ou finalizada)
*/

import java.time.LocalDateTime; // salvar dada e horario do pedido

public class Entrega {
    private int id;
    // id - chaves estrangeiras
    private int idProduto;
    private int idCliente;
    private Drone drone;
    private int peso;
    private String status;
    private LocalDateTime dataCompra;

    // Construtor
    public Entrega(int idProduto, int idCliente, Drone drone, int peso) {
        this.idProduto = idProduto;
        this.idCliente = idCliente;
        this.drone = drone;
        this.peso = peso;
        this.status = "Pendente";
        this.dataCompra = LocalDateTime.now();
    }
    // Getters e Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
    
    public int getIdProduto() { return idProduto; }
    
    public int getIdCliente() { return idCliente; }
    
    public Drone getDrone() { return drone; }
    
    public int getPeso() { return peso; }
    
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataCompra() { return dataCompra; }
}
