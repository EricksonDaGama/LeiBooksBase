package leibooks.domain.metadatareader;

import java.time.LocalDate;
import java.util.Optional;

public interface IMetadataReader {
    String getAuthors();
    String getMimeType();
    LocalDate getModifiedDate();
    Optional<Integer> getNumPages();
}
