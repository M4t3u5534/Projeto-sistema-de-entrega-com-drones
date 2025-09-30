/*
 Implementação da classe de drones 
 -> ID
 -> Status da bateria
 -> Capacidade de carga
 -> Disponibilidade (está em entrega ou livre) 
 */
public class Drone {
    private int id;
    private int statusBateria; // 0 a 100
    private int capacidade;    // quantidade máxima que pode carregar
    private boolean disponivel;

    // Construtor     
    public Drone(int id, int statusBateria, int capacidade, boolean disponivel) {
        if (statusBateria < 0 || statusBateria > 100) {
            throw new IllegalArgumentException("Status da bateria deve ser entre 0 e 100.");
        }
        this.id = id;
        this.statusBateria = statusBateria;
        this.capacidade = capacidade;
        this.disponivel = disponivel;
    }

    // Getters e Setters
    public int getId() { return id; }

    public int getStatusBateria() { return statusBateria; }
    
    public void setStatusBateria(int bateria){ this.statusBateria = bateria;}
    
    public int getCapacidade() { return capacidade; }
    
    public boolean isDisponivel() { return disponivel; }
    
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}
