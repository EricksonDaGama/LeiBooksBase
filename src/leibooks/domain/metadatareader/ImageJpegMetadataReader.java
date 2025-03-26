package leibooks.domain.metadatareader;//REMOVE COMMENTS ONCE THE CLASS AMetadataReader IS DEFINED

import java.io.FileNotFoundException;
import java.util.Optional;

public class ImageJpegMetadataReader extends AMetadataReader {

	public ImageJpegMetadataReader(String pathToPhotoFile) throws FileNotFoundException {
		super(pathToPhotoFile);
		numPages = Optional.of(1);
		authors = "";
	}

}
