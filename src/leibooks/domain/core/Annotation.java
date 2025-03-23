package leibooks.domain.core;

public class Annotation {
    private String annotationText;

    public Annotation(String text) {
        this.annotationText = text;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String text) {
        this.annotationText = text;
    }

    @Override
    public String toString() {
        return "Annotation{" + "text='" + annotationText + '\'' + '}';
    }
}
