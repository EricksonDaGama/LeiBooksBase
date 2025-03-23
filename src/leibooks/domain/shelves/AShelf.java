package leibooks.domain.shelves;

import java.util.Objects;

import leibooks.domain.facade.IDocument;

public abstract class AShelf implements IShelf {

    protected final String name;

    public AShelf(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    // Compara prateleiras apenas pelo nome
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IShelf)) return false;
        IShelf other = (IShelf) obj;
        return Objects.equals(name, other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
