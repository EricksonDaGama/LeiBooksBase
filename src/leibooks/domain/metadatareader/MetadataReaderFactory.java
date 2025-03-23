package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public enum MetadataReaderFactory {
    INSTANCE;

    private final Map<String, Class<? extends IMetadataReader>> mimeReaderMap = new HashMap<>();

    private final String EXT_READER_PATH = "viewers_readers/metadatareader"; // pasta configurável se necessário

    MetadataReaderFactory() {
        // Registra o leitor padrão para PDF
        mimeReaderMap.put("application/pdf", PDFMetadataReaderAdapter.class);

        // Carrega dinamicamente outros leitores
        loadExternalReaders();
    }

    public IMetadataReader createMetadataReader(String pathToDocFile) throws FileNotFoundException {
        File file = new File(pathToDocFile);
        if (!file.exists()) throw new FileNotFoundException("File not found: " + pathToDocFile);

        String mimeType = getMimeType(file);
        Class<? extends IMetadataReader> readerClass = mimeReaderMap.get(mimeType);

        if (readerClass != null) {
            try {
                return readerClass.getConstructor(String.class).newInstance(pathToDocFile);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate reader for " + mimeType, e);
            }
        }

        // Caso não haja leitor conhecido, usa genérico
        return new GenericMetadataReader(pathToDocFile);
    }

    private String getMimeType(File file) {
        try {
            String mime = Files.probeContentType(file.toPath());
            return (mime != null) ? mime : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private void loadExternalReaders() {
        File dir = new File(EXT_READER_PATH);
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles((d, name) -> name.endsWith("MetadataReader.class"));
        if (files == null) return;

        for (File classFile : files) {
            String className = classFile.getName().replace(".class", "");
            try {
                // Constrói nome qualificado completo
                String qualifiedName = "leibooks.domain.metadatareader." + className;
                Class<?> clazz = Class.forName(qualifiedName);

                if (IMetadataReader.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends IMetadataReader> readerClass = (Class<? extends IMetadataReader>) clazz;

                    // Inferência de MIME: abcXyz → "abc/xyz"
                    String inferredMime = inferMimeFromClassName(className);
                    mimeReaderMap.put(inferredMime, readerClass);
                }
            } catch (ClassNotFoundException e) {
                // ignora classes inválidas
            }
        }
    }

    private String inferMimeFromClassName(String className) {
        String base = className.replace("MetadataReader", "");
        StringBuilder mime = new StringBuilder();

        for (int i = 0; i < base.length(); i++) {
            char c = base.charAt(i);
            if (Character.isUpperCase(c) && i > 0) mime.append('/');
            mime.append(Character.toLowerCase(c));
        }

        return mime.toString(); // Exemplo: PdfDoc → pdf/doc
    }
}
