# 🏥 Sistema de Gestão Hospitalar - Clínica Prisma

## 📌 Descrição do Projeto

O **Sistema de Gestão Hospitalar** é uma aplicação desktop desenvolvida em **Java**, utilizando a arquitetura **MVC (Model-View-Controller)** para garantir organização e escalabilidade.

O projeto simula o fluxo de trabalho de uma clínica médica, permitindo o gerenciamento de:

* 📅 Agendas
* 📄 Prontuários eletrônicos
* 👥 Perfis de acesso distintos (Médicos, Secretárias e Pacientes)

Um dos diferenciais técnicos é o sistema de **Persistência em Arquivos (JSON)**, implementado com a biblioteca **Gson**, que permite salvar todo o estado do sistema (incluindo o polimorfismo de documentos médicos) sem a necessidade de um banco de dados relacional instalado.

---

## 📚 Documentação e Modelagem

A documentação técnica completa do projeto pode ser encontrada na pasta **`/docs`** na raiz deste repositório.

Lá você encontrará:

* 📄 **Documentação do Código:** Detalhes sobre decisões de projeto, padrões adotados e justificativas arquiteturais.
* 📊 **Diagrama de Classes:** Estrutura completa das classes e seus relacionamentos (arquivo `DiagramaClasses.mermaid`).

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Gerenciamento de Build:** Maven
* **Interface Gráfica:** Java Swing
* **Persistência de Dados:** Google Gson (JSON)
* **Testes Unitários:** JUnit 4

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

* Java JDK 21 instalado
* Maven instalado
* Terminal (Linux, macOS ou Windows)

---

### ▶️ Passo a Passo

#### 1️⃣ Abrir o terminal na pasta raiz do projeto

Abra o terminal na pasta onde você descompactou ou clonou o projeto.

Para verificar se você está na pasta correta, execute:

##### 💻 Linux / macOS

```bash
ls
```

##### 💻 Windows

```bash
dir
```

O resultado deve conter, obrigatoriamente:

* A pasta `src`
* O arquivo `pom.xml`
* A pasta `docs`

Se esses arquivos estiverem visíveis, você está na pasta raiz correta.

---

#### 2️⃣ Compilar e Executar

O projeto está configurado para facilitar a execução via Maven. Execute o seguinte comando para limpar compilações antigas, baixar dependências e rodar o sistema:

```bash
mvn clean compile exec:java
```

Caso queira gerar o arquivo executável (**Fat JAR**) contendo todas as dependências, execute:

```bash
mvn clean package
```

O arquivo gerado estará em:

```
target/sistema-hospitalar-1.0-SNAPSHOT.jar
```

E pode ser executado com:

```bash
java -jar target/sistema-hospitalar-1.0-SNAPSHOT.jar
```

---

## 👤 Usuários Pré-Cadastrados para Teste

Para facilitar a avaliação, o sistema já vem populado com dados fictícios.

### 🩺 Médico — Dr. House

**Funcionalidades:**

* Gerenciar agenda
* Realizar atendimento
* Emitir receitas/atestados

**Credenciais:**

* 📧 Email: `house@hospital.com`
* 🔑 Senha: `123`

---

### 💼 Secretária — Pam Beesly

**Funcionalidades:**

* Cadastrar médicos e pacientes
* Gerenciar horários de todos os médicos
* Visualizar dashboard de plantão

**Credenciais:**

* 📧 Email: `pam@hospital.com`
* 🔑 Senha: `123`

---

### 😷 Paciente — Michael Scott

**Funcionalidades:**

* Visualizar histórico clínico

**Credenciais:**

* 📧 Email: `michael@dunder.com`
* 🔑 Senha: `123`

---

## 🚧 Roadmap e Próximos Passos

Este projeto foi desenvolvido como avaliação final de disciplina acadêmica (**V1.0**).

Melhorias planejadas para a **versão 2.0 (Pós-Semestre)**:

* [ ] **Refatoração de Arquitetura:** Substituir o acesso estático (`public static`) ao banco de dados em memória por padrão Singleton seguro.
* [ ] **Validação de Integridade:** Implementar verificação de CPF duplicado diretamente na camada Controller antes da instanciação.
* [ ] **Tratamento de Exceções:** Criar exceções personalizadas (`CpfDuplicadoException`, `ChoqueHorarioException`) para substituir lançamentos genéricos.
* [ ] **UX/UI:** Adicionar funcionalidade de remoção/edição de horários na grade de trabalho do médico.

---

## ✍️ Autores

* Gustavo Bersan
* João Vitor Almeida

Desenvolvido para a disciplina de **Programação Orientada a Objetos — UFJF**.
