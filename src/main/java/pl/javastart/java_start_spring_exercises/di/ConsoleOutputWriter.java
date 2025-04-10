package pl.javastart.java_start_spring_exercises.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.javastart.java_start_spring_exercises.di.formatter.TextFormatter;

@Service
public class ConsoleOutputWriter {

    private final TextFormatter textFormatter;

    @Autowired
    public ConsoleOutputWriter(TextFormatter textFormatter) {
        this.textFormatter = textFormatter;
    }

    void println (String text){
        String formattedText = textFormatter.format(text);
        System.out.println(formattedText);
    }
}
