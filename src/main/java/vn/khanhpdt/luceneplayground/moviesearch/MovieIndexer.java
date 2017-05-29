package vn.khanhpdt.luceneplayground.moviesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class MovieIndexer {

	private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	private final String dataDir;
	private final String indexDir;
	private IndexWriter indexWriter;

	MovieIndexer(String indexDir, String dataDir) throws IOException {
		this.indexDir = indexDir;
		this.dataDir = dataDir;
	}

	IndexWriter getIndexWriter() {
		return indexWriter;
	}

	void start() throws IOException {
		createIndexWriter();
		indexDirectory();
		commitIndex();
	}

	private void commitIndex() throws IOException {
		long committed = indexWriter.commit();
		if (committed == -1L) {
			System.out.println("No change committed");
		}
	}

	private void createIndexWriter() throws IOException {
		FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);

		System.out.println("Writer config: " + indexWriterConfig.toString());
	}

	private void indexDirectory() throws IOException {
		int fileCount = 0;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dataDir), "*.yml")) {
			for (Path path : stream) {
				indexFile(path);
				fileCount++;
			}
			System.out.println(String.format("Indexed %d documents from %d files.", indexWriter.numDocs(), fileCount));
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
		return movies.stream().map(this::createDocument).collect(Collectors.toList());
	}

	private Document createDocument(Movie movie) {
		Document result = new Document();
		result.add(new TextField(MovieFields.TITLE, movie.getTitle(), Field.Store.YES));
		result.add(new TextField(MovieFields.SUMMARY, movie.getSummary(), Field.Store.YES));
		result.add(new TextField(MovieFields.STORYLINE, movie.getStoryline(), Field.Store.NO));

		for (String genre : movie.getGenres()) {
			result.add(new TextField(MovieFields.GENRE, genre, Field.Store.NO));
		}

		result.add(new DoublePoint(MovieFields.RATING_SCORE, movie.getRating().getScore()));
		result.add(new LongPoint(MovieFields.RATING_COUNT, movie.getRating().getCount()));

		FieldType dateFieldType = new FieldType();
		dateFieldType.setIndexOptions(IndexOptions.DOCS);
		dateFieldType.setStored(false);
		result.add(new Field(MovieFields.RELEASE_DATE,
				DateTools.dateToString(movie.getReleaseDate(), DateTools.Resolution.DAY), dateFieldType));

		for (String director : movie.getDirectors()) {
			result.add(new TextField(MovieFields.DIRECTOR, director, Field.Store.NO));
		}

		for (String star : movie.getStars()) {
			result.add(new TextField(MovieFields.STAR, star, Field.Store.NO));
		}

		return result;
	}

	void close() throws IOException {
		indexWriter.close();
	}
}
