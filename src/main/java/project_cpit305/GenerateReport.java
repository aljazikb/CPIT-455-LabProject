package project_cpit305;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateReport {

    public static Path generateProductionReport(
            String machineName,
            int totalUnits,
            int totalRuns) throws IOException {

        if (machineName == null || machineName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Machine name is required."
            );
        }

        if (totalUnits < 0 || totalRuns < 0) {
            throw new IllegalArgumentException(
                    "Production totals cannot be negative."
            );
        }

        Path file = Paths.get("ProductionReport.txt").toAbsolutePath();

        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Close the writer automatically after saving.
        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {

            writer.write("========================================");
            writer.newLine();
            writer.write("           PRODUCTION REPORT");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();

            writer.write("Date: " + LocalDateTime.now().format(format));
            writer.newLine();
            writer.write("Scope: Current application session");
            writer.newLine();
            writer.newLine();

            writer.write("Machine Name  : " + machineName.trim());
            writer.newLine();
            writer.write("Total Runs    : " + totalRuns);
            writer.newLine();
            writer.write("Units Produced: " + totalUnits);
            writer.newLine();

            writer.write("========================================");
            writer.newLine();
            writer.write("             END OF REPORT");
            writer.newLine();
        }

        return file;
    }
}