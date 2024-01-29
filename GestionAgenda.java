import java.io.*;
import java.util.*;

public class GestionAgenda {
    private static final String FILE_PATH = "/c:/Users/artur/OneDrive/Documentos/Ej-entornos/agenda.dat";
    private static final int RECORD_SIZE = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Phone Book Management");
            System.out.println("1. Add Contact");
            System.out.println("2. Delete Contact");
            System.out.println("3. Show Phone Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    deleteContact();
                    break;
                case 3:
                    showPhoneBook();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addContact() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();

            file.seek(file.length());
            file.writeUTF(String.format("%-" + (RECORD_SIZE - 4) + "s", name));
            file.writeUTF(String.format("%-" + (RECORD_SIZE - 4) + "s", phoneNumber));

            System.out.println("Contact added successfully.");

            scanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while adding contact.");
        }
    }

    private static void deleteContact() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter name to delete: ");
            String nameToDelete = scanner.nextLine();

            long currentPosition = 0;
            boolean found = false;

            while (currentPosition < file.length()) {
                file.seek(currentPosition);
                String name = file.readUTF().trim();
                file.readUTF(); // Skip phone number

                if (name.equalsIgnoreCase(nameToDelete)) {
                    found = true;
                    break;
                }

                currentPosition += RECORD_SIZE;
            }

            if (found) {
                file.seek(currentPosition);
                file.writeUTF(String.format("%-" + (RECORD_SIZE - 4) + "s", ""));
                file.writeUTF(String.format("%-" + (RECORD_SIZE - 4) + "s", ""));
                System.out.println("Contact deleted successfully.");
            } else {
                System.out.println("Contact not found.");
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while deleting contact.");
        }
    }

    private static void showPhoneBook() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r")) {
            System.out.println("Phone Book:");

            long currentPosition = 0;

            while (currentPosition < file.length()) {
                file.seek(currentPosition);
                String name = file.readUTF().trim();
                String phoneNumber = file.readUTF().trim();

                if (!name.isEmpty()) {
                    System.out.println("Name: " + name);
                    System.out.println("Phone Number: " + phoneNumber);
                    System.out.println("--------------------");
                }

                currentPosition += RECORD_SIZE;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while showing phone book.");
        }
    }
}
