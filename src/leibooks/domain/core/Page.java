package leibooks.domain.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {
    private final int pageNum;
    private boolean bookmarked = false;
    private final List<Annotation> annotations = new ArrayList<>();

    public Page(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void toggleBookmark() {
        this.bookmarked = !bookmarked;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void addAnnotation(String text) {
        annotations.add(new Annotation(text));
    }

    public void removeAnnotation(int index) {
        if (index >= 0 && index < annotations.size()) {
            annotations.remove(index);
        }
    }

    public int getAnnotationCount() {
        return annotations.size();
    }

    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }

    public Iterable<Annotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public String getAnnotationText(int index) {
        return annotations.get(index).getAnnotationText();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Page{bookmark=").append(bookmarked);
        sb.append(", annotations=").append(annotations);
        sb.append(", pageNum=").append(pageNum);
        sb.append("}");
        return sb.toString();
    }

}
