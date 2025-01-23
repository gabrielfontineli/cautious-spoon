package br.ufrn.bti.banco1000.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufrn.bti.banco1000.controller.ClienteController;
import br.ufrn.bti.banco1000.controller.ContaController;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.utils.ExportarCSV;

/**
 * Interface de texto para interação com o sistema bancário.
 */
public class BancoGUI {

    private final ClienteController clienteController;
    private final ContaController contaController;

    public BancoGUI() {
        this.clienteController = new ClienteController();
        this.contaController = new ContaController();
    }

    private boolean ehNumero(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("\\d+");
    }
    

    public static void main(String[] args) {
        BancoGUI bancoGUI = new BancoGUI();
        bancoGUI.executar();
    }

    /**
     * Método principal que executa o menu do sistema bancário.
     */
    public void executar() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Banco 1000 ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Criar Conta");
            System.out.println("3. Realizar Depósito");
            System.out.println("4. Realizar Saque");
            System.out.println("5. Realizar Transferência");
            System.out.println("6. Listar Clientes");
            System.out.println("7. Listar Contas");
            System.out.println("8. Exportar Dados");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            String input = scanner.nextLine(); // Consumir quebra de linha ou valor inválido
            
            if(!input.matches("\\d+")){
                System.out.println("Opção inválida.");
                continue;
            }
            
            int opcao = Integer.parseInt(input);
            
            switch (opcao) {
                case 1 -> cadastrarCliente(scanner);
                case 2 -> criarConta(scanner);
                case 3 -> realizarDeposito(scanner);
                case 4 -> realizarSaque(scanner);
                case 5 -> realizarTransferencia(scanner);
                case 6 -> listarClientes();
                case 7 -> listarContas();
                case 8 -> exportar();
                case 0 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarCliente(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        try {
            clienteController.cadastrarCliente(nome, cpf, email, telefone);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarConta(Scanner scanner) {
        System.out.print("CPF do Cliente: ");
        String cpf = scanner.nextLine();
        Cliente cliente = clienteController.buscarClientePorCpf(cpf);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.print("Nome da conta: ");
        String nome = scanner.nextLine();

        System.out.print("Agência: ");
        String agenciaInput = scanner.nextLine();
        if (!ehNumero(agenciaInput)) {
            System.out.println("Agência inválida. Deve conter apenas números.");
            return;
        }
        int agencia = Integer.parseInt(agenciaInput);

        System.out.print("Número da Conta: ");
        String numeroContaInput = scanner.nextLine();
        if (!ehNumero(numeroContaInput)) {
            System.out.println("Número da conta inválido. Deve conter apenas números.");
            return;
        }
        int numeroConta = Integer.parseInt(numeroContaInput);

        System.out.print("Tipo da Conta (1 - Corrente, 2 - Poupança): ");
        String tipoContaInput = scanner.nextLine();
        if (!ehNumero(tipoContaInput)) {
            System.out.println("Número da conta inválido. Deve conter apenas números.");
            return;
        }
        int tipoInput = scanner.nextInt();

        System.out.print("Senha da Conta: ");
        String senhaInput = scanner.next();
        if (!ehNumero(senhaInput)) {
            System.out.println("Senha inválida. Deve conter apenas números.");
            return;
        }
        int senha = Integer.parseInt(senhaInput);

        System.out.print("Saldo inicial: ");
        String saldoInput = scanner.next();
        double saldo;
        try {
            saldo = Double.parseDouble(saldoInput);
        } catch (NumberFormatException e) {
            System.out.println("Saldo inicial inválido. Deve ser um número.");
            return;
        }

        scanner.nextLine(); // Consumir quebra de linha

        Conta.TipoConta tipoConta = (tipoInput == 1) ? Conta.TipoConta.CORRENTE : Conta.TipoConta.POUPANCA;
        if(tipoInput == 1){

        }

        try {
            contaController.criarConta(nome, cliente, agencia, numeroConta, tipoConta, senha, saldo);
            System.out.println("Conta criada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarDeposito(Scanner scanner) {
        System.out.print("Agência: ");
        int agencia = scanner.nextInt();
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        System.out.print("Valor do Depósito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta conta = contaController.buscarConta(agencia, numeroConta);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        try {
            contaController.depositar(conta, valor);
            System.out.println("Depósito realizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarSaque(Scanner scanner) {
        System.out.print("Agência: ");
        int agencia = scanner.nextInt();
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        System.out.print("Valor do Saque: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta conta = contaController.buscarConta(agencia, numeroConta);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        try {
            contaController.sacar(conta, valor);
            System.out.println("Saque realizado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarTransferencia(Scanner scanner) {
        System.out.print("Agência da Conta Origem: ");
        int agenciaOrigem = scanner.nextInt();
        System.out.print("Número da Conta Origem: ");
        int numeroContaOrigem = scanner.nextInt();
        System.out.print("Agência da Conta Destino: ");
        int agenciaDestino = scanner.nextInt();
        System.out.print("Número da Conta Destino: ");
        int numeroContaDestino = scanner.nextInt();
        System.out.print("Valor da Transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir quebra de linha

        Conta contaOrigem = contaController.buscarConta(agenciaOrigem, numeroContaOrigem);
        Conta contaDestino = contaController.buscarConta(agenciaDestino, numeroContaDestino);

        if (contaOrigem == null || contaDestino == null) {
            System.out.println("Conta de origem ou destino não encontrada.");
            return;
        }

        try {
            contaController.transferir(contaOrigem, contaDestino, valor);
            System.out.println("Transferência realizada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("=== Clientes Cadastrados ===");
        clienteController.listarClientes().forEach(cliente -> 
                System.out.println("Nome: " + cliente.getNome() + ", CPF: " + cliente.getCpf()));
    }

    private void listarContas() {
        System.out.println("=== Contas Cadastradas ===");
        contaController.listarContas().forEach(conta -> 
                System.out.println("Conta: " + conta.getNumeroConta() + ", Agência: " + conta.getAgencia() +
                        ", Cliente: " + conta.getCliente().getNome() + ", Saldo: " + conta.getSaldo()));
    }

    private void exportar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Exportar Dados ===");
        System.out.println("1. Exportar Clientes");
        System.out.println("2. Exportar Contas");
        System.out.println("3. Exportar Movimentações");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        String pastaDestino = "dados_exportados";
        File pasta = new File(pastaDestino);

        if (!pasta.exists()) {
            pasta.mkdir(); 
        }

        String filePath;
        switch (opcao) {
            case 1 -> {
                List<Cliente> clientes = clienteController.listarClientes();

                if(clientes.isEmpty()){
                    System.out.println("Não há cliente para exportar");
                    return;
                }

                filePath = pastaDestino + File.separator + "clientes.csv";
                exportarClientes(filePath);
            }
            case 2 -> {
                List<Conta> contas = contaController.listarContas();

                if(contas.isEmpty()){
                    System.out.println("Não há contas para exportar");
                    return;
                }

                filePath = pastaDestino + File.separator + "contas.csv";
                exportarContas(filePath);
            }
            case 3 -> {
                List<String[]> rows = new ArrayList<>();
                List<Conta> contas = contaController.listarContas();

                for (Conta conta : contas) {
                    conta.getMovimentacoes().forEach(movimentacao -> {
                        rows.add(new String[]{
                                movimentacao.getTipo().name(),
                                movimentacao.getDescricao(),
                                String.valueOf(movimentacao.getValor()),
                                movimentacao.getData().toString(),
                                conta.getCliente().getNome(),
                                String.valueOf(conta.getNumeroConta())
                        });
                    });
                }

                if (rows.isEmpty()) {
                    System.out.println("Não há movimentações para exportar.");
                    return;
                }

                filePath = pastaDestino + File.separator + "movimentacoes.csv";
                exportarMovimentacoes(filePath);
            }
            default -> {
                System.out.println("Opção inválida.");
                return;
            }
        }
        System.out.println("Dados exportados com sucesso! Arquivo: " + filePath);
    }


    private void exportarClientes(String filePath) {
        List<Cliente> clientes = clienteController.listarClientes();
        List<String[]> rows = new ArrayList<>();

        for (Cliente cliente : clientes) {
            rows.add(new String[]{cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone()});
        }

        try {
            ExportarCSV.export(filePath, new String[]{"Nome", "CPF", "Email", "Telefone"}, rows);
        } catch (IOException e) {
            System.out.println("Erro ao exportar clientes: " + e.getMessage());
        }   
    }

    private void exportarContas(String filePath) {
        List<Conta> contas = contaController.listarContas();
        List<String[]> rows = new ArrayList<>();
    
        for (Conta conta : contas) {
            rows.add(new String[]{
                String.valueOf(conta.getAgencia()),
                String.valueOf(conta.getNumeroConta()),
                conta.getNome(),
                conta.getCliente().getNome(),
                conta.getTipo().name(),
                String.format("%.2f", conta.getSaldo())
            });
        }
    
        try {
            ExportarCSV.export(filePath, new String[]{"Agência", "Número Conta", "Nome", "Cliente", "Tipo Conta", "Saldo"}, rows);
        } catch (IOException e) {
            System.out.println("Erro ao exportar contas: " + e.getMessage());
        }
    }

    private void exportarMovimentacoes(String filePath) {

        List<String[]> rows = new ArrayList<>();
        
        List<Conta> contas = contaController.listarContas();
        for (Conta conta : contas) {

            conta.getMovimentacoes().forEach(movimentacao -> {
                rows.add(new String[]{
                    movimentacao.getTipo().name(),
                    movimentacao.getDescricao(),                 
                    String.valueOf(movimentacao.getValor()),     
                    movimentacao.getData().toString(),          
                    conta.getCliente().getNome(),               
                    String.valueOf(conta.getNumeroConta())      
                });
            });
        }
    
        String[] headers = {"Tipo", "Descrição", "Valor", "Data", "Cliente", "Conta"};
    
        try {
            ExportarCSV.export(filePath, headers, rows);
            System.out.println("Movimentações exportadas com sucesso! Arquivo: " + filePath);
        } catch (IOException e) {
            System.out.println("Erro ao exportar movimentações: " + e.getMessage());
        }
    }
    
}