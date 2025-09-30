/*
Implementação da classe dos produtos do e commerce. 
Adm passa dados que serão atribuidos ao Produto:
-> id do produto
-> Nome
-> Peso do pacote
-> Preço
 */

public class Produto {
    private int id;
    private String nome;
    private int peso;
    private double preco;

    // Construtor
    public Produto(int id, String nome, int peso, double preco) {
        this.id = id;
        this.nome = nome;
        this.peso = peso;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() { return id; }

    public String getNome() { return nome; }
    
    public int getPeso() { return peso; }
    
    public double getPreco() { return preco; }
}