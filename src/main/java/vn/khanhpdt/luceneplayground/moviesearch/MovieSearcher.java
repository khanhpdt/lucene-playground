package vn.khanhpdt.luceneplayground.moviesearch;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

class MovieSearcher {

	private IndexSearcher indexSearcher;

	MovieSearcher(String indexDir) throws IOException {
		FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
	}

	TopDocs search(Query query) throws IOException {
		return indexSearcher.search(query, 20);
	}

	IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	void close() throws IOException {
		indexSearcher.getIndexReader().close();
	}
}
