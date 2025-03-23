package leibooks.domain.shelves;

import java.util.Iterator;
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
        return library.getMatches(criteria).iterator();
    }

    @Override
    public boolean addDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot manually add documents to smart shelf.");
    }

    @Override
    public boolean removeDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Cannot manually remove documents from smart shelf.");
    }

    @Override
    public void processEvent(DocumentEvent e) {
        // Pode ser usada no futuro para atualizações dinâmicas
    }
}
