import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
   
    public static List<String> readAllLines(String filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.err.println(" Error : " + e);
        }
        return new ArrayList<String>();
    }

    public static String[][] getData(List<String> rows) {
        String[][] variables = new String[rows.size() - 1][];
        for (int i = 1; i < rows.size(); i++) {
            variables[i - 1] = rows.get(i).split(",");
        }
        return variables;
    }
    
    public static void save(String filename, String content) {
        try {
            File f = new File(filename);
            f.createNewFile();
            PrintStream out = new PrintStream(f);
            out.print(content);
            out.close();
        } catch (IOException e) {
            System.err.println("Saving failed " + e);
        }
    }
}