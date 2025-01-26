package br.ufrn.bti.banco1000.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportarCSV {

    /**
     *
     * @param filePath
     * @param headers 
     * @param rows
     * @throws IOException
     */
    public static void export(String filePath, String[] headers, List<String[]> rows) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {

            if (headers != null) {
                writer.append(String.join(",", headers)).append("\n");
            }
            
            for (String[] row : rows) {
                writer.append(String.join(",", row)).append("\n");
            }
        }
    }
    /*
     * Importa dados de um arquivo CSV.
     *
     * @param filePath Caminho do arquivo CSV.
     * @return Lista de linhas importadas. Cada linha é representada como um array de Strings.
     * @throws IOException Em caso de erro de leitura do arquivo.
     */
    public static List<String[]> importar(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                // Ignora a primeira linha (cabeçalho)
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Divide a linha em colunas
                rows.add(linha.split(","));
            }
        }

        return rows;
    }
}
    
