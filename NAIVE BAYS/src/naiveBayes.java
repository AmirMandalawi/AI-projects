import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class naiveBayes {
    public static final classType[] types = { new classType("no-recurrence-events", Attributes.attributesList()),
            new classType("recurrence-events", Attributes.attributesList()) };
    public static String output = "\n>> Below are the results after training <<\n";

    public static void train(String[][] data) {
        for (String[] instance : data) {
            classType y = Arrays.stream(types)
                    .filter(t -> t.name.equals(instance[1]))
                    .findFirst()
                    .orElse(null);
            y.count++;
            IntStream.range(2, data.length).forEach(i -> {
                if (i < instance.length) {
                    if (y.atts[i - 2].counts.containsKey(instance[i])) {
                        int count = y.atts[i - 2].counts.get(instance[i]);
                        y.atts[i - 2].counts.put(instance[i], count + 1);
                    } else {
                        y.atts[i - 2].counts.put(instance[i], 1);
                    }
                }
            });
        }
        int classTotal = Arrays.stream(types)
                .peek(instance -> {
                    Arrays.stream(instance.atts).forEach(attribute -> {
                        attribute.total = attribute.counts.values().stream().mapToInt(Integer::intValue).sum();
                    });
                })
                .mapToInt(instance -> instance.count)
                .sum();

        Arrays.stream(types).forEach(instance -> {
            instance.prob = (1.0 * instance.count) / classTotal;
            Arrays.stream(instance.atts).forEach(attribute -> {
                attribute.calculateProbabilities();
            });
        });
    }

    private static double test(String[] instances, classType type) {
        double result = type.prob *
                IntStream.range(2, instances.length)
                        .mapToDouble(i -> {
                            return type.atts[i - 2].probs.getOrDefault(instances[i], 0.0);
                        })
                        .reduce(1.0, (a, b) -> a * b);
        output += (type.name + " " + result + "\n");
        return result;
    }

    private static classType bestType(String[] instance, List<classType> types) {
        Optional<classType> bestClass = types.stream()
                .reduce((y1, y2) -> test(instance, y1) > test(instance, y2) ? y1 : y2);
        return bestClass.orElse(null);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please insert two files names :  <training-set-file> <test-set-file>");
            System.exit(1);
        }
        train(Parser.getData(Parser.readAllLines(args[0])));
        output += "Class probabilities:\n" +
                "P(No-recurrence-events) = " + types[0].prob + "\n" +
                "P(Recurrence-events) = " + types[1].prob + "\n";

        output += "\n **  " + types[0].name + " **\n";
        output += Arrays.stream(types[0].atts)
                .map(Attributes::toString)
                .collect(Collectors.joining());

        output += "\n **  " + types[1].name + "  **\n";
        output += Arrays.stream(types[1].atts)
                .map(Attributes::toString)
                .collect(Collectors.joining());

        output += "\n >> Below are the results after testing <<\n\n";
        int numInstances = 0;
        double numCorrect = 0;
        String[][] testInstances = Parser.getData(Parser.readAllLines(args[1]));
        for (String[] instance : testInstances) {
            classType bestClass = bestType(instance, List.of(types));
            output += ("Predicted class label for " + numInstances + "th instance was " + bestClass.name + "\n");
            if (bestClass.name.equals(instance[1])) {
                output += "Actual class label was " + instance[1] + " and prediction was correct! \n";
                numCorrect++;
                output += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
                output += "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";
            } else {
                output += "Predicted class label was " + instance[1] + " and prediction was wrong! \n";
                output += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
                output += "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";
            }
            numInstances++;
        }
        output += "\nAccuracy: " + (numCorrect / numInstances) * 100.0 + "%\n";
        Parser.save("output.txt", output);
        System.out.println(output);
    }
}
