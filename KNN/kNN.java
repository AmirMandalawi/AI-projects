import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kNN {
    public void knnAlgorithm(String trainingFile, String testFile, int k) throws IOException {
        List<Wine> trainWines = parse(trainingFile);
        List<Wine> testWines = parse(testFile);
        normalize(trainWines, testWines);
        int correct = 0;
        for (Wine testWine : testWines) {
            double[] minimumDistances = new double[k];
            Wine[] closestWine = new Wine[k];
            Arrays.fill(minimumDistances, Double.MAX_VALUE);
            for (Wine trainWine : trainWines) {
                double distance = calculateDistance(testWine, trainWine);
                for (int i = 0; i < minimumDistances.length; i++) {
                    if (distance < minimumDistances[i]) {
                        moveElements(minimumDistances, closestWine, i);
                        minimumDistances[i] = distance;
                        closestWine[i] = trainWine;
                        break;
                    }
                }
            }
            int prediction = majorityLabel(closestWine);
            if (prediction == testWine.classType()) {
                correct++;
            }
        }
        double accuracy = (double) correct / testWines.size() * 100;
        System.out.println("Accuracy : " + accuracy + "%");
        System.out.println("correct : " + correct);
        System.out.println("total : " + testWines.size());
    }

    public int majorityLabel(Wine[] classifications) {
        int[] counts = new int[3];
        int maxIndex = 0;
        for (Wine wine : classifications) {
            counts[wine.classType() - 1]++;
        }
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > counts[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex + 1;
    }

    public double calculateDistance(Wine wine1, Wine wine2) {
        double distance = 0;
        for (int i = 0; i < wine1.attributes().length; i++) {
            distance += Math.pow(wine1.getAttribute(i) - wine2.getAttribute(i), 2);
        }
        return distance;
    }

    public void normalize(List<Wine> trainWines, List<Wine> testWine) {
        double[] minValues = new double[trainWines.get(0).attributes().length];
        double[] maxValues = new double[trainWines.get(0).attributes().length];
        Arrays.fill(minValues, Double.MAX_VALUE);
        Arrays.fill(maxValues, Double.MIN_VALUE);
        for (int i = 0; i < trainWines.size(); i++) {
            for (int j = 0; j < trainWines.get(i).attributes().length; j++) {
                if (trainWines.get(i).attributes()[j] < minValues[j]) {
                    minValues[j] = trainWines.get(i).getAttribute(j);
                }
                if (trainWines.get(i).attributes()[j] > maxValues[j]) {
                    maxValues[j] = trainWines.get(i).getAttribute(j);
                }
            }
        }
        minmaxNormalisation(testWine, trainWines, maxValues, minValues);
    }
    
    public void minmaxNormalisation(List<Wine> testWine, List<Wine> trainWines, double[] maxValues, double[] minValues){
        for (int i = 0; i < testWine.size(); i++) {
            for (int j = 0; j < testWine.get(i).attributes().length; j++) {
                trainWines.get(i).attributes()[j] = (trainWines.get(i).attributes()[j] / (maxValues[j] - minValues[j]));
                testWine.get(i).attributes()[j] = (testWine.get(i).attributes()[j] / (maxValues[j] - minValues[j]));
            }
        }
    }

    public void moveElements(double[] distances, Wine[] wines, int index) {
        for (int j = distances.length - 1; j > index; j--) {
            distances[j] = distances[j - 1];
            wines[j] = wines[j - 1];
        }
    }

    public List<Wine> parse(String trainingFile) throws IOException {
        List<Wine> wines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(trainingFile))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(" ");
                double[] attributes = new double[fields.length - 1];
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = Double.parseDouble(fields[i]);
                }
                wines.add(new Wine(attributes, Integer.parseInt(fields[fields.length - 1])));
            }
        } catch (IOException e) {
            throw new IOException("Error reading file: " + trainingFile, e);
        }
        return wines;
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 3){
            System.out.println("Usage: java kNN <training file> <test file> <K>");
            System.out.println("Must provide the training file forst and then the test file second as inputs.");
            System.exit(1);
        }
        kNN algo = new kNN();
        algo.knnAlgorithm(args[0], args[1], Integer.parseInt(args[2]));
    }
}
