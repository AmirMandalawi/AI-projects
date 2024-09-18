import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Attributes {
    final String name;
    public TreeMap<String, Integer> counts;
    public TreeMap<String, Double> probs;

    int total = 0;

    public Attributes(String name) {
        this.name = name;
        this.counts = new TreeMap<String, Integer>();
        this.probs = new TreeMap<String, Double>();
    }

    public void calculateProbabilities() {
        for (String value : this.counts.keySet()) {
            int count = this.counts.get(value);
            double probability = (1.0 * count) / this.total;
            this.probs.put(value, probability);
        }
    }

    public static Attributes[] attributesList() {
        Attributes[] features = {
                createInstances("age",
                        new ArrayList<String>(Arrays.asList("10-19", "20-29", "30-39", "40-49", "50-59", "60-69",
                                "70-79", "80-89", "90-99"))),
                createInstances("menopause", new ArrayList<String>(Arrays.asList("lt40", "ge40", "premeno"))),
                createInstances("tumor-size",
                        new ArrayList<String>(Arrays.asList("0-4", "5-9", "10-14", "15-19", "20-24", "25-29", "30-34",
                                "35-39", "40-44",
                                "45-49", "50-54", "55-59"))),
                createInstances("inv-nodes",
                        new ArrayList<String>(
                                Arrays.asList("0-2", "3-5", "6-8", "9-11", "12-14", "15-17", "18-20", "21-23", "24-26",
                                        "27-29", "30-32", "33-35", "36-39"))),
                createInstances("node-caps", new ArrayList<String>(Arrays.asList("yes", "no"))),
                createInstances("deg-malig", new ArrayList<String>(Arrays.asList("1", "2", "3"))),
                createInstances("breast", new ArrayList<String>(Arrays.asList("left", "right"))),
                createInstances("breast-quad",
                        new ArrayList<String>(
                                Arrays.asList("left_up", "left_low", "right_up", "right_low", "central"))),
                createInstances("irradiat", new ArrayList<String>(Arrays.asList("yes", "no")))
        };
        return features;
    }

    public static Attributes createInstances(String name, List<String> values) {
        Attributes att = new Attributes(name);
        att.addAll(values);
        return att;
    }

    public void addAll(List<String> values) {
        values.forEach(value -> {
            this.counts.put(value, this.counts.getOrDefault(value, 0) + 1);
            this.total++;
        });
    }

    @Override
    public String toString() {
        String builder = "\n";
        builder += "  P(" + name + ")";
        for (String value : probs.keySet()) {
            builder += "\n  " + value + ": " + String.format("%.4f", probs.get(value));
        }
        builder += "\n==================";
        return builder;
    }

}
