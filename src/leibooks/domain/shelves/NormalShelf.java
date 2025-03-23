package leibooks.domain.shelves;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.Listener;

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
}
