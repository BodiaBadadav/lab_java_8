/** Класс для хранения ссылки и ее глубины */
public class URLDepthPair {
    private String url;
    private int depth;
    private int visited;
    public URLDepthPair(String URL, int dep) {
        url = URL;
        depth = dep;
        visited = 1;
    }
    public String getURL() {
        return url;
    }
    public int getDepth() {
        return depth;
    }
    public void incrementVisited() {
        visited++;
    }
    public String toString() {
        return "<URL href=\"" + url + "\" visited=\"" + visited + "\" depth=\"" + depth + "\" \\>";
    }
}