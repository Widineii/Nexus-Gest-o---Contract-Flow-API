-- Usuario administrador padrao (senha: admin123)
-- Hash BCrypt gerado com força 10 para a senha "admin123"
INSERT INTO tb_usuario (email, senha, nome, role) VALUES
('admin@nexus.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador Nexus', 'ADMIN');

INSERT INTO tb_fornecedor (nome_fantasia, cnpj, email, categoria) VALUES
('TechSolutions Brasil',     '12345678000190', 'contato@techsolutions.com.br', 'TI'),
('LimpaTudo Servicos',       '98765432000110', 'comercial@limpatudo.com.br',   'LIMPEZA'),
('ManutencaoPro Ltda',       '45678912000155', 'atendimento@manutencaopro.com.br', 'MANUTENCAO'),
('LogisticaExpress',         '78912345000133', 'vendas@logisticaexpress.com.br',  'LOGISTICA');

INSERT INTO tb_contrato (numero_contrato, valor_total, data_inicio, data_fim, status, fornecedor_id) VALUES
('CT-2026-001', 120000.00, '2026-01-15', '2026-12-31', 'ATIVO',    1),
('CT-2026-002',  48000.00, '2026-02-01', '2027-01-31', 'ATIVO',    2),
('CT-2026-003',  86500.50, '2026-03-10', '2026-06-30', 'ATIVO',    3),
('CT-2025-099',  35000.00, '2025-06-01', '2025-12-31', 'VENCIDO',  4),
('CT-2026-004',  72000.00, '2026-04-01', '2026-09-30', 'SUSPENSO', 1);
