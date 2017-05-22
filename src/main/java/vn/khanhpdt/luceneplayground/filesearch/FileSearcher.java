package vn.khanhpdt.luceneplayground.filesearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

class FileSearcher {

	private IndexSearcher indexSearcher;

	FileSearcher(String indexDir) throws IOException {
		FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
	}

	TopDocs search(Query query) throws IOException {
		TopDocs docs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : docs.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			System.out.println("Found doc: " + doc.get("fullpath"));
		}
		return docs;
	}

	void close() throws IOException {
		indexSearcher.getIndexReader().close();
	}

}
