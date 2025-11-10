package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe que representa uma movimentação de estoque (entrada ou saída de
 * produtos). Implementa Serializable para permitir envio via rede ou gravação
 * em arquivo.
 */
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Produto produto;
    private String tipo;
    private int quantidade;
    private Date dataMovimentacao;

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

    @Override
    public String toString() {
        return "Movimentacao{"
                + "id=" + id
                + ", produto=" + (produto != null ? produto.getNome() : "N/A")
                + ", tipo='" + tipo + '\''
                + ", quantidade=" + quantidade
                + ", dataMovimentacao=" + dataMovimentacao
                + '}';
    }
}
