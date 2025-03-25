package leibooks.domain.shelves;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;

public class SmartShelf extends AShelf {

    protected final ILibrary library;
    protected final Predicate<IDocument> criteria;

    public SmartShelf(String name, ILibrary library, Predicate<IDocument> criteria) {
        super(name);
        this.library = library;
        this.criteria = criteria;
    }

    @Override
    public Iterator<IDocument> iterator() {
        List<IDocument> filtered = new ArrayList<>();
        for (IDocument doc : library) {
            if (criteria.test(doc)) {
                filtered.add(doc);
            }
        }
        return filtered.iterator();
    }

    @Override
    public boolean addDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot manually add documents to a smart shelf.");
    }

    @Override
    public boolean removeDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot manually remove documents from a smart shelf.");
    }

    @Override
    public void processEvent(DocumentEvent e) {
        // SmartShelf could react to document events if needed
    }
}
