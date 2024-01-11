package main.mapReduce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class for reading the content of a text file and returning it as a string.
 */
public class TextReader {

    /**
     * Reads the content of the specified file and returns it as a string.
     *
     * @param filePath The path to the file to be read.
     * @return The content of the file as a string.
     */
    public static String readText(String filePath) {
        StringBuilder content = new StringBuilder();
        char[] buffer = new char[1024]; // Block size, you can adjust according to your needs

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath, StandardCharsets.UTF_8))) {
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // Handle file reading exceptions (you can customize according to your needs)
            e.printStackTrace();
        }
        return content.toString();
    }
}
