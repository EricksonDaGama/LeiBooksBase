package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public abstract class AMetadataReader implements IMetadataReader {

    protected final String mimeType;
    protected final LocalDate modifiedDate;
    protected Optional<Integer> numPages = Optional.empty();
    protected String authors = "n/a";

    public AMetadataReader(String pathToDocFile) throws FileNotFoundException {
        File file = new File(pathToDocFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + pathToDocFile);
        }

        this.modifiedDate = Instant.ofEpochMilli(file.lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        this.mimeType = guessMimeType(file);
    }

    private String guessMimeType(File file) {
        try {
            return java.nio.file.Files.probeContentType(file.toPath());
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public Optional<Integer> getNumPages() {
        return numPages;
    }
}
