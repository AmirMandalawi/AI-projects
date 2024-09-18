import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DT {

    private List<Instance> allInstances;
    private List<Instance> testInstances;
    private List<String> attNames;
    private tree root;

    public DT() {
        this.allInstances = new ArrayList<>();
        this.attNames = new ArrayList<>();
    }

    public void dtAlgorithm(String trainingFile, String testFile) {
        DataReader data = new DataReader();
        DataReader testData = new DataReader();
        data.readDataFile(trainingFile);
        testData.readDataFile(testFile);
        this.testInstances = testData.getAllInstances();
        this.allInstances = data.getAllInstances();
        this.attNames = data.getAttNames();
        this.root = buildTree(allInstances, attNames);
        root.report("");
        testTree(root, testInstances);
    }

    public tree buildTree(List<Instance> instances, List<String> attributes) {
        if (instances.isEmpty()) {
            return getMajorCategory(this.allInstances);
        } else if (isPure(instances)) {
            return new LeafNode(instances.get(0).getCategory(), 1);
        } else if (attributes.isEmpty()) {
            return getMajorCategory(instances);
        }
        double average = Double.MAX_VALUE;
        List<Instance> bestInstancesTrue = new ArrayList<>();
        List<Instance> bestInstancesFalse = new ArrayList<>();
        String bestAttribute = null;
        for (int i = 0; i < attNames.size(); i++) {
            if (!attributes.contains(attNames.get(i))) {
                continue;
            }
            List<Instance> instancesTrue = new ArrayList<>();
            List<Instance> instancesFalse = new ArrayList<>();
            for (Instance instance : instances) {
                if (instance.getAtt(i)) {
                    instancesTrue.add(instance);
                } else {
                    instancesFalse.add(instance);
                }
            }
            double purityTrue = 1 - calculatePurity(instancesTrue);
            double purityFalse = 1 - calculatePurity(instancesFalse);
            double dt = (double) instancesTrue.size() / instances.size();
            double df = 1 - dt;

            double weightedPurity = dt * purityTrue + df * purityFalse;
            if (weightedPurity < average) {
                average = weightedPurity;
                bestAttribute = attNames.get(i);
                bestInstancesTrue = instancesTrue;
                bestInstancesFalse = instancesFalse;
            }
        }
        List<String> attsCopy = new ArrayList<>(attributes);
        attsCopy.remove(bestAttribute);
        tree left = buildTree(bestInstancesTrue, attsCopy);
        tree right = buildTree(bestInstancesFalse, attsCopy);
        return new dtNode(bestAttribute, left, right);
    }

    public double calculatePurity(List<Instance> lists) {
        Map<String, Integer> categoryCount = new HashMap<>();
        for (Instance instance : lists) {
            String category = instance.getCategory();
            if (categoryCount.containsKey(category)) {
                categoryCount.put(category, categoryCount.get(category) + 1);
            } else {
                categoryCount.put(category, 1);
            }
        }
        double purity = 0;
        for (String category : categoryCount.keySet()) {
            double probability = (double) categoryCount.get(category) / lists.size();
            purity += probability * probability;
        }
        return purity;
    }

    private boolean isPure(List<Instance> instances) {
        String category = instances.iterator().next().getCategory();
        for (Instance instance : instances) {
            if (!instance.getCategory().equals(category)) {
                return false;
            }
        }
        return true;
    }

    private LeafNode getMajorCategory(List<Instance> instances) {
        int liveCount = 0;
        int dieCount = 0;
        for (Instance instance : instances) {
            if (instance.getCategory().equals("live")) {
                liveCount++;
            } else {
                dieCount++;
            }
        }
        if (liveCount > dieCount) {
            return new LeafNode("live", (double) liveCount / instances.size());
        } else {
            return new LeafNode("die", (double) dieCount / instances.size());
        }
    }

    public void testTree(tree root, List<Instance> testList) {
        int total = 0;
        for (Instance test : testList) {
            root = this.root;
            while (root instanceof dtNode) {
                if (test.getAtt(attNames.indexOf(((dtNode) root).getBestAttribute()))) {
                    root = ((dtNode) root).getLeft();
                } else {
                    root = ((dtNode) root).getRight();
                }
            }
            if (test.getCategory().equals(((LeafNode) root).getCategory())) {
                total++;
            }
        }
        System.out.println("Accuracy: " + (double) total / testList.size() * 100 + "%");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java DT <training file> <test file>");
            System.out.println("Must provide the training file forst and then the test file second as inputs.");
            System.exit(1);
        }
        DT d = new DT();
        d.dtAlgorithm(args[0], args[1]);
    }
}
