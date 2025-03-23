package leibooks.domain.metadatareader;

import java.io.FileNotFoundException;

import leibooks.services.reader.PDFReader;

public class PDFMetadataReaderAdapter extends AMetadataReader {

    public PDFMetadataReaderAdapter(String pathToDocFile) throws FileNotFoundException {
        super(pathToDocFile);

        PDFReader reader = new PDFReader(pathToDocFile);
        this.authors = reader.getAuthor();
        this.numPages = Optional.of(reader.getNumberOfPages());
    }
}
