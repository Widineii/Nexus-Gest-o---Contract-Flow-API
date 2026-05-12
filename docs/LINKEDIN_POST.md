# Post para LinkedIn — Nexus Gestão · Contract Flow

> Copie e cole no LinkedIn. Anexe a imagem `docs/banner.png` (ou os 3 prints: banner, swagger-ui e arquitetura) no momento da publicação.

---

## Versão 1 — Curta e profissional (recomendada)

🚀 Acabei de subir mais um projeto no meu portfólio: **Nexus Gestão — Contract Flow API**

Um backend corporativo em **Java 17 + Spring Boot 3.5** para controle do ciclo de vida de contratos com fornecedores. Pensado pra simular o tipo de sistema que empresas usam no dia a dia: cadastro de fornecedores, contratos com vigência, alertas automáticos e dashboard de gastos.

🔧 O que está aplicado nesse projeto:

→ Autenticação stateless com JWT (Spring Security + BCrypt)
→ Arquitetura em camadas: Controller · Service · Repository · Entity
→ DTOs com Bean Validation (incluindo validador customizado de CNPJ com dígito verificador)
→ MapStruct para mapeamento Entity ↔ DTO sem boilerplate
→ Job agendado (@Scheduled) que atualiza contratos vencidos toda meia-noite
→ Tratamento global de exceções com mensagens padronizadas
→ Documentação interativa via Swagger / OpenAPI 3
→ Migrations versionadas com Flyway (MySQL) + perfil local com H2
→ Testes unitários de regras de negócio (JUnit 5 + Mockito)

💡 Diferencial: o repositório vem com um script `run.ps1` que baixa o Maven automaticamente e sobe a aplicação em modo zero-config (H2 em memória) — quem clonar abre o Swagger e testa em minutos, sem instalar nada além do Java.

🔗 Repositório completo: https://github.com/Widineii/Nexus-Gest-o---Contract-Flow-API

#java #springboot #backend #api #rest #jwt #mysql #portfolio #desenvolvedor #programacao

---

## Versão 2 — Mais "história" (storytelling)

Quando eu comecei a estudar Java, fazia CRUD de “lista de tarefas” pra praticar. Hoje, depois de bastante estudo, montei um projeto que se parece de verdade com o que rola no mercado:

**Nexus Gestão — Contract Flow API**, um backend corporativo para gestão de contratos com fornecedores. 📑

A ideia: simular o cenário de uma empresa que precisa controlar dezenas de contratos ativos, com datas de vencimento, valores, integridade de dados e relatórios — exatamente o tipo de sistema que recrutadores procuram em vagas Java pleno/sênior.

⚙️ Tecnologias e práticas aplicadas:

• Java 17 + Spring Boot 3.5 + Maven
• Spring Security com JWT (stateless, BCrypt, roles)
• Spring Data JPA + Hibernate + MySQL 8 + Flyway
• Bean Validation (`@CNPJ` customizado, `@Digits`, `@Email`, `@AssertTrue`)
• MapStruct + Lombok para reduzir boilerplate
• Tarefa agendada (`@Scheduled`) para atualização automática de status
• Tratamento global de exceções com `@RestControllerAdvice`
• Documentação interativa com Swagger / OpenAPI 3
• Perfil local com H2 (zero-config) e perfil dev/prod com MySQL
• Testes unitários cobrindo as regras de negócio

📂 GitHub: https://github.com/Widineii/Nexus-Gest-o---Contract-Flow-API

Esse é o tipo de exercício que mais me ensinou: pegar uma especificação real e montar uma API completa, do esquema do banco até a documentação interativa.

Se você tá começando ou quer trocar uma ideia sobre Java/Spring, manda mensagem que eu adoro conversar sobre código.

#java #springboot #backenddeveloper #api #jwt #springsecurity #mysql #portfolio #vagajava #devbrasil

---

## Sugestão de imagens para anexar

1. **`docs/banner.png`** — banner principal, ideal como primeira imagem do post.
2. **`docs/swagger-ui.png`** — print da Swagger UI rodando (mostra o "produto funcionando").
3. **`docs/arquitetura.png`** — diagrama em camadas (mostra que você sabe organizar código).

O LinkedIn deixa subir até 9 imagens. Use as 3 e o post vira praticamente um "carrossel".

---

## Dicas extras

- Publique entre **terça e quinta, manhã (9h-11h)** ou **fim de tarde (17h-19h)**.
- Marque pessoas/empresas relevantes (ex: recrutadores Java que você segue).
- Responda comentários nas primeiras 1–2 horas — o algoritmo dá boost.
- Adicione uma **descrição curta** no repo GitHub: "Backend Spring Boot 3 + Java 17 para gestão de contratos com fornecedores. JWT, JPA, Swagger, Flyway, Scheduler."
- Adicione **topics** no repo: `java`, `spring-boot`, `rest-api`, `jwt`, `mysql`, `swagger`, `portfolio`, `backend`.
