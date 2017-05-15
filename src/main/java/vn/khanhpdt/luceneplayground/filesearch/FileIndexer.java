package vn.khanhpdt.luceneplayground.filesearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileIndexer {

    private IndexWriter indexWriter;

    FileIndexer(String indexDir, String dataDir) throws IOException {
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
        indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig());

        indexDirectory(dataDir);
    }

    private void indexDirectory(String dataDir) throws IOException {
        try {
            System.out.println("Delete all existing docs...");
            indexWriter.deleteAll();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dataDir), "*.txt")) {
                for (Path path : stream) {
                    indexFile(path);
                }
            }
            System.out.println("Indexed " + String.valueOf(indexWriter.numDocs()) + " documents.");
        } finally {
            indexWriter.close();
        }
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
        contentsFieldType.setIndexOptions(IndexOptions.DOCS);
        doc.add(new Field("contents", new FileReader(filePath.toFile()), contentsFieldType));

        doc.add(new TextField("filename", filePath.getFileName().toString(), Field.Store.NO));
        doc.add(new TextField("fullpath", filePath.toAbsolutePath().toString(), Field.Store.YES));

        return doc;
    }

}
