/*
Para o código da Main, foi feito o uso de Inteligencia Artificial Generativa (chatGPT) para auxiliar na
criação de uma interface interativa que simula a aba de cadastro e compra de produtos. Comando básicos
foram feitos pelo integrante Pedro Henrique Carvalho Pereira através de exemplos práticos disponíveis
nos sites anexados abaixo. 
A inteligencia artificial foi apenas utilizada no tratamento de erros, ajuda na criação dos botões
interativos, plotagem da tabela na interface e correção no trecho da seleção da quantidade de itens.


Fontes:
https://www.devmedia.com.br/introducao-a-interface-gui-no-java/25646
https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html (usado para a seleção da qunt de itens do produto)
https://www.alura.com.br/artigos/como-criar-interface-grafica-swing-java
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class Main extends JFrame {

    private Connection conexao;
    private ClienteDAO clienteDAO;
    private DroneDAO droneDAO;
    private EntregaDAO entregaDAO;

    private JTextField tfNome, tfEmail, tfEndereco, tfTelefone;
    private JButton btnProduto1, btnProduto2, btnProduto3;
    private DefaultTableModel tableModel;
    private JTable tabelaPedidos;

    private int contadorPedidosPendentes = 0;

    public Main() {
        // criar conexão com o banco de dados e inicialização da conexão das classes auxiliares de manipulação do banco
        conexao = BancoDeDados.conectar();
        clienteDAO = new ClienteDAO(conexao);
        droneDAO = new DroneDAO(conexao);
        entregaDAO = new EntregaDAO(conexao);



        // método interno que inicializa 5 drones fixos, porém pode ser alterado como quiser
        inicializarDrones();

        // configuração da janela (tamanho, título da GUI e etc)
        setTitle("Sistema de Pedidos com Drones");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // --- Painel principal com BoxLayout ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de cliente ---
        JPanel painelCliente = new JPanel(new GridLayout(4, 2, 5, 5));
        painelCliente.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

        tfNome = new JTextField(10);
        tfEmail = new JTextField(10);
        tfEndereco = new JTextField(10);
        tfTelefone = new JTextField(10);

        painelCliente.add(new JLabel("Nome:"));
        painelCliente.add(tfNome);
        painelCliente.add(new JLabel("Email:"));
        painelCliente.add(tfEmail);
        painelCliente.add(new JLabel("Endereço:"));
        painelCliente.add(tfEndereco);
        painelCliente.add(new JLabel("Telefone:"));
        painelCliente.add(tfTelefone);

        mainPanel.add(painelCliente);

        // --- Painel de produtos ---
        JPanel painelProdutos = new JPanel(new FlowLayout());
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Escolha um Produto"));

        btnProduto1 = new JButton("Produto 1");
        btnProduto2 = new JButton("Produto 2");
        btnProduto3 = new JButton("Produto 3");

        painelProdutos.add(btnProduto1);
        painelProdutos.add(btnProduto2);
        painelProdutos.add(btnProduto3);

        mainPanel.add(painelProdutos);

        // --- Tabela de pedidos ---
        tableModel = new DefaultTableModel(new String[]{"Cliente", "Email", "Pedido", "Status"}, 0);
        tabelaPedidos = new JTable(tableModel);
        tabelaPedidos.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        scrollPane.setPreferredSize(new Dimension(780, 200));
        mainPanel.add(scrollPane);

        add(mainPanel);

        // --- Ações dos produtos ---
        ActionListener pedidoListener = e -> registrarPedido(((JButton) e.getSource()).getText());
        btnProduto1.addActionListener(pedidoListener);
        btnProduto2.addActionListener(pedidoListener);
        btnProduto3.addActionListener(pedidoListener);

        atualizarTabela();

        setVisible(true);
    }

    /* metodo que inicializa os drones. A DAO e a estrutura criada são reaproveitaveis 
    para quantos drones forem necessários
    
     */
    private void inicializarDrones() {
        List<Drone> dronesExistentes = droneDAO.listarDrones();
        if (dronesExistentes.size() >= 5) return;

        for (int i = dronesExistentes.size(); i < 5; i++) {
            Drone d = new Drone(100, 5.0, true);
            droneDAO.inserirDrone(d);
        }
    }

    /* registro dos pedidos com tratamento de erros para endereço inválido ou campos não preenchidos na GUI */
    private void registrarPedido(String produto) {
        // validação dos campos
        if (tfNome.getText().isEmpty() || tfEmail.getText().isEmpty() || tfEndereco.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome, Email e Endereço.");
            return;
        }

        // validação de formato de endereço (TO DO: fazer com CEP para ser mais real)
        String endereco = tfEndereco.getText().trim();
        if (!endereco.matches("Rua\\s+.+\\s+\\d+")) {
            JOptionPane.showMessageDialog(this, "Formato inválido, use: Rua XXXXX NUMERO");
            return;
        }

        try {
            // cadastra cliente ou usa cliente já cadastrado
            String email = tfEmail.getText();
            Cliente cliente = clienteDAO.listarClientes().stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElseGet(() -> {
                        Cliente c = new Cliente(tfNome.getText(), tfEmail.getText(), endereco, tfTelefone.getText());
                        clienteDAO.inserirCliente(c);
                        List<Cliente> updated = clienteDAO.listarClientes();
                        return updated.get(updated.size() - 1);
                    });

            // --- Escolher quantidade de itens (1 a 3) ---
            int quantidadeItens = 1; // padrão
            String[] opcoes = {"1", "2", "3"};
            String q = (String) JOptionPane.showInputDialog(this, 
                "Escolha a quantidade de itens (1 a 3):", 
                "Quantidade de Itens", 
                JOptionPane.QUESTION_MESSAGE, 
                null, opcoes, opcoes[0]);
            if (q == null) return; // cancelou
            quantidadeItens = Integer.parseInt(q);

            // cada item pesa 1, limite do drone é 3 itens e cada drone é destinado a uma entrega
            double pesoPacote = quantidadeItens * 1.0;

            // cria a entrega para o pedido
            Entrega entrega = new Entrega(cliente, pesoPacote, "Pedido: " + produto + " (" + quantidadeItens + " itens)");
            entregaDAO.registrarEntrega(entrega);

            contadorPedidosPendentes++;

            // atualizar tabela na interface
            atualizarTabela();

            // ao chegar no quinto pedido, finaliza todas pendentes
            // lógica pensada para 5 drones deve ser alterada a medida que aumentam os drone
            if (contadorPedidosPendentes >= 5) {
                entregaDAO.finalizarEntregasPendentes();
                contadorPedidosPendentes = 0;
                atualizarTabela();
            }

            JOptionPane.showMessageDialog(this, "Pedido enviado para entrega");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // atualiza a tabela na interface, usa o metodo criado da DAO da entrega (junta dados do cliente, drone e status da entrega)
    private void atualizarTabela() {
        try {
            tableModel.setRowCount(0);
            List<Entrega> entregas = entregaDAO.listarEntregesComClienteEDrone(); // método que retorna lista completa
            for (Entrega e : entregas) {
                tableModel.addRow(new Object[]{
                        e.getCliente().getNome(),
                        e.getCliente().getEmail(),
                        e.getEnderecoDestino(),
                        e.getStatusEntrega()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}