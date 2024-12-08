import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DataBackup {
    String src;
    String dest;

    void showOperations() {
        System.out.println("BACKUP FILES [1] ");
        System.out.println("BACKUP DIRECTORIES [2] ");
        System.out.println("RESTORE FROM BACKUP [3] ");
        System.out.println("COMPRESS FILE / DIRECTORY [4] ");
        System.out.println("EXIT [5] ");
    }

    void performOperations() throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            showOperations();
            System.out.println("ENTER OPERATION: ");
            int operation = sc.nextInt();
            sc.nextLine();
            if (operation == 5) { break; }

            switch (operation) {
                case 1 -> backupFiles(sc);
                case 2 -> backupDirectories(sc);
                case 3 -> restoreBackup(sc);
                case 4 -> compressBackup(sc);
                default -> System.out.println("INVALID OPTION");
            }
        }
    }

    void backupFiles(Scanner sc) throws IOException {
        getDetails(sc);
        List<String> files = List.of(src.split(","));
        Path backupPath = Paths.get(dest);

        if (!Files.isDirectory(backupPath)) {
            System.out.println("BACKUP PATH IS NOT A DIRECTORY");
            return;
        }

        for(String file: files) {
            Path source = Paths.get(file);
            Path target = Paths.get(dest + source.getFileName());
            if (!Files.exists(source)) {
                System.err.println("Source file does not exist: " + file);
                continue;
            }
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Backed up: " + source + " to " + target);
        }
    }

    void backupDirectories(Scanner sc) throws IOException {
        getDetails(sc);
        Path srcPath = Paths.get(src);
        Path backupPath = Paths.get(dest);

        if (!Files.exists(srcPath) || !Files.isDirectory(backupPath)) {
            throw new IOException("Source directory does not exist or is not a directory.");
        }

        Files.walk(srcPath).forEach(source -> {
            try {
                Path target = backupPath.resolve(srcPath.relativize(source));
                if (Files.isDirectory(source)) {
                    if (!Files.exists(target)) {
                        Files.createDirectories(target);
                    }
                } else {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                System.err.println("Error backing up file: " + source + " -> " + e.getMessage());
            }
        });
    }

    void getDetails(Scanner sc) {
        System.out.println("ENTER DIRECTORY TO BACKUP: ");
        src = sc.nextLine();    //"C:\Users\tarun\Desktop\BackupSrc"
        System.out.println("ENTER BACKUP DIRECTORY: ");
        dest = sc.nextLine();   //"C:\Users\tarun\Desktop\BackupDest"
    }

    void compressBackup(Scanner sc) throws IOException {
        getDetails(sc);
        Path source = Paths.get(src);
        Path destination = Paths.get(dest);

        if (!Files.exists(source)) {
            System.out.println("Source path does not exist.");
            return;
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(destination))) {
            if (Files.isDirectory(source)) {
                Files.walk(source).forEach(path -> {
                    try {
                        String zipname = source.relativize(path).toString();
                        if (!Files.isDirectory(path)) {
                            zipOut.putNextEntry(new ZipEntry(zipname + "/"));
                            zipOut.closeEntry();
                        } else {
                            zipOut.putNextEntry(new ZipEntry(zipname));
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        }
                    } catch (SecurityException | IOException e) {
                        System.out.println("Error compressing file: " + path);
                    }
                });
            } else {
                zipOut.putNextEntry(new ZipEntry(source.getFileName().toString()));
                Files.copy(source, zipOut);
                zipOut.closeEntry();
            }
        }  catch (IOException e) {
            System.out.println("Error during compression: " + e.getMessage());
        }
    }

    void restoreBackup(Scanner sc) throws IOException {
        getDetails(sc);
        Path source = Paths.get(src);
        Path destination = Paths.get(dest);
        ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(source));

    }
}

