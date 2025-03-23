package leibooks.domain.core;

import java.io.FileNotFoundException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.metadatareader.IMetadataReader;
import leibooks.domain.metadatareader.MetadataReaderFactory;

public enum DocumentFactory {
    INSTANCE;

    public IDocument createDocument(String title, String pathToDocFile) throws FileNotFoundException {
        IMetadataReader reader = MetadataReaderFactory.INSTANCE.createMetadataReader(pathToDocFile);

        return new Document(
                title,
                reader.getAuthors(),
                reader.getModifiedDate(),
                reader.getMimeType(),
                pathToDocFile,
                reader.getNumPages()
        );
    }
}
