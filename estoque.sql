-- ====================================================
-- BANCO DE DADOS: Sistema de Estoque (versão compatível com backend Java)
-- ====================================================
DROP DATABASE IF EXISTS estoque_db;
CREATE DATABASE estoque_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE estoque_db;

-- ====================================================
-- TABELA: categoria
-- ====================================================
CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    embalagem ENUM('VIDRO', 'PLASTICO', 'LATA') NOT NULL,
    tamanho ENUM('PEQUENO', 'MEDIO', 'GRANDE') NOT NULL
);

-- ====================================================
-- TABELA: produto
-- ====================================================
CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    tipo_unidade VARCHAR(50),
    quantidade_atual INT DEFAULT 0,
    quantidade_minima INT DEFAULT 0,
    quantidade_maxima INT DEFAULT 0,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ====================================================
-- TABELA: movimentacao
-- ====================================================
CREATE TABLE movimentacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
    quantidade INT NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ====================================================
-- INSERINDO DADOS INICIAIS
-- ====================================================
INSERT INTO categoria (nome, embalagem, tamanho)
VALUES 
('Refrigerante', 'LATA', 'MEDIO'),
('Molho de Tomate', 'VIDRO', 'PEQUENO'),
('Detergente', 'PLASTICO', 'GRANDE');

INSERT INTO produto (nome, preco, tipo_unidade, quantidade_atual, quantidade_minima, quantidade_maxima, categoria_id)
VALUES
('Coca-Cola', 6.50, 'Lata 350ml', 100, 10, 200, 1),
('Molho Tradicional', 4.20, 'Vidro 340g', 50, 5, 100, 2),
('Detergente Neutro', 3.00, 'Frasco 500ml', 80, 10, 150, 3);
