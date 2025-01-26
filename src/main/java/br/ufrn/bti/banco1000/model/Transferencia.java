package br.ufrn.bti.banco1000.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que representa uma transferência entre contas bancárias.
 * Contém informações sobre a conta de origem, conta de destino,
 * valor, data e descrição da transferência.
 */
public class Transferencia {

    private final Conta contaOrigem;
    private final Conta contaDestino;
    private final double valor;
    private final Date data;
    private final String descricao;

    /**
     * Construtor da classe Transferencia.
     *
     * @param contaOrigem Conta de onde o valor será retirado.
     * @param contaDestino Conta para onde o valor será enviado.
     * @param valor Valor da transferência.
     * @param descricao Descrição da transferência.
     */
    public Transferencia(Conta contaOrigem, Conta contaDestino, double valor, String descricao) {
        validarTransferencia(contaOrigem, contaDestino, valor);

        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.data = new Date();
        this.descricao = descricao;

        realizarTransferencia();
    }

    /**
     * Valida os parâmetros da transferência.
     */
    private void validarTransferencia(Conta contaOrigem, Conta contaDestino, double valor) {
        if (contaOrigem == null || contaDestino == null) {
            throw new IllegalArgumentException("As contas de origem e destino não podem ser nulas.");
        }
        if (contaOrigem == contaDestino) {
            throw new IllegalArgumentException("As contas de origem e destino devem ser diferentes.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }
        if (contaOrigem.getSaldo() < valor) {
            throw new IllegalStateException("Saldo insuficiente na conta de origem.");
        }
    }

    /**
     * Realiza a transferência entre as contas.
     */
    private void realizarTransferencia() {
        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);

        contaOrigem.getMovimentacoes().add(
                new Movimentacao(
                        Movimentacao.TipoMovimentacao.TRANSFERENCIA_ENVIADA,
                        contaOrigem.getNumeroConta(),
                        "Transferência para conta " + contaDestino.getNumeroConta(),
                        valor
                )
        );

        contaDestino.getMovimentacoes().add(
                new Movimentacao(
                        Movimentacao.TipoMovimentacao.TRANSFERENCIA_RECEBIDA,
                        contaDestino.getNumeroConta(),
                        "Transferência recebida da conta " + contaOrigem.getNumeroConta(),
                        valor
                )
        );
    }

    // Getters
    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }

    public String getDataFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(data);
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna os dados da transferência em formato CSV.
     */
    public String toCsv() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.join(",",
                String.valueOf(contaOrigem.getNumeroConta()),
                String.valueOf(contaDestino.getNumeroConta()),
                String.valueOf(valor),
                descricao,
                sdf.format(data)
        );
    }

    /**
     * Reconstrói uma transferência a partir de uma linha CSV.
     */
    public static Transferencia fromCsv(String csv, Conta contaOrigem, Conta contaDestino) {
        String[] campos = csv.split(",");
        if (campos.length != 5) {
            throw new IllegalArgumentException("CSV inválido para criação de transferência.");
        }

        double valor = Double.parseDouble(campos[2]);
        String descricao = campos[3];
        Date data;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data = sdf.parse(campos[4]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data inválida no CSV.");
        }

        return new Transferencia(contaOrigem, contaDestino, valor, descricao);
    }

    @Override
    public String toString() {
        return "Transferencia{" +
                "contaOrigem=" + contaOrigem.getNumeroConta() +
                ", contaDestino=" + contaDestino.getNumeroConta() +
                ", valor=" + valor +
                ", data=" + getDataFormatada() +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}