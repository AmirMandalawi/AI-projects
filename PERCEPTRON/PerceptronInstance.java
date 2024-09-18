import java.util.List;

public class PerceptronInstance {
    
    private String categoryName;
    private List<Double> features;
    public PerceptronInstance(String categoryName, List<Double> features)  {
        this.categoryName = categoryName;
        this.features = features;
    }
    public String getCategory() {
        return categoryName;
    }
    public List<Double> getFeatures() {
        return features;
    }
    
    public boolean getAtt(int index) {
        return features.get(index) > 0;
    }
}
