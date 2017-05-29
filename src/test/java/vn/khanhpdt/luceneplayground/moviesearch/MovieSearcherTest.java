package vn.khanhpdt.luceneplayground.moviesearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class MovieSearcherTest {

	private MovieIndexer indexer;
	private MovieSearcher searcher;

	@Before
	public void setUp() throws Exception {
		String dataDir = this.getClass().getResource("/MovieIndexer/data").getPath();
		String indexDir = this.getClass().getResource("/MovieIndexer/index").getPath();

		indexer = new MovieIndexer(indexDir, dataDir);
		indexer.start();

		searcher = new MovieSearcher(indexDir);
	}

	@After
	public void tearDown() throws Exception {
		indexer.close();
		searcher.close();
	}

	@Test
	public void testIndexedFieldNames() throws Exception {
		System.out.println("Indexed fields: " + Arrays.toString(indexer.getIndexWriter().getFieldNames().toArray()));
	}

	@Test
	public void testContainQuery_1() throws Exception {
		Term term = new Term(MovieFields.TITLE, "shawshank");
		TermQuery termQuery = new TermQuery(term);

		TopDocs docs = searcher.search(termQuery);

		assertThat(docs.totalHits, Matchers.is(1));
		printMovieTitles(docs);
	}

	@Test
	public void testContainQuery_2() throws Exception {
		Term term = new Term(MovieFields.TITLE, "godfather");
		TermQuery termQuery = new TermQuery(term);

		TopDocs docs = searcher.search(termQuery);

		assertThat(docs.totalHits, Matchers.is(2));
		printMovieTitles(docs);
	}

	@Test
	public void testPhraseQueryConsecutiveTerms() throws Exception {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		builder.add(new Term("summary", "dark"));
		builder.add(new Term("summary", "knight"));

		TopDocs docs = searcher.search(builder.build());

		assertThat(docs.totalHits, Matchers.is(1));
		printMovieTitles(docs);
	}

	@Test
	public void testPhraseQueryWithPositions() throws Exception {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		builder.add(new Term("summary", "dark"), 0);
		builder.add(new Term("summary", "knight"), 2);

		TopDocs docs = searcher.search(builder.build());

		assertThat(docs.totalHits, Matchers.is(1));
		printMovieTitles(docs);
	}

	private void printMovieTitles(TopDocs docs) throws IOException {
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
			Document movie = searcher.getIndexSearcher().doc(scoreDoc.doc);
			System.out.println("Found movie: " + movie.get(MovieFields.TITLE));
		}
	}
}