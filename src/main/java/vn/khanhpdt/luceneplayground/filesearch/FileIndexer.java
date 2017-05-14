package vn.khanhpdt.luceneplayground.filesearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIndexer {

    private IndexWriter indexWriter;

    private FileIndexer(String indexDir) throws IOException {
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
        indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig());
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Two arguments needed.");
        }

        String indexDir = args[0];
        String dataDir = args[1];

        long start = System.currentTimeMillis();
        FileIndexer fileIndexer = new FileIndexer(indexDir);
        fileIndexer.indexDirectory(dataDir);

        System.out.println("Finish indexing in " + String.valueOf(System.currentTimeMillis() - start) + " ms.");
    }

    private void indexDirectory(String dataDir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dataDir), "*.txt")) {
            for (Path path : stream) {
                indexFile(path);
            }
        }
        System.out.println("Indexed " + String.valueOf(indexWriter.numDocs()) + " documents.");
    }

    private void indexFile(Path filePath) throws IOException {
        System.out.println("Indexing file " + filePath.toAbsolutePath() + "...");
        Document doc = createDocument(filePath);
        try {
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when adding document to index.");
        }
    }

    private Document createDocument(Path filePath) throws IOException {
        Document doc = new Document();

        FieldType contentsFieldType = new FieldType();
        doc.add(new Field("contents", new FileReader(filePath.toFile()), contentsFieldType));

        FieldType filenameFieldType = new FieldType();
        filenameFieldType.setStored(true);
        doc.add(new Field("filename", filePath.getFileName().toString(), filenameFieldType));

        FieldType fullpathFieldType = new FieldType();
        fullpathFieldType.setStored(true);
        doc.add(new Field("fullpath", filePath.toAbsolutePath().toString(), fullpathFieldType));

        return doc;
    }

}
