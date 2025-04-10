package pl.javastart.java_start_spring_exercises.di.crypto;

public interface CipherService {
    String encrypt(String text);
    String decrypt(String cipher);
}
