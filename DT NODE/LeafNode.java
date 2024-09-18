public class LeafNode implements tree {
    private String category;
    private double prob;

    public LeafNode(String category, double prob) {
        this.category = category;
        this.prob = prob;
    }

    public String getCategory() {
        return category;
    }

    public void report(String indent) {
        if (prob == 0) { // Error-checking
            System.out.printf("%sUnknown%n", indent);
        } else {
            System.out.printf("%sClass %s, prob=%.2f%n", indent, category, prob);
        }
    }

    public String toString() {
        return "LeafNode [category=" + category + ", prob=" + prob + "]";
    }

}
