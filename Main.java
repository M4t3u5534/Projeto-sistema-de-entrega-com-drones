/*
Main.java é um código que vai juntar todas funcionalidades em um menu
interativo através do terminal. Essa classe vai fazer a coleta dos inputs
do usuário e contém varios metodos que fazem a aquisição e uso das DAOs
implementadas durante o código.

Fontes:
https://www.devmedia.com.br/como-funciona-a-classe-scanner-do-java/28448 
https://www.dio.me/articles/como-usar-switch-case-no-java (usavamos em c e em python)
 */

import java.util.List;
import java.util.Scanner;

public class Main {
    // scanner para todos inputs (evita abrir e fechar multiplos scanners - boa prática)
    private static final Scanner sc = new Scanner(System.in);

    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final DroneDAO droneDAO = new DroneDAO();
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final EntregaDAO entregaDAO = new EntregaDAO();

    public static void main(String[] args) {
        /* Sistema de interação via terminal. Commit anterior usava GUI, mas foi necessário 'downgrade' para 
         * implementar recursos mais genéricos e que englobam melhor o projeto
         */
        while (true) {
            System.out.println("\n--- Sistema de Entregas com Drones ---");
            System.out.println("1. Cadastro de Cliente");
            System.out.println("2. Cadastro de Drone (admin)");
            System.out.println("3. Cadastro de Produto (admin)");
            System.out.println("4. Realizar Compra");
            System.out.println("5. Listar Entregas (admin)");
            System.out.println("6. Finalizar Entrega (admin)");
            System.out.println("7. Recarregar Drones (admin)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = sc.nextLine();

            switch (opcao) {
                case "1" -> cadastrarCliente();
                case "2" -> { if (validarSenhaAdmin()) cadastrarDrone(); }
                case "3" -> { if (validarSenhaAdmin()) cadastrarProduto(); }
                case "4" -> realizarCompra();
                case "5" -> { if (validarSenhaAdmin()) listarEntregas(); }
                case "6" -> { if (validarSenhaAdmin()) finalizarEntrega(); }
                case "7" -> { if (validarSenhaAdmin()) recarregarDrones(); }
                case "0" -> { System.out.println("Encerrando sistema."); return; }
                default -> System.out.println("Opção invalida.");
            }
        }
    }

    // CADASTRO CLIENTE 
    private static void cadastrarCliente() {
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine().trim();

            System.out.print("CEP (12345-678 ou 12345678): ");
            String cep = sc.nextLine().trim();

            System.out.print("Email (opcional): ");
            String email = sc.nextLine().trim();

            System.out.print("Telefone (opcional, apenas números): ");
            String telefone = sc.nextLine().trim();

            Cliente cliente = new Cliente(nome, cep, email.isEmpty() ? null : email,
                    telefone.isEmpty() ? null : telefone);

            clienteDAO.cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado. Seu ID é: " + cliente.getId());
        // tratamento de erro para qualquer dado errado, não insere e mostra erro apenas após o útlimo dado cadastrado
        } catch (IllegalArgumentException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }
    }

    // CADASTRO DRONE
    private static void cadastrarDrone() {
        try {
            System.out.print("ID do Drone (somente números): ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Status da bateria (0-100): ");
            int bateria = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Capacidade máxima (peso): ");
            int capacidade = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Disponível? (true/false): ");
            boolean disponivel = Boolean.parseBoolean(sc.nextLine().trim());

            Drone drone = new Drone(id, bateria, capacidade, disponivel);
            droneDAO.cadastrarDrone(drone);
            System.out.println("Drone cadastrado");
        // tratamento de erro para qualquer dado errado, não insere e mostra erro apenas após o útlimo dado cadastrado
        } catch (NumberFormatException e) {
            System.out.println("Erro: digite apenas numeros onde necessário.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // CADASTRO PRODUTO
    private static void cadastrarProduto() {
        try {
            System.out.print("ID do Produto: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Nome do Produto: ");
            String nome = sc.nextLine().trim();

            System.out.print("Peso do Produto: ");
            int peso = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Preço do Produto: ");
            double preco = Double.parseDouble(sc.nextLine().trim());

            Produto produto = new Produto(id, nome, peso, preco);
            produtoDAO.cadastrarProduto(produto);
            System.out.println("Produto cadastrado");

        } catch (NumberFormatException e) {
            System.out.println("Erro: digite valores numéricos válidos.");
        }
    }


    // REALIZAR COMPRA
    private static void realizarCompra() {
        try {
            System.out.print("Digite seu ID de cliente: ");
            String idStr = sc.nextLine().trim();
            if (!idStr.matches("\\d+")) {
                System.out.println("ID invalido, use apenas números.");
                return;
            }
            int idCliente = Integer.parseInt(idStr);
            Cliente cliente = clienteDAO.buscarClientePorId(idCliente);
            if (cliente == null) {
                System.out.println("Nenhum cliente encontrado para esse ID.");
                return;
            }

            List<Produto> produtos = produtoDAO.listarProdutos();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado");
                return;
            }

            System.out.println("Produtos disponíveis:");
            for (Produto p : produtos) {
                System.out.println(p.getId() + " - " + p.getNome() + " | Peso: " + p.getPeso() + " | Preço: " + p.getPreco());
            }

            System.out.print("Digite o ID do produto que deseja comprar: ");
            String idProdStr = sc.nextLine().trim();
            if (!idProdStr.matches("\\d+")) {
                System.out.println("ID de produto inválido.");
                return;
            }
            int idProduto = Integer.parseInt(idProdStr);

            Produto produto = produtoDAO.buscarProdutoPorId(idProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            Drone droneDisponivel = droneDAO.buscarDroneDisponivel(produto.getPeso());
            if (droneDisponivel == null) {
                System.out.println("Não há drones disponíveis para a entrega deste produto.");
                return;
            }



            Entrega entrega = new Entrega(idProduto, idCliente, droneDisponivel, produto.getPeso());
            entregaDAO.cadastrarEntrega(entrega);
            System.out.println("Compra feita, entrega cadastrada com ID: " + entrega.getId());

        } catch (NumberFormatException e) {
            System.out.println("Erro de entrada numérica: " + e.getMessage());
        }
    }

    
    // LISTAR ENTREGAS (tabela)
    private static void listarEntregas() {
        List<Entrega> entregas = entregaDAO.listarEntregas();
        // Se entregas estiver fazio, sinaliza
        if (entregas.isEmpty()) {
            System.out.println("Nenhuma entrega cadastrada");
            return;
        }
        // TOD0: metodo criado na main. Pode ser função em EntregaDAO
        System.out.printf("%-5s %-10s %-10s %-5s %-10s %-12s %-20s\n", // formatações pro cabeçalho
                "ID", "Produto", "Cliente", "Peso", "DroneID", "Status", "Data Compra");
        for (Entrega e : entregas) {
            System.out.printf("%-5d %-10d %-10d %-5d %-10d %-12s %-20s\n", // formatação pros dados
                    e.getId(), e.getIdProduto(), e.getIdCliente(), e.getPeso(),
                    e.getDrone().getId(), e.getStatus(), e.getDataCompra());
        }
    }

    // FINALIZAR ENTREGA
    private static void finalizarEntrega() {
        try {
            List<Entrega> pendentes = entregaDAO.listarEntregasPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("Não existem entregas pendentes");
                return;
            }

            System.out.println("Entregas pendentes:");
            for (Entrega e : pendentes) {
                System.out.printf("%d - Produto %d, Cliente %d, Peso %d, Drone %d\n",
                        e.getId(), e.getIdProduto(), e.getIdCliente(), e.getPeso(), e.getDrone().getId());
            }

            System.out.print("Digite o ID da entrega que deseja finalizar: ");
            int idEntrega = Integer.parseInt(sc.nextLine().trim());
            Entrega entrega = entregaDAO.buscarEntregaPorId(idEntrega);

            if (entrega == null || !entrega.getStatus().equals("Pendente")) {
                System.out.println("Entrega inválida.");
                return;
            }

            Drone drone = entrega.getDrone();
            int novaBateria = drone.getStatusBateria() - (int) (drone.getStatusBateria() * 0.2); // 20% de bateria
            drone.setStatusBateria(novaBateria);
            drone.setDisponivel(novaBateria >= 50);

            entrega.setStatus("Finalizado");
            entregaDAO.atualizarEntrega(entrega);
            droneDAO.atualizarDrone(drone);

            System.out.println("Entrega finlaizada e drone liberado");

        } catch (NumberFormatException e) {
            System.out.println("ID invalido");
        }
    }

    // RECARREGAR DRONES
    private static void recarregarDrones() {
        List<Drone> drones = droneDAO.listarDrones();
        for (Drone d : drones) {
            if (d.getStatusBateria() < 50) {
                d.setStatusBateria(100);
                d.setDisponivel(true);
                droneDAO.atualizarDrone(d);
            }
        }
        System.out.println("Todos os drones com bateria abaixo de 50% foram recarregados para 100%.");
    }

    // VALIDAÇÃO DE SENHA ADMIN
    private static boolean validarSenhaAdmin() {
        System.out.print("Digite a senha de administrador: ");
        String senha = sc.nextLine().trim(); 
        try {
            java.util.Properties prop = new java.util.Properties(); // cria properties para acessar o dado sensível (senha)
            java.io.FileInputStream input = new java.io.FileInputStream("config.properties");
            prop.load(input);
            input.close();
            String senhaAdmin = prop.getProperty("DB_PASS"); // armazena a variavel sensível

            // compara
            if (!senha.equals(senhaAdmin)) {
                System.out.println("Senha incorreta!");
                return false;
            }
        // tratamento de erro caso dê algo de errado na leitura do config.properties
        } catch (Exception e) {
            System.out.println("Erro ao ler senha do arquivo: " + e.getMessage());
            return false;
        }
        return true;


    }
}
