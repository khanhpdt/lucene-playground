package vn.khanhpdt.luceneplayground.issuesearch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class IssueSearcherTest {

	private IssueIndexer indexer;
	private IssueSearcher searcher;

	@Before
	public void setUp() throws Exception {
		String dataDir = this.getClass().getResource("/IssueIndexer/data").getPath();
		String indexDir = this.getClass().getResource("/IssueIndexer/index").getPath();

		indexer = new IssueIndexer(indexDir, dataDir);
		indexer.start();

		searcher = new IssueSearcher(indexDir);
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
}