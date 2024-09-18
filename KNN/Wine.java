public record Wine(double[] attributes, int classType) {
    public double getAttribute(int i) {
        return attributes[i];
    }
}
