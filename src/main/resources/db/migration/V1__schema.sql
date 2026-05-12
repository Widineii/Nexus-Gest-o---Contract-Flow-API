CREATE TABLE IF NOT EXISTS tb_fornecedor (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    nome_fantasia   VARCHAR(150)    NOT NULL,
    cnpj            CHAR(14)        NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    categoria       VARCHAR(30)     NOT NULL,
    criado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME        NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_fornecedor_cnpj UNIQUE (cnpj)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_fornecedor_categoria ON tb_fornecedor (categoria);

CREATE TABLE IF NOT EXISTS tb_contrato (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    numero_contrato   VARCHAR(20)     NOT NULL,
    valor_total       DECIMAL(15,2)   NOT NULL,
    data_inicio       DATE            NOT NULL,
    data_fim          DATE            NOT NULL,
    status            VARCHAR(20)     NOT NULL,
    fornecedor_id     BIGINT          NOT NULL,
    criado_em         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em     DATETIME        NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_contrato_numero UNIQUE (numero_contrato),
    CONSTRAINT fk_contrato_fornecedor FOREIGN KEY (fornecedor_id)
        REFERENCES tb_fornecedor (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_contrato_data_fim ON tb_contrato (data_fim);
CREATE INDEX idx_contrato_status   ON tb_contrato (status);
CREATE INDEX idx_contrato_fornecedor ON tb_contrato (fornecedor_id);

CREATE TABLE IF NOT EXISTS tb_usuario (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    email           VARCHAR(150)    NOT NULL,
    senha           VARCHAR(255)    NOT NULL,
    nome            VARCHAR(150)    NOT NULL,
    role            VARCHAR(20)     NOT NULL,
    criado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
