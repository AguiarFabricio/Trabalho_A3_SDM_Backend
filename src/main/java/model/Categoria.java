package model;

import java.io.Serializable;

public class Categoria implements Serializable {

    private int id;
    private String nome;
    private EmbalagemProduto embalagem; // ✅ era String
    private TamanhoProduto tamanho;     // ✅ era String

    public Categoria() {
    }

    public Categoria(int id, String nome, EmbalagemProduto embalagem, TamanhoProduto tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EmbalagemProduto getEmbalagem() {
        return embalagem;
    } // ✅ tipo enum

    public void setEmbalagem(EmbalagemProduto embalagem) {
        this.embalagem = embalagem;
    } // ✅ tipo enum

    public TamanhoProduto getTamanho() {
        return tamanho;
    } // ✅ tipo enum

    public void setTamanho(TamanhoProduto tamanho) {
        this.tamanho = tamanho;
    } // ✅ tipo enum

    @Override
    public String toString() {
        return "Categoria{"
                + "id=" + id
                + ", nome='" + nome + '\''
                + ", embalagem=" + embalagem
                + ", tamanho=" + tamanho
                + '}';
    }
}
