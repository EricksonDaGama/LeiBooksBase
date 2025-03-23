package leibooks.domain.core;

import java.util.*;
import java.util.regex.Pattern;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.domain.facade.events.UpdateDocumentPropertiesEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

public class Library extends AbsSubject<DocumentEvent> implements ILibrary {

	private final Set<IDocument> documents = new TreeSet<>();

	@Override
	public Iterator<IDocument> iterator() {
		return Collections.unmodifiableSet(documents).iterator();
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public boolean addDocument(IDocument document) {
		boolean added = documents.add(document);
		if (added) {
			document.registerListener(this); // React to document events
			emitEvent(new AddDocumentEvent(document));
		}
		return added;
	}

	@Override
	public void removeDocument(IDocument document) {
		if (documents.remove(document)) {
			document.unregisterListener(this);
			emitEvent(new RemoveDocumentEvent(document));
		}
	}

	@Override
	public void updateDocument(IDocument document, DocumentProperties properties) {
		document.setTitle(properties.getTitle());
		document.setAuthor(properties.getAuthor());
		emitEvent(new UpdateDocumentPropertiesEvent(document));
	}

	@Override
	public List<IDocument> getMatches(String regex) {
		List<IDocument> matches = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		for (IDocument doc : documents) {
			if (doc.matches(regex)) {
				matches.add(doc);
			}
		}
		return matches;
	}

	@Override
	public void processEvent(DocumentEvent event) {
		emitEvent(event); // Propaga eventos dos documentos
	}
}
