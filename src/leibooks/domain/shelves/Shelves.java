package leibooks.domain.shelves;

import java.util.*;
import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.*;
import leibooks.utils.AbsSubject;

public class Shelves extends AbsSubject<ShelfEvent> implements IShelves {

	private final ILibrary library;
	private final Map<String, IShelf> shelfMap = new HashMap<>();

	public Shelves(ILibrary library) {
		this.library = library;
	}

	@Override
	public Iterator<IShelf> iterator() {
		return Collections.unmodifiableCollection(shelfMap.values()).iterator();
	}

	@Override
	public boolean addNormalShelf(String shelfName) {
		if (shelfMap.containsKey(shelfName)) return false;
		IShelf shelf = new NormalShelf(shelfName);
		shelfMap.put(shelfName, shelf);
		emitEvent(new AddShelfEvent(shelf));
		return true;
	}

	@Override
	public boolean addSmartShelf(String shelfName, Predicate<IDocument> criteria) {
		if (shelfMap.containsKey(shelfName)) return false;
		IShelf shelf = new SmartShelf(shelfName, library, criteria);
		shelfMap.put(shelfName, shelf);
		emitEvent(new AddShelfEvent(shelf));
		return true;
	}

	@Override
	public void removeShelf(String shelfName) throws OperationNotSupportedException {
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf == null) return;
		if (shelf instanceof UnRemovable) {
			throw new OperationNotSupportedException("This shelf is unremovable.");
		}
		shelfMap.remove(shelfName);
		emitEvent(new RemoveShelfEvent(shelf));
	}

	@Override
	public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf != null && shelf.removeDocument(document)) {
			emitEvent(new RemoveDocumentShelfEvent(shelf, document));
		}
	}

	@Override
	public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf != null) {
			return shelf.addDocument(document);
		}
		return false;
	}

	@Override
	public Iterable<IDocument> getDocuments(String shelfName) {
		IShelf shelf = shelfMap.get(shelfName);
		return (shelf != null) ? shelf : Collections.emptyList();
	}
}
