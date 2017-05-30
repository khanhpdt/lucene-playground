package vn.khanhpdt.luceneplayground.issuesearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

class IssueSearcher {

	private IndexSearcher indexSearcher;

	IssueSearcher(String indexDir) throws IOException {
		FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
	}

	TopDocs search(Query query) throws IOException {
		return indexSearcher.search(query, 20);
	}

	/**
	 * @see <a href="https://lucene.apache.org/core/6_5_1/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description">More details on query</a>
	 */
	TopDocs search(String query) throws ParseException, IOException {
		QueryParser queryParser = new QueryParser(null, new StandardAnalyzer());
		return search(queryParser.parse(query));
	}

	IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	void close() throws IOException {
		indexSearcher.getIndexReader().close();
	}
}
