package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class ReportCustomizer {
    public static Path copyReports() throws IOException {
        Path source = Paths.get("target/karate-reports");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path destination = Paths.get("TestReports", timestamp);
        try(Stream<Path> paths = Files.walk(source)){
            paths.forEach(path -> {
                try {
                    Path target = destination.resolve(source.relativize(path));

                    if (Files.isDirectory(path)) {
                        Files.createDirectories(target);
                    } else {
                        Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING);
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Report copying failed", e);
                }
            });
        }



        return destination;
    }
}
