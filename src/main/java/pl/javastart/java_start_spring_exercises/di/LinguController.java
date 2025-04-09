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

    public LinguController(EntryRepository entryRepository, FileService fileService, Scanner scanner) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.scanner = scanner;
    }

    void mainLoop() {
        System.out.println("Welcome to the LinguApp application.");
        Option option = null;
        do {
            printMenu();
            try {
                option = chooseOption();
                executeOption(option);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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
            System.out.println("Add at least one phrase to the database.");
            return;
        }
        final int testSize = Math.min(entryRepository.size(), TEST_SIZE);
        Set<Entry> randomEntries = entryRepository.getRandomEntries(testSize);
        int score = 0;
        for (Entry entry : randomEntries) {
            System.out.printf("Provide the translation for :\"%s\"\n", entry.getOriginal());
            String translation = scanner.nextLine();
            if(entry.getTranslation().equalsIgnoreCase(translation)) {
                System.out.println("Correct answer");
                score++;
            } else {
                System.out.println("Incorrect answer - " + entry.getTranslation());
            }
        }
        System.out.printf("Your score: %d/%d\n", score, testSize);
    }

    private void addEntry() {
        System.out.println("Enter the original phrase");
        String original = scanner.nextLine();
        System.out.println("Enter the translation");
        String translation = scanner.nextLine();
        Entry entry = new Entry(original, translation);
        entryRepository.add(entry);
    }

    private void close() {
        try {
            fileService.saveEntries(entryRepository.getAll());
            System.out.println("Application state saved");
        } catch (IOException e) {
            System.out.println("Failed to save changes");
        }
        System.out.println("Bye Bye!");
    }

    private void printMenu() {
        System.out.println("Choose an option:");
        for (Option option : Option.values()) {
            System.out.println(option);
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
