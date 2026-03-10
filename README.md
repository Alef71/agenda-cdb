# 🗺️ Agenda-CdB - Sistema de Gestão para Barbearias

O **Agenda-CdB** é uma API REST desenvolvida para modernizar a gestão interna de barbearias. O foco do sistema é oferecer agilidade ao barbeiro, permitindo o controle total de sua agenda, catálogo de serviços, vendas de produtos e fechamento de caixa diário em uma interface mobile-first.

---

## 🚀 Funcionalidades Principal (MVP)

- **Gestão de Barbeiros:** Cadastro com aprovação do administrador e perfis de acesso (ADMIN/BARBEIRO).
- **Agenda Inteligente:** Grade flexível de 30 minutos com suporte a múltiplos blocos para serviços longos (ex: química/platinado).
- **Bloqueio de Horários:** Função "Pausa" para intervalos, almoço ou imprevistos.
- **Catálogo Dinâmico:** Gerenciamento de serviços com preços editáveis no momento da conclusão.
- **Loja Integrada:** Registro de vendas de produtos físicos (pomadas, óleos, etc).
- **Financeiro:** Fechamento de caixa diário automatizado com separação por forma de pagamento (PIX, Dinheiro, Cartão).
- **Integração WhatsApp:** Link direto para contato com o cliente e suporte a fotos personalizadas.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA** (Persistência de dados)
- **Spring Security** (Autenticação e Autorização)
- **PostgreSQL** (Banco de dados relacional)
- **Maven** (Gerenciador de dependências)

---

## 🏗️ Estrutura do Projeto

O projeto segue uma arquitetura em camadas para facilitar a manutenção:
- `models`: Entidades do banco de dados.
- `repositories`: Interfaces de comunicação com o banco.
- `services`: Regras de negócio do sistema.
- `controllers`: Endpoints da API.
- `dtos`: Objetos de transferência de dados para segurança da comunicação.
- `config`: Configurações de segurança (JWT) e CORS.

---

## 🏁 Como Executar o Projeto

1. Certifique-se de ter o **Java 17** e o **Maven** instalados.
2. Clone o repositório:
   ```bash
   git clone [https://github.com/Alef71/agenda-cdb.git](https://github.com/Alef71/agenda-cdb.git)
