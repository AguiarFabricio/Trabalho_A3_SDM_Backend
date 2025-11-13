package model;

/**
 * Enumeração que define os tipos de movimentação de estoque.
 * <p>
 * Este enum é utilizado na classe {@link model.Movimentacao} para indicar
 * se a operação realizada representa uma <b>entrada</b> ou uma <b>saída</b>
 * de produtos no estoque.
 * </p>
 *
 * <p>
 * Os tipos disponíveis são:
 * <ul>
 *     <li>{@link #ENTRADA} — Representa a entrada de produtos no estoque,
 *         como compras, devoluções ou ajustes positivos.</li>
 *     <li>{@link #SAIDA} — Representa a saída de produtos do estoque,
 *         como vendas, consumo interno ou perdas.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Essa enumeração padroniza os valores aceitos no banco de dados e melhora
 * a legibilidade do código, evitando erros de digitação ou inconsistência.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 */
public enum TipoMovimentacao {

    /** Entrada de produtos no estoque. */
    ENTRADA,

    /** Saída de produtos do estoque. */
    SAIDA
}
