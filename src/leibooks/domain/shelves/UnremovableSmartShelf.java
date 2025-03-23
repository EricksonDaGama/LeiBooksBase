package leibooks.domain.shelves;

import java.util.function.Predicate;

import leibooks.domain.core.ILibrary;
import leibooks.utils.UnRemovable;
import leibooks.domain.facade.IDocument;

public class UnremovableSmartShelf extends SmartShelf implements UnRemovable {

    public UnremovableSmartShelf(String name, ILibrary library, Predicate<IDocument> criteria) {
        super(name, library, criteria);
    }
}
