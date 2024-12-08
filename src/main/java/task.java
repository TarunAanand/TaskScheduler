import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class task {
    static PriorityQueue<Integer> taskOrder = new PriorityQueue<>(Collections.reverseOrder());
    static ConcurrentHashMap<Integer, Integer> tasks = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            showTasks();
            System.out.println("SELECT YOUR TASK: ");
            int taskNumber = sc.nextInt();
            if (taskNumber == 6) { break; }
            System.out.println("Enter Task priority [ from 1 to 15 ]");
            int taskPriority = sc.nextInt();
            sc.nextLine();

            tasks.put(taskPriority, taskNumber);
            taskOrder.add(taskPriority);
        }
        sc.nextLine();
        System.out.println(taskOrder);
        executeTask(taskOrder, sc);
    }

    private static void showTasks() {
        System.out.println("\t TASKS");
        System.out.println("EMAIL [1]");
        System.out.println("FILE PROCESSING [2]");
        System.out.println("DATA BACKUP [3]");
        System.out.println("REPORT GENERATION [4]");
        System.out.println("NOTIFICATION [5]");
        System.out.println("EXIT [6]");
    }

    private static void executeTask(PriorityQueue taskOrder, Scanner sc) throws IOException {
        while (!taskOrder.isEmpty()) {
            int priority = (int) taskOrder.poll();
            int taskNumber = tasks.get(priority);
            switch (taskNumber) {
                case 1 -> handleEmail(sc);
                case 2 -> handleFileProcessing(sc);
                case 3 -> handleDataBackup(sc);
                default -> System.out.println("INVALID OPTION");
            }
        }

    }


    private static void handleEmail(Scanner sc) {
        System.out.println("Enter From Address:");
        String from = sc.nextLine();    //"aegs.colab@gmail.com"
        System.out.println("Enter To Address:");
        String to = sc.nextLine();  //"tarunaananad27@gmail.com"
        System.out.println("Enter Subject:");
        String subject = sc.nextLine();
        System.out.println("Enter Content:");
        String content = sc.nextLine();

        System.out.println("Sending email from " + from + " to " + to + "...");
        Email email = new Email(from, to, subject, content);
        email.sendEmail();
    }

    private static void handleFileProcessing(Scanner sc) {
        System.out.println("ENTER YOUR FILE ADDRESS: ");
        String fileAddress = sc.nextLine();

        FileProcessing file = new FileProcessing(fileAddress);
        file.performOperations();
    }

    private static void handleDataBackup(Scanner sc) throws IOException {
        DataBackup data = new DataBackup();
        data.performOperations();
    }
}

