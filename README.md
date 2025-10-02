## Diagrama de classes: <img width="780" height="798" alt="image" src="https://github.com/user-attachments/assets/b8bd9215-0497-4f31-9bdc-7ed22caf666b" />
## Diagrama de sequência: https://drive.google.com/file/d/1i6oVmwnCZChrRgjJcBhaAG81YV4v3oHp/view?usp=sharing


# Sistema de Entregas com Drones

## Descrição Geral
Este projeto implementa um sistema de e-commerce para gerenciamento de entregas utilizando drones.  
Ele permite o cadastro de clientes, drones e produtos, realização de compras, acompanhamento das entregas e gerenciamento do status dos drones, tudo via interface de terminal.  
O sistema foi desenvolvido em Java, utilizando conexões com banco de dados MySQL para mantimento dos dados.

O sistema foi modularizado, ou seja, consiste em classes separadas para **clientes**, **drones**, **produtos**, **entregas** e **DAOs** (responsáveis pela comunicação com o banco de dados via SQL).

---

## Funcionalidades Implementadas

- **Cadastro de Cliente**: coleta informações como nome, CEP, e-mail (opcional) e telefone (opcional). Recebe um ID para apresentar na hora de fazer a compra. 
- **Cadastro de Drone (admin)**: inclui ID, status da bateria, capacidade máxima e disponibilidade.  
- **Cadastro de Produto (admin)**: inclui ID, nome, peso e preço.  
- **Realização de Compra**: o cliente seleciona produtos disponíveis e o sistema atribui automaticamente um drone que tenha capacidade para o peso do pacote.  
- **Criação de Entregas**: cada compra gera uma entrega com associação entre cliente, produto e drone.  
- **Listagem de Entregas (admin)**: exibe todas as entregas cadastradas com detalhes de produto, cliente, drone e status.  
- **Finalização de Entrega (admin)**: atualiza o status da entrega para "Finalizado", reduz o nível de bateria do drone e libera drones disponíveis.  
- **Recarregamento de Drones (admin)**: recarrega drones com bateria abaixo de 50% para 100% e os torna disponíveis novamente.  
- **Integração com banco de dados MySQL**: usando `config.properties` para esconder dados sensíveis (como usuário e senha do banco).

### Exemplo de Menu no Terminal
```

--- Sistema de Entregas com Drones ---

1. Cadastro de Cliente
2. Cadastro de Drone (admin)
3. Cadastro de Produto (admin)
4. Realizar Compra
5. Listar Entregas (admin)
6. Finalizar Entrega (admin)
7. Recarregar Drones (admin)
8. Sair

```



---

## Requisitos de Segurança e Tratamento de Erros

O sistema inclui validações e tratamentos de erros para aumentar a confiabilidade e proteger os dados:  

- **Validação de inputs do usuário**:
  - Nome: apenas letras e espaços.  
  - CEP: formato `12345-678` ou `12345678`.  
  - E-mail: deve ter `@` e não ter espaços. (sem muito tratamento - apenas para exemplificar uma feature)
  - Telefone: apenas números.  
  - IDs e valores numéricos: devem ser inteiros ou decimais válidos.  

- **Tratamento de exceções com try-catch**:
  - Leitura de arquivos (`config.properties`).  
  - Conexão com o banco de dados (classe `BancoDeDados`).  
  - Inserção, consulta e atualização de registros no banco (DAOs).  

- **Validação de capacidade dos drones**:
  - Antes de criar uma entrega, o sistema verifica se há drones disponíveis com capacidade suficiente para o peso do produto selecionado.  

- **Verificação de senha de administrador**:
  - Operações administrativas exigem senha lida do arquivo `config.properties`.  
  - Garante que apenas usuários autorizados possam executar cadastros de drones e produtos, finalizar entregas e recarregar drones.  

- **Exemplos de validação específica**:
  - Peso do pacote: não excede capacidade do drone.  
  - CEP: valida formato correto.  
  - Status da bateria: deve estar entre 0 e 100.  
  - Dados do cliente: não são armazenados se forem inválidos (ex: números no campo nome).
  - (...) 

---

## Comunicação com Banco de Dados

O sistema utiliza **MySQL Connector/J** (`mysql-connector-java.jar`) para se conectar ao MySQL. Foi criado um banco de dados local que pode ser alterado apenas pelo administrador. Os comandos de criação e monitoramento foram feitos através do CMD(executado para administrador).

### Funcionamento:
1. **Arquivo de configuração `config.properties`**:
   - Armazena informações sensíveis: `DB_URL`, `DB_USER` e `DB_PASS`.  
   - Mantém os dados inacessíveis ao usuário comum.  

2. **Classe `BancoDeDados`**:
   - Cria a conexão com o MySQL usando `DriverManager.getConnection(URL, USUARIO, SENHA)`.  
   - Retorna a conexão para as DAOs.  

3. **DAOs (Data Access Objects)**:
   - `ClienteDAO`, `ProdutoDAO`, `DroneDAO` e `EntregaDAO` realizam operações específicas no banco:
     - Inserção (`INSERT`)  
     - Consulta (`SELECT`)  
     - Atualização (`UPDATE`)  
     - Listagem de dados completos ou filtrados  
   - Todas as operações estão envolvidas em try-catch para tratamento de erros de SQL ou de conexão.  

---

## Exemplos de Execução

### 1. Cadastro de Cliente
```

Nome: Pedro
CEP: 12345-678
Email (opcional): pedro@mail
Telefone (opcional): 11111111111
Cliente cadastrado. Seu ID é: 1

```

### 2. Cadastro de Drone (admin)
```

Digite a senha de administrador: DB_PASS
ID do Drone: 1
Status da bateria (0-100): 100
Capacidade máxima (peso): 5
Disponível? (true/false): true
Drone cadastrado

```

### 3. Cadastro de Produto (admin)
```

Digite a senha de administrador: DB_PASS
ID do Produto: 1
Nome do Produto: Drone Extra
Peso do Produto: 3
Preço do Produto: 1200.50
Produto cadastrado

```

### 4. Realizar Compra
```

Digite seu ID de cliente: 1
Produtos disponíveis:
1 - Drone Extra | Peso: 3 | Preço: 1200.5
Digite o ID do produto que deseja comprar: 1
Compra feita, entrega cadastrada com ID: 1

```

### 5. Listar Entregas (admin)
```

Digite a senha de administrador: DB_PASS
ID    Produto    Cliente    Peso   DroneID    Status       Data Compra
1     1          1          3      1          Pendente    2025-10-01T19:00

```

### 6. Finalizar Entrega (admin)
```

Digite a senha de administrador: DB_PASS
Entregas pendentes:
1 - Produto 1, Cliente 1, Peso 3, Drone 1
Digite o ID da entrega que deseja finalizar: 1
Entrega finalizada e drone liberado

```

### 7. Recarregar Drones (admin)
```

Digite a senha de administrador: DB_PASS
Todos os drones com bateria abaixo de 50% foram recarregados para 100%.

```
---
## Identificação dos Alunos
- Mateus Cerqueira Ribeiro - RA: 10443901
- Pedro Henrique Carvalho Pereira - RA: 10418861
