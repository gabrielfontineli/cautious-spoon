package br.ufrn.bti.banco1000.test;

import br.ufrn.bti.banco1000.controller.ClienteController;
import br.ufrn.bti.banco1000.controller.ContaController;
import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {

    private ClienteController clienteController;
    private ContaController contaController;

    @BeforeEach
    void setup() {
        clienteController = new ClienteController();
        contaController = new ContaController();
    }

    @Test
    void testCadastroCliente() {
        clienteController.cadastrarCliente("João Silva", "12345678901", "joao@email.com", "99999-9999");

        Cliente cliente = clienteController.buscarClientePorCpf("12345678901");
        assertNotNull(cliente);
        assertEquals("João Silva", cliente.getNome());
        assertEquals("12345678901", cliente.getCpf());
    }

    @Test
    void testCriarConta() {
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "99999-9999");
        clienteController.cadastrarCliente(cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone());

        contaController.criarConta("Conta João", cliente, 1, 12345, Conta.TipoConta.CORRENTE, 1234, 1500.00);

        Conta conta = contaController.buscarConta(1, 12345);
        assertNotNull(conta);
        assertEquals(1500.00, conta.getSaldo());
    }

    @Test
    void testDeposito() {
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "99999-9999");
        clienteController.cadastrarCliente(cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone());

        contaController.criarConta("Conta João", cliente, 1, 12345, Conta.TipoConta.CORRENTE, 1234, 1000.00);
        Conta conta = contaController.buscarConta(1, 12345);

        contaController.depositar(conta, 500.00);
        assertEquals(1500.00, conta.getSaldo());
    }

    @Test
    void testSaque() {
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "99999-9999");
        clienteController.cadastrarCliente(cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getTelefone());

        contaController.criarConta("Conta João", cliente, 1, 12345, Conta.TipoConta.CORRENTE, 1234, 1000.00);
        Conta conta = contaController.buscarConta(1, 12345);

        contaController.sacar(conta, 200.00);
        assertEquals(800.00, conta.getSaldo());
    }

    @Test
    void testTransferencia() {
        Cliente clienteJoao = new Cliente("João Silva", "12345678901", "joao@email.com", "99999-9999");
        clienteController.cadastrarCliente(clienteJoao.getNome(), clienteJoao.getCpf(), clienteJoao.getEmail(), clienteJoao.getTelefone());

        Cliente clienteAna = new Cliente("Ana Souza", "98765432100", "ana@email.com", "88888-8888");
        clienteController.cadastrarCliente(clienteAna.getNome(), clienteAna.getCpf(), clienteAna.getEmail(), clienteAna.getTelefone());

        contaController.criarConta("Conta João", clienteJoao, 1, 12345, Conta.TipoConta.CORRENTE, 1234, 1500.00);
        contaController.criarConta("Conta Ana", clienteAna, 2, 67890, Conta.TipoConta.POUPANCA, 5678, 2000.00);

        Conta contaJoao = contaController.buscarConta(1, 12345);
        Conta contaAna = contaController.buscarConta(2, 67890);

        contaController.transferir(contaJoao, contaAna, 500.00);

        assertEquals(1000.00, contaJoao.getSaldo());
        assertEquals(2500.00, contaAna.getSaldo());
    }
}