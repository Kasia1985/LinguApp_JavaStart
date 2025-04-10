package pl.javastart.java_start_spring_exercises.di.formatter;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UpperCaseFormater implements TextFormatter{

    @Override
    public String format(String originalText) {
        return originalText.toUpperCase();
    }
}
