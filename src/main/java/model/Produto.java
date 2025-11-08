package model;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private double preco;
    private String tipoUnidade;
    private int quantidadeAtual;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private Categoria categoria;

    public Produto() {}

    public Produto(int id, String nome, double preco, String tipoUnidade,
                   int quantidadeAtual, int quantidadeMinima, int quantidadeMaxima, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.tipoUnidade = tipoUnidade;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getTipoUnidade() { return tipoUnidade; }
    public void setTipoUnidade(String tipoUnidade) { this.tipoUnidade = tipoUnidade; }

    public int getQuantidadeAtual() { return quantidadeAtual; }
    public void setQuantidadeAtual(int quantidadeAtual) { this.quantidadeAtual = quantidadeAtual; }

    public int getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(int quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

    public int getQuantidadeMaxima() { return quantidadeMaxima; }
    public void setQuantidadeMaxima(int quantidadeMaxima) { this.quantidadeMaxima = quantidadeMaxima; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return nome + " (" + tipoUnidade + ")";
    }
}

