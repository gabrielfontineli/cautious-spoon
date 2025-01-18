package br.ufrn.bti.banco1000.utils;

import java.io.FileWriter;
import java.io.IOException;
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
}
