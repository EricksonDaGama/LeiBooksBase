package leibooks.domain.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.IShelvesController;
import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.domain.shelves.IShelf;
import leibooks.domain.shelves.IShelves;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

public class ShelvesController extends AbsSubject<LBEvent> implements IShelvesController {

	private final IShelves shelves;

	public ShelvesController(IShelves shelves) {
		this.shelves = shelves;
		this.shelves.registerListener(this);
	}

	@Override
	public boolean addNormalShelf(String name) {
		return shelves.addNormalShelf(name);
	}

	@Override
	public boolean addSmartShelf(String name, Predicate<IDocument> criteria) {
		return shelves.addSmartShelf(name, criteria);
	}

	@Override
	public Iterable<String> getShelves() {
		List<String> names = new ArrayList<>();
		for (IShelf s : shelves) {
			names.add(s.getName());
		}
		return names;
	}

	@Override
	public void remove(String name) throws OperationNotSupportedException {
		shelves.removeShelf(name);
	}

	@Override
	public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		return shelves.addDocument(shelfName, document);
	}

	@Override
	public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		shelves.removeDocument(shelfName, document);
	}

	@Override
	public Iterable<IDocument> getDocuments(String shelfName) {
		return shelves.getDocuments(shelfName);
	}

	@Override
	public void processEvent(ShelfEvent e) {
		emitEvent(e); // propaga eventos da shelf como LBEvent
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Shelves=\n");
		for (IShelf s : shelves) {
			result.append(s.getName()).append(" = [");
			StringBuilder shelfInfo = new StringBuilder();
			for (IDocument d : s) {
				shelfInfo.append(d.getFile().getName()).append(", ");
			}
			if (shelfInfo.length() > 0) {
				result.append(shelfInfo.substring(0, shelfInfo.length() - 2));
			}
			result.append("]\n");
		}
		return result.toString();
	}
}
