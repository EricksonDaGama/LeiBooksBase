package leibooks.domain.metadatareader;

import java.io.FileNotFoundException;

public class GenericMetadataReader extends AMetadataReader {

    public GenericMetadataReader(String pathToDocFile) throws FileNotFoundException {
        super(pathToDocFile);
        // Valores default já definidos na superclasse
    }
}
