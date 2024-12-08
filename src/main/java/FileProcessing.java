import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class FileProcessing {
    String fileAddress;


    FileProcessing(String fileAddress) {
        this.fileAddress = fileAddress;
        File file = new File(fileAddress);
    }

    static void showOperations() {
        System.out.println("\nCOUNT WORDS [1]: ");
        System.out.println("COUNT LINES [2]: ");
        System.out.println("COUNT OCCURRENCE OF A SPECIFIC WORD [3]: ");
        System.out.println("REMOVE DUPLICATE LINES [4]: ");
        System.out.println("EXIT [5]: ");
    }

    long countWords() {
        try {
            return Files.lines(Paths.get(fileAddress))
                    .flatMap(line -> Arrays.stream(line.trim().split("//s")))
                    .count();
        } catch (IOException | SecurityException e)  {
            throw new RuntimeException(e);
        }
    }

    long countLines() {
        try {
            return Files.lines(Paths.get(fileAddress))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    long countSpecificWord(Scanner sc) {

        System.out.println("Enter word to be counted: ");
        String target = sc.next().toLowerCase();
        try {
            return Files.lines(Paths.get(fileAddress))
                    .flatMap(line -> Arrays.stream(line.trim().split("//s")))
                    .map(String :: toLowerCase)
                    .filter(word -> word.equals(target))
                    .count();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void removeDuplicates() {
        try {
            Set<String> uniqueLines = new LinkedHashSet<>(Files.readAllLines(Paths.get(fileAddress)));
            Files.write(Paths.get(fileAddress), uniqueLines);
            System.out.println("DUPLICATE LINES REMOVED FROM FILE");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void performOperations() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            showOperations();
            System.out.println("ENTER OPERATION: ");
            int operation = sc.nextInt();
            sc.nextLine();
            if (operation == 5) {
                break;
            }
            switch (operation) {
                case 1 -> System.out.println(countWords());
                case 2 -> System.out.println(countLines());
                case 3 -> System.out.println(countSpecificWord(sc));
                case 4 -> removeDuplicates();
                default -> System.out.println("INVALID OPTION");
            }
        }
    }

}
