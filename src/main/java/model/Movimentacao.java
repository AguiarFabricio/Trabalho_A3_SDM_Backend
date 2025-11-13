package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa uma movimentação de estoque no sistema.
 * <p>
 * Uma movimentação indica a entrada ou saída de produtos, registrando a quantidade
 * alterada, o tipo de movimentação (por exemplo: "ENTRADA" ou "SAÍDA"), a data em
 * que ocorreu e o produto associado.
 * </p>
 *
 * <p>
 * Esta classe implementa {@link Serializable}, permitindo que objetos sejam
 * transmitidos pela rede ou gravados em arquivos de forma persistente.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 * @see model.Produto
 */
public class Movimentacao implements Serializable {

    /** Identificador de versão da classe para serialização. */
    private static final long serialVersionUID = 1L;

    /** Identificador único da movimentação. */
    private int id;

    /** Produto associado à movimentação. */
    private Produto produto;

    /** Tipo da movimentação (ex.: "ENTRADA" ou "SAÍDA"). */
    private String tipo;

    /** Quantidade de produtos movimentados. */
    private int quantidade;

    /** Data e hora em que a movimentação foi registrada. */
    private Date dataMovimentacao;

    /**
     * Construtor padrão sem parâmetros.
     * <p>
     * Necessário para compatibilidade com frameworks e mecanismos de persistência.
     * </p>
     */
    public Movimentacao() {
    }

    /**
     * Construtor completo que inicializa todos os atributos da movimentação.
     *
     * @param id identificador único da movimentação
     * @param produto produto associado à movimentação
     * @param tipo tipo da movimentação (ex.: "ENTRADA" ou "SAÍDA")
     * @param quantidade quantidade movimentada
     * @param dataMovimentacao data em que a movimentação ocorreu
     */
    public Movimentacao(int id, Produto produto, String tipo, int quantidade, Date dataMovimentacao) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
    }

    /**
     * Construtor alternativo sem o campo {@code id}, usado para novos registros
     * ainda não persistidos no banco de dados.
     *
     * @param produto produto associado à movimentação
     * @param tipo tipo da movimentação (ex.: "ENTRADA" ou "SAÍDA")
     * @param quantidade quantidade movimentada
     * @param dataMovimentacao data em que a movimentação ocorreu
     */
    public Movimentacao(Produto produto, String tipo, int quantidade, Date dataMovimentacao) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
    }

    /**
     * Retorna o identificador único da movimentação.
     *
     * @return identificador numérico da movimentação
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador único da movimentação.
     *
     * @param id identificador numérico da movimentação
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o produto associado à movimentação.
     *
     * @return instância de {@link Produto}
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * Define o produto associado à movimentação.
     *
     * @param produto produto relacionado à movimentação
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * Retorna o tipo da movimentação (ex.: "ENTRADA" ou "SAÍDA").
     *
     * @return tipo da movimentação
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo da movimentação.
     *
     * @param tipo tipo da movimentação (ex.: "ENTRADA" ou "SAÍDA")
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Retorna a quantidade movimentada de produtos.
     *
     * @return quantidade movimentada
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade movimentada de produtos.
     *
     * @param quantidade valor numérico da movimentação
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna a data em que a movimentação ocorreu.
     *
     * @return data e hora da movimentação
     */
    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    /**
     * Define a data em que a movimentação ocorreu.
     *
     * @param dataMovimentacao data e hora da movimentação
     */
    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    /**
     * Retorna uma representação textual da movimentação.
     * <p>
     * Inclui o ID, nome do produto (quando disponível), tipo, quantidade e data.
     * </p>
     *
     * @return representação textual da movimentação
     */
    @Override
    public String toString() {
        return "Movimentacao{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "N/A") +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", dataMovimentacao=" + dataMovimentacao +
                '}';
    }
}
