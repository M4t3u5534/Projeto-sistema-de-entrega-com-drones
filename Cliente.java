/*
Implementação do cadastro de clientes 
-> Dados pessoais (nome)
-> Endereço (cep)
-> Email *
-> Telefone *

Dados de cadastro acrescentados em relação ao mínimo proposto estão marcados com '*'
 */

import java.util.regex.Pattern;

public class Cliente {
    private int id;
    private String nome;
    private String cep;
    private String email;
    private String telefone;


    // contrutor com tratamento para evitar armazenamento de dados inválidos no banco
    public Cliente(String nome, String cep, String email, String telefone) throws IllegalArgumentException {
        if (!Pattern.matches("[a-zA-Z ]+", nome)) {
            throw new IllegalArgumentException("Nome inválido. Use apenas letras e espaços.");
        }
        if (!Pattern.matches("\\d{5}-?\\d{3}", cep)) {
            throw new IllegalArgumentException("CEP inválido. Formato esperado: 12345-678 ou 12345678.");
        }
        if (email != null && !email.isEmpty()) {
            if (!email.contains("@") || email.contains(" ")) {
                throw new IllegalArgumentException("Email inválido.");
            }
        }
        if (telefone != null && !telefone.isEmpty()) {
            if (!telefone.matches("\\d+")) {
                throw new IllegalArgumentException("Telefone deve conter apenas números.");
            }
        }
        this.nome = nome;
        this.cep = cep;
        this.email = email;
        this.telefone = telefone;
    }
    // Getters e setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }

    public String getCep() { return cep; }
    
    public String getEmail() { return email; }
    
    public String getTelefone() { return telefone; }
}
