package vn.khanhpdt.luceneplayground.issuesearch;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IssueSearcherTest {

	private IssueIndexer indexer;
	private IssueSearcher searcher;

	@Before
	public void setUp() throws Exception {
		String dataDir = this.getClass().getResource("/IssueIndexer").getPath() + "/data";
		String indexDir = this.getClass().getResource("/IssueIndexer").getPath() + "/index";

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

	@Test
	public void testContainQuery() throws Exception {
		Term term = new Term(IssueFields.SUMMARY, "issue");
		TermQuery termQuery = new TermQuery(term);

		TopDocs docs = searcher.search(termQuery);

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(3));
	}

	@Test
	public void testPrefixQuery() throws Exception {
		Term term = new Term(IssueFields.SUMMARY, "iss");
		PrefixQuery query = new PrefixQuery(term);

		TopDocs docs = searcher.search(query);

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(3));
	}

	@Test
	public void testPhraseQueryConsecutiveTerms() throws Exception {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		builder.add(new Term(IssueFields.DESCRIPTION, "first"));
		builder.add(new Term(IssueFields.DESCRIPTION, "quick"));
		builder.add(new Term(IssueFields.DESCRIPTION, "brown"));

		TopDocs docs = searcher.search(builder.build());

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(1));
	}

	@Test
	public void testPhraseQueryWithPositions() throws Exception {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		builder.add(new Term(IssueFields.DESCRIPTION, "quick"), 0);
		builder.add(new Term(IssueFields.DESCRIPTION, "fox"), 2);

		TopDocs docs = searcher.search(builder.build());

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(3));
	}

	private void printIssueKeys(TopDocs docs) throws IOException {
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
			Document issue = searcher.getIndexSearcher().doc(scoreDoc.doc);
			System.out.println("Found issue: " + issue.get(IssueFields.KEY));
		}
	}

	@Test
	public void testBooleanQueryParser() throws Exception {
		TopDocs docs = searcher.search("+key:ISSUE +summary:\"first issue\"");

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(1));
	}

	@Test
	public void testProximityQueryParser() throws Exception {
		TopDocs docs = searcher.search("description:\"first fox\"~3");

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(1));
	}

	@Test
	public void testFuzzyQueryParser() throws Exception {
		TopDocs docs = searcher.search("key:ISUEE~2");

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(3));
	}

	@Test
	public void testWildcardQueryParser() throws Exception {
		TopDocs docs = searcher.search("key:I*E");

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(3));
	}

	@Test
	public void testRangeQueryParser() throws Exception {
		TopDocs docs = searcher.search("createdTime:[20170530100001 TO 20170530113001]");

		printIssueKeys(docs);
		assertThat(docs.totalHits, Matchers.is(2));
	}

}