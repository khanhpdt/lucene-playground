package vn.khanhpdt.luceneplayground.moviesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MovieIndexer {

	private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	private final String dataDir;
	private final String indexDir;
	private IndexWriter indexWriter;

	MovieIndexer(String indexDir, String dataDir) throws IOException {
		this.indexDir = indexDir;
		this.dataDir = dataDir;
	}

	void start() throws IOException {
		createIndexWriter();
		indexDirectory();
	}

	private void createIndexWriter() throws IOException {
		FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
		indexWriterConfig.setInfoStream(System.out);
		indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
	}

	private void indexDirectory() throws IOException {
		int fileCount = 0;
		try {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dataDir), "*.yml")) {
				for (Path path : stream) {
					indexFile(path);
					fileCount++;
				}
				System.out.println(String.format("Indexed %d documents from %d files.", indexWriter.numDocs(), fileCount));
			}
		} finally {
			indexWriter.close();
		}
	}

	private void indexFile(Path filePath) throws IOException {
		System.out.println("Indexing file " + filePath.toAbsolutePath());
		List<Document> docs = createDocuments(filePath);
		indexWriter.addDocuments(docs);
	}

	private List<Document> createDocuments(Path filePath) throws IOException {
		List<Movie> movies = objectMapper.readValue(filePath.toFile(),
				objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Movie.class));

		return new ArrayList<>();
	}

}
