-- ============================================================
-- Produto: saldo GLOBAL (produto.quantidade_exata) + saldo POR ARMAZÉM
--          (produto_stock.quantidade_exata)
--
-- PostgreSQL
-- ============================================================

-- 1) TABELA produto_stock (saldo por armazém)
CREATE TABLE IF NOT EXISTS produto_stock (
    fk_portfolio     VARCHAR(50) NOT NULL,
    fk_armazem       INTEGER     NOT NULL,
    quantidade_exata INTEGER     NOT NULL DEFAULT 0,

    CONSTRAINT pk_produto_stock PRIMARY KEY (fk_portfolio, fk_armazem),
    CONSTRAINT fk_produto_stock_portfolio FOREIGN KEY (fk_portfolio)
        REFERENCES portfolio(pk_portfolio),
    CONSTRAINT fk_produto_stock_armazem FOREIGN KEY (fk_armazem)
        REFERENCES armazem(pk_armazem)
);

CREATE INDEX IF NOT EXISTS ix_produto_stock_armazem
    ON produto_stock (fk_armazem);

-- 2) RECALCULAR saldo POR ARMAZÉM a partir do histórico
INSERT INTO produto_stock (fk_portfolio, fk_armazem, quantidade_exata)
SELECT
    m.fk_portfolio,
    m.fk_armazem,
    COALESCE(SUM(
        CASE
            WHEN m.tipo_movimento = 'E' THEN m.quantidade
            WHEN m.tipo_movimento = 'S' THEN -m.quantidade
            ELSE 0
        END
    ), 0) AS saldo
FROM movimentacao_inventario m
WHERE m.fk_armazem IS NOT NULL
GROUP BY m.fk_portfolio, m.fk_armazem
ON CONFLICT (fk_portfolio, fk_armazem)
DO UPDATE SET quantidade_exata = EXCLUDED.quantidade_exata;

-- 3) RECALCULAR saldo GLOBAL a partir do histórico
UPDATE produto p
SET quantidade_exata = x.saldo
FROM (
    SELECT
        m.fk_portfolio,
        COALESCE(SUM(
            CASE
                WHEN m.tipo_movimento = 'E' THEN m.quantidade
                WHEN m.tipo_movimento = 'S' THEN -m.quantidade
                ELSE 0
            END
        ), 0) AS saldo
    FROM movimentacao_inventario m
    GROUP BY m.fk_portfolio
) x
WHERE p.fk_portfolio = x.fk_portfolio;

-- Produtos sem histórico ficam com 0
UPDATE produto p
SET quantidade_exata = 0
WHERE NOT EXISTS (
    SELECT 1
    FROM movimentacao_inventario m
    WHERE m.fk_portfolio = p.fk_portfolio
);

COMMIT;
