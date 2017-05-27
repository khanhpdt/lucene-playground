package vn.khanhpdt.luceneplayground.moviesearch;

import org.junit.Before;
import org.junit.Test;

public class MovieSearcherTest {

	private MovieIndexer indexer;

	@Before
	public void setUp() throws Exception {
		String dataDir = this.getClass().getResource("/MovieIndexer/data").getPath();
		String indexDir = this.getClass().getResource("/MovieIndexer/index").getPath();

		indexer = new MovieIndexer(indexDir, dataDir);
	}

	@Test
	public void testIndexing() throws Exception {
		indexer.start();
	}
}