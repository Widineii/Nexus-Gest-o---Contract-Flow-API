# Landing page — Nexus Contract Flow

Página estática profissional pra deploy no **Netlify** ou qualquer hospedagem de site estático.

## Estrutura

```
landing/
├── index.html       # Página única
├── styles.css       # Tema dark com gradientes
├── script.js        # Tabs de código + animação on scroll
├── netlify.toml     # Headers + cache (opcional)
└── _redirects       # /github e /repo redirecionam pro GitHub
```

## Deploy no Netlify — 2 minutos

### Opção A — Arrastar e soltar (mais rápido)

1. Acesse https://app.netlify.com/drop
2. Arraste a pasta `landing/` inteira pra área "Drag and drop your folder"
3. Pronto. URL gerada automaticamente (algo como `https://statuesque-syrniki-a62ad4.netlify.app`)
4. Em **Site settings → Change site name** você pode personalizar pra ex: `nexus-contract-flow`

### Opção B — Conectado ao GitHub (deploy contínuo)

1. No dashboard do Netlify: **Add new site → Import an existing project**
2. Conecte com GitHub e selecione `Nexus-Gest-o---Contract-Flow-API`
3. Configure:
   - **Base directory:** `landing`
   - **Build command:** _(deixe em branco)_
   - **Publish directory:** `landing`
4. Deploy. Toda vez que você der `git push`, o Netlify atualiza sozinho.

## Customizações rápidas

- **Logo/branding:** `index.html` linhas 23-26 (mude `NX` e `Nexus.Contract Flow`)
- **Cores:** `styles.css` topo (variáveis CSS `--primary` e `--accent`)
- **GitHub URL:** procure por `Widineii/Nexus-Gest-o---Contract-Flow-API` no `index.html` e substitua se mudar de repositório
- **Conteúdo das features:** seção `#features` no HTML

## Por que landing estática em vez de Swagger ao vivo?

- ✅ **Grátis pra sempre** no Netlify (sem cold start, sem cartão)
- ✅ **Visual profissional** que recrutador vê em 5 segundos
- ✅ **Não depende** de backend rodando 24/7
- ✅ O código real está sempre acessível no GitHub
- ✅ Pra rodar o Swagger interativo de verdade: o README do repositório explica como subir local em 1 comando (`run.bat`)
