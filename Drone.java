/*
 Implementação do cadastro de drones 
 -> ID
 -> Status da bateria
 -> Capacidade de carga
 -> Disponibilidade (está em entrega ou livre) *

Dados de cadastro acrescentados em relação ao mínimo proposto estão marcados com '*'
 */

public class Drone {
    private int id;
    private int statusBateria;
    private double capacidadeCarga;
    private boolean disponivel;

    // Construtor:
    public Drone(int statusBateria, double capacidadeCarga, boolean disponivel) {
        this.statusBateria = statusBateria;
        this.capacidadeCarga = capacidadeCarga;
        this.disponivel = disponivel;
    }
    // Getters e setters:
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getStatusBateria() { return statusBateria; }

    public double getCapacidadeCarga() { return capacidadeCarga; }

    public boolean isDisponivel() { return disponivel; }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}