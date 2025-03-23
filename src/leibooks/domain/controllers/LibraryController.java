package leibooks.domain.controllers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import leibooks.domain.core.ILibrary;
import leibooks.domain.core.DocumentFactory;
import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

public class LibraryController extends AbsSubject<LBEvent> implements ILibraryController {

    private final ILibrary library;

    public LibraryController(ILibrary library) {
        this.library = library;
        this.library.registerListener(this); // Para receber eventos dos documentos
    }

    @Override
    public Iterable<IDocument> getDocuments() {
        return library;
    }

    @Override
    public Optional<IDocument> importDocument(String title, String pathTofile) {
        try {
            IDocument doc = DocumentFactory.INSTANCE.createDocument(title, pathTofile);
            if (library.addDocument(doc)) {
                return Optional.of(doc);
            }
        } catch (FileNotFoundException e) {
            // erro ao importar documento
        }
        return Optional.empty();
    }

    @Override
    public void removeDocument(IDocument document) {
        library.removeDocument(document);
    }

    @Override
    public void updateDocument(IDocument document, DocumentProperties documentProperties) {
        library.updateDocument(document, documentProperties);
    }

    @Override
    public List<IDocument> getMatches(String regex) {
        return library.getMatches(regex);
    }

    @Override
    public void processEvent(DocumentEvent e) {
        emitEvent(e); // propaga eventos do documento como LBEvent
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Library = \n");
        for (IDocument doc : library) {
            result.append(doc).append("\n");
        }
        return result.toString();
    }
}
