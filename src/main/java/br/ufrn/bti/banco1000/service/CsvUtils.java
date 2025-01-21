package br.ufrn.bti.banco1000.service;

import br.ufrn.bti.banco1000.model.Cliente;
import br.ufrn.bti.banco1000.model.Conta;
import br.ufrn.bti.banco1000.model.Movimentacao;
import br.ufrn.bti.banco1000.model.Conta.TipoConta;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvUtils {

    public static String clienteToCsv(Cliente cliente) {
        return cliente.getNome() + "," + cliente.getCpf() + "," + cliente.getEmail() + "," + cliente.getTelefone();
    }

    public static Cliente clienteFromCsv(String csv) {
        String[] dados = csv.split(",");
        return new Cliente(dados[0], dados[1], dados[2], dados[3]);
    }

    public static String contaToCsv(Conta conta) {
        return conta.getNome() + "," + conta.getCliente().getCpf() + "," + conta.getAgencia() + "," + conta.getNumeroConta() + "," + conta.getTipo() + "," + conta.getSaldo();
    }

    public static Conta contaFromCsv(String csv) {
        String[] dados = csv.split(",");
        Cliente cliente = new Cliente(dados[1], dados[1], "", "");
        return new Conta(dados[0], cliente, Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), TipoConta.valueOf(dados[4]), 0, Double.parseDouble(dados[5]));
    }

    public static String movimentacaoToCsv(Movimentacao movimentacao) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(movimentacao.getData()) + "," + movimentacao.getTipo() + "," + movimentacao.getCliente().getCpf() + "," + movimentacao.getDescricao() + "," + movimentacao.getValor();
    }

    public static Movimentacao movimentacaoFromCsv(String csv) {
        String[] dados = csv.split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = null;
        try {
            data = sdf.parse(dados[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Movimentacao(Movimentacao.TipoMovimentacao.valueOf(dados[1]), new Cliente(dados[2], dados[2], "", ""), dados[3], Double.parseDouble(dados[4]));
    }
}