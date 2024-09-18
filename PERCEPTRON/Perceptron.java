import java.io.FileNotFoundException;
import java.util.List;
import java.lang.*;

public class Perceptron {
    private List<PerceptronInstance> instancesList;
    private List<PerceptronInstance> testList;
    private List<PerceptronInstance> trainingList;

    public Perceptron(List<PerceptronInstance> instances, List<PerceptronInstance> testList,
            List<PerceptronInstance> trainingList) {
        this.instancesList = instances;
        this.testList = testList;
        this.trainingList = trainingList;
    }

    public void trainInstances() {
        double[] weights = new double[35];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 0;
        }
        double learningRate = 0.1;
        int count = 0;
        double accuracy = testInstances(instancesList, weights);
        while (accuracy < 0.95 && count < 1000) {
            for (PerceptronInstance instance : instancesList) {
                double output = weights[0];
                output = calculateBias(output, instance, weights);
                double y = output > 0 ? 1 : 0;
                double distance = instance.getCategory().equals("g") ? 1 : 0;
                checkAdjustment(y, distance, weights, learningRate, instance);
                if (testInstances(instancesList, weights) > 0.95) {
                    break;
                }
            }
            if (count % 10 == 0) {
                System.out.println("Epoch " + count + " Bias = " + weights[0] + " Accuracy = "
                        + testInstances(instancesList, weights));
            }
            count++;
        }
        System.out.println("Epoch " + count + " bias = " + weights[0]);
        System.out.println("Accuracy: " + testInstances(instancesList, weights));
        System.out.println("Weights: ");
        for (int i = 0; i < weights.length; i++) {
            System.out.print(weights[i] + "  ");
        }
    }

    public void trainSplitData() {
        double[] weights = new double[35];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 0;
        }
        double learningRate = 0.1;
        int count = 0;
        while (count < 500) {
            for (PerceptronInstance instance : testList) {
                double output = weights[0];
                output = calculateBias(output, instance, weights);
                double y = output > 0 ? 1 : 0;
                double distance = instance.getCategory().equals("g") ? 1 : 0;
                checkAdjustment(y, distance, weights, learningRate, instance);
            }
            if (count % 10 == 0) {
                System.out.println(
                        "Epoch " + count + " Bias = " + weights[0] + " Accuracy = "
                                + testInstances(trainingList, weights));
            }
            count++;
        }
        System.out.println("Epoch " + count + " Bias = " + weights[0]);
        System.out.println("Accuracy: " + testInstances(trainingList, weights));
        System.out.println("Final Weights: ");
        for (int i = 0; i < weights.length; i++) {
            System.out.print(weights[i] + "  ");
        }
    }

    public void checkAdjustment(double y, double distance, double[] weights, double learningRate,
            PerceptronInstance instance) {
        if (y == 0 && distance == 1) {
            weights[0] += learningRate;
            for (int i = 0; i < 34; i++) {
                weights[i + 1] += learningRate * instance.getFeatures().get(i);
            }
        }
        if (y == 1 && distance == 0) {
            weights[0] -= learningRate;
            for (int i = 0; i < 34; i++) {
                weights[i + 1] -= learningRate * instance.getFeatures().get(i);
            }
        }
    }

    public double calculateBias(double weight, PerceptronInstance instance, double[] weights) {
        double bias = weight;
        for (int i = 0; i < 34; i++) {
            bias += instance.getFeatures().get(i) * weights[i + 1];
        }
        return bias;
    }

    public double testInstances(List<PerceptronInstance> testList, double[] weights) {
        int correct = 0;
        for (PerceptronInstance instance : testList) {
            double bias = weights[0];
            bias = calculateBias(bias, instance, weights);
            String categoryPrediction = bias > 0 ? "g" : "b";
            if (categoryPrediction.equals(instance.getCategory())) {
                correct++;
            }
        }
        return (double) correct / testList.size();
    }

    public static void main(String[] args) throws FileNotFoundException {
        parser parser = new parser();
        if (args.length == 2) {
            parser.dataSeperation(args);
            Perceptron perceptron = new Perceptron(null, parser.getTrainList(), parser.getTestList());
            perceptron.trainSplitData();
            return;
        }
        if (args.length != 1) {
            System.out.println("Usage: java Percetron <filename>");
            System.exit(1);
        }
        Perceptron perceptron = new Perceptron(parser.readFromFile(args[0]), null, null);
        perceptron.trainInstances();
    }
}
