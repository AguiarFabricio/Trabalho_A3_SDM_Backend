package model;

/**
 * Enumeração que representa os tipos de embalagem disponíveis para os produtos.
 * <p>
 * Cada constante define o material da embalagem utilizado para armazenar ou
 * comercializar o produto. Este enum é utilizado nas entidades {@link model.Categoria}
 * e {@link model.Produto} para padronizar os valores aceitos no sistema.
 * </p>
 *
 * <p>
 * Os tipos definidos são:
 * <ul>
 *     <li>{@link #PLASTICO} — Embalagem feita de plástico, leve e comum em produtos alimentícios.</li>
 *     <li>{@link #VIDRO} — Embalagem de vidro, geralmente usada para produtos líquidos ou de conservação longa.</li>
 *     <li>{@link #LATA} — Embalagem metálica, usada para bebidas, conservas e produtos industrializados.</li>
 * </ul>
 * </p>
 *
 * @author Luiz
 * @version 1.0
 */
public enum EmbalagemProduto {

    /** Embalagem de plástico, comum em produtos de uso cotidiano. */
    PLASTICO,

    /** Embalagem de vidro, geralmente utilizada para produtos líquidos ou de conservação prolongada. */
    VIDRO,

    /** Embalagem metálica (lata), usada para bebidas e alimentos enlatados. */
    LATA
}
