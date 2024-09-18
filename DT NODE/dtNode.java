public class dtNode implements tree{
    private String bestAttribute;
    private tree left;
    private tree right;

    public dtNode(String attribute, tree left, tree right) {
        this.bestAttribute = attribute;
        this.left = left;
        this.right = right;
    }

    public String getBestAttribute() {
        return bestAttribute;
    }

    public tree getLeft(){
        return left;
    }

    public tree getRight(){
        return right;
    }

    public void report(String indent){
        System.out.printf("%s%s = True:%n", indent, bestAttribute);
        left.report(indent+"\t");
        System.out.printf("%s%s = False:%n", indent, bestAttribute);
        right.report(indent+"\t");
     }

    public String toString() {
        return "dtNode [ best attribute= " + bestAttribute + ", left=" + left + ", right=" + right + "]";
    }
}
