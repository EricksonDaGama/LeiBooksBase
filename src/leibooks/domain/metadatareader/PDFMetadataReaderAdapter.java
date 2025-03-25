package leibooks.domain.metadatareader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import leibooks.services.reader.PDFReader;

public class PDFMetadataReaderAdapter extends AMetadataReader {

    public PDFMetadataReaderAdapter(String pathToDocFile) throws IOException {
        super(pathToDocFile);

        PDFReader reader = new PDFReader(new File(pathToDocFile));
        this.authors = reader.getAuthors();
        this.numPages = Optional.of(reader.getPages());
    }
}
