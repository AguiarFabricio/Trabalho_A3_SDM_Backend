package model;

import java.util.Date;

public class Movimentacao {
    private int id;
    private Produto produto; // associação direta ao produto
    private String tipo;
    private int quantidade;
    private Date dataMovimentacao; // nome esperado pelo DAO

    public Movimentacao() {
    }

    public Movimentacao(int id, Produto produto, String tipo, int quantidade, Date dataMovimentacao) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
    }

    public Movimentacao(Produto produto, String tipo, int quantidade, Date dataMovimentacao) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
}
