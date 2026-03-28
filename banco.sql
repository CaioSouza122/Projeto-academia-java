-- ============================================================
--  GymFlow - Script de criação do banco de dados
-- ============================================================

-- Tabela central de usuários (alunos, professores e recepcionistas)
CREATE TABLE IF NOT EXISTS usuarios (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    cpf             VARCHAR(14)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    senha           VARCHAR(100) NOT NULL,
    tipo            VARCHAR(20)  NOT NULL CHECK (tipo IN ('aluno','professor','recepcionista')),
    professor_id    INT REFERENCES usuarios(id) ON DELETE SET NULL,
    plano_id        INT,
    ativo           BOOLEAN DEFAULT TRUE
);

-- Tabela de planos da academia
CREATE TABLE IF NOT EXISTS planos (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(60) NOT NULL,
    valor           NUMERIC(10,2) NOT NULL,
    descricao       TEXT,
    duracao_meses   INT NOT NULL
);

-- Adiciona FK de usuarios -> planos (separado para evitar dependência circular)
ALTER TABLE usuarios
    ADD CONSTRAINT fk_plano FOREIGN KEY (plano_id) REFERENCES planos(id) ON DELETE SET NULL;

-- Tabela de pagamentos
CREATE TABLE IF NOT EXISTS pagamentos (
    id                SERIAL PRIMARY KEY,
    aluno_id          INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    plano_id          INT NOT NULL REFERENCES planos(id),
    data_pagamento    DATE NOT NULL DEFAULT CURRENT_DATE,
    data_vencimento   DATE NOT NULL,
    status            VARCHAR(20) NOT NULL CHECK (status IN ('pago','pendente','vencido'))
);

-- Tabela de treinos (um treino por aluno, criado pelo professor)
CREATE TABLE IF NOT EXISTS treinos (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    aluno_id        INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    professor_id    INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    descricao       TEXT,
    data_criacao    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de exercícios (pertence a um treino)
CREATE TABLE IF NOT EXISTS exercicios (
    id              SERIAL PRIMARY KEY,
    treino_id       INT NOT NULL REFERENCES treinos(id) ON DELETE CASCADE,
    nome            VARCHAR(100) NOT NULL,
    series          INT NOT NULL,
    repeticoes      INT NOT NULL,
    descanso_seg    INT DEFAULT 60
);

-- ============================================================
--  Dados iniciais de exemplo
-- ============================================================

-- Planos
INSERT INTO planos (nome, valor, descricao, duracao_meses) VALUES
    ('Básico',    89.90,  'Acesso à musculação',                          1),
    ('Intermediário', 129.90, 'Musculação + aulas em grupo',              1),
    ('Premium',   179.90, 'Acesso completo + acompanhamento nutricional', 1)
ON CONFLICT DO NOTHING;

-- Recepcionista padrão (senha: 1234)
INSERT INTO usuarios (nome, cpf, email, senha, tipo) VALUES
    ('Recepção Admin', '000.000.000-00', 'recepcao@gymflow.com', '1234', 'recepcionista')
ON CONFLICT DO NOTHING;
