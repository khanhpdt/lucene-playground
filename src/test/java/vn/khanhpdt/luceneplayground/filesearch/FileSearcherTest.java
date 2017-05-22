package vn.khanhpdt.luceneplayground.filesearch;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class FileSearcherTest {

	private static final String INDEX_DIR = "/home/khanh/projects/lucene-playground/src/test/resources/FileIndexer/index";
	private static final String DATA_DIR = "/home/khanh/projects/lucene-playground/src/test/resources/FileIndexer/data";

	private FileIndexer fileIndexer;

	private FileSearcher fileSearcher;

	@Before
	public void setUp() throws Exception {
		fileIndexer = new FileIndexer(INDEX_DIR);
		fileIndexer.deleteDocs();
		fileIndexer.indexDirectory(DATA_DIR);

		fileSearcher = new FileSearcher(INDEX_DIR);
	}

	@After
	public void tearDown() throws Exception {
		fileSearcher.close();
	}

	@Test
	public void testTermQuery() throws Exception {
		Term term = new Term("filename", "file1");
		TermQuery query = new TermQuery(term);

		TopDocs hits = fileSearcher.search(query);

		assertThat(hits.totalHits, Matchers.is(1));
	}

	@Test
	public void testPrefixQuery() throws Exception {
		Term term = new Term("filename", "file");
		PrefixQuery query = new PrefixQuery(term);

		TopDocs hits = fileSearcher.search(query);

		assertThat(hits.totalHits, Matchers.is(2));
	}

	@Test
	public void testFindContents() throws Exception {
		Term term = new Term("contents", "file");
		PrefixQuery query = new PrefixQuery(term);

		TopDocs hits = fileSearcher.search(query);

		assertThat(hits.totalHits, Matchers.is(2));
	}
}