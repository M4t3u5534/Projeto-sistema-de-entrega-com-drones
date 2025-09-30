/*
Implementação do cadastro de clientes 
-> Dados pessoais 
-> Endereço 
-> Email *
-> Telefone *

Dados de cadastro acrescentados em relação ao mínimo proposto estão marcados com '*'
 */
public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String endereco;
    private String telefone;

    // Contrutor
    public Cliente(String nome, String email, String endereco, String telefone) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    // Getters e setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }

    public String getEmail() { return email; }

    public String getEndereco() { return endereco; }

    public String getTelefone() { return telefone; }
}