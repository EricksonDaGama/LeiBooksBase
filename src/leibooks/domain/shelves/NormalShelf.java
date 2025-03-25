package leibooks.domain.shelves;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;

public class NormalShelf extends AShelf {

    private final Set<IDocument> documents = new HashSet<>();

    public NormalShelf(String name) {
        super(name);
    }

    @Override
    public Iterator<IDocument> iterator() {
        return Collections.unmodifiableSet(documents).iterator();
    }

    @Override
    public boolean addDocument(IDocument document) {
        boolean added = documents.add(document);
        if (added) {
            document.registerListener(this);
        }
        return added;
    }

    @Override
    public boolean removeDocument(IDocument document) {
        boolean removed = documents.remove(document);
        if (removed) {
            document.unregisterListener(this);
        }
        return removed;
    }

    @Override
    public void processEvent(DocumentEvent e) {
        if (e instanceof leibooks.domain.facade.events.RemoveDocumentEvent) {
            removeDocument(e.getDocument());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (IDocument doc : this) {
            String fileName = extractFileNameFromToString(doc.toString());
            sb.append(fileName).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove última vírgula
        sb.append("]");
        return sb.toString();
    }

    private String extractFileNameFromToString(String docStr) {
        int fileStart = docStr.indexOf("file=") + 5;
        int fileEnd = docStr.indexOf(",", fileStart);
        if (fileStart >= 5 && fileEnd > fileStart) {
            String filePath = docStr.substring(fileStart, fileEnd).trim();
            return filePath.substring(filePath.lastIndexOf('/') + 1);
        }
        return "unknown";
    }
}
