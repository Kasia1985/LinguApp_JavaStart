package pl.javastart.java_start_spring_exercises.di;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

@Controller
public class LinguController {
    private static final int TEST_SIZE = 10;
    private final EntryRepository entryRepository;
    private final FileService fileService;
    private final Scanner scanner;
    private final ConsoleOutputWriter consoleOutputWriter;


    public LinguController(EntryRepository entryRepository, FileService fileService, Scanner scanner, ConsoleOutputWriter consoleOutputWriter) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.scanner = scanner;
        this.consoleOutputWriter = consoleOutputWriter;
    }

    void mainLoop() {
        consoleOutputWriter.println("Welcome to the LinguApp application.");
        Option option = null;
        do {
            printMenu();
            try {
                option = chooseOption();
                executeOption(option);
            } catch (IllegalArgumentException e) {
                consoleOutputWriter.println(e.getMessage());
            }
        } while (option != Option.EXIT);
    }

    private Option chooseOption() {
        int option = scanner.nextInt();
        scanner.nextLine();
        return Option.fromInt(option);
    }

    private void executeOption(Option option) {
        switch (option) {
            case ADD_ENTRY -> addEntry();
            case START_TEST -> startTest();
            case EXIT -> close();
        }
    }

    private void startTest() {
        if(entryRepository.isEmpty()) {
            consoleOutputWriter.println("Add at least one phrase to the database.");
            return;
        }
        final int testSize = Math.min(entryRepository.size(), TEST_SIZE);
        Set<Entry> randomEntries = entryRepository.getRandomEntries(testSize);
        int score = 0;
        for (Entry entry : randomEntries) {
            consoleOutputWriter.println(String.format("Provide the translation for :\"%s\"", entry.getOriginal()));
            String translation = scanner.nextLine();
            if(entry.getTranslation().equalsIgnoreCase(translation)) {
                consoleOutputWriter.println("Correct answer");
                score++;
            } else {
                consoleOutputWriter.println("Incorrect answer - " + entry.getTranslation());
            }
        }
        consoleOutputWriter.println(String.format("Your score: %d/%d\n", score, testSize));
    }

    private void addEntry() {
        consoleOutputWriter.println("Enter the original phrase");
        String original = scanner.nextLine();
        consoleOutputWriter.println("Enter the translation");
        String translation = scanner.nextLine();
        Entry entry = new Entry(original, translation);
        entryRepository.add(entry);
    }

    private void close() {
        try {
            fileService.saveEntries(entryRepository.getAll());
            consoleOutputWriter.println("Application state saved");
        } catch (IOException e) {
            consoleOutputWriter.println("Failed to save changes");
        }
        consoleOutputWriter.println("Bye Bye!");
    }

    private void printMenu() {
        consoleOutputWriter.println("Choose an option:");
        for (Option option : Option.values()) {
            consoleOutputWriter.println(option.toString());
        }
    }
    private static enum Option {
        ADD_ENTRY(1, "Add text with translation"),
        START_TEST(2, "Start the test"),
        EXIT(3, "End of the program");

        private final int optionNumber;
        private final String description;

        Option(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        static Option fromInt(int option) {
            if (option < 0 || option > values().length) {
                throw new IllegalArgumentException("There is no option with that number.");
            }
            return values()[option - 1];
        }

        @Override
        public String toString() {
            return String.format("%d - %s", optionNumber, description);
        }
    }
}
