import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class parser {
    private List<PerceptronInstance> trainList;
    private List<PerceptronInstance> testList;

    public parser(){
        this.trainList = new ArrayList<>();
        this.testList = new ArrayList<>();
    }

    public List<PerceptronInstance> readFromFile(String file) {
        List<PerceptronInstance> instances = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(file))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                instances.add(createPercetronInstance(line));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + file);
            System.exit(1);
        }
        return instances;
    }

    public PerceptronInstance createPercetronInstance(String line) {
        Scanner sc = new Scanner(line);
        List<Double> features = new ArrayList<>();
        while (sc.hasNextDouble()) {
            features.add(sc.nextDouble());
        }
        String category = sc.next();
        sc.close();
        return new PerceptronInstance(category, features);
    }

    public void dataSeperation(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Perceptron <train_file> <test_file>");
            System.exit(1);
        }
        this.trainList = readFromFile(args[0]);
        this.testList = readFromFile(args[1]);
    }

    public List<PerceptronInstance> getTrainList() {
        return trainList;
    }

    public List<PerceptronInstance> getTestList() {
        return testList;
    }
}
