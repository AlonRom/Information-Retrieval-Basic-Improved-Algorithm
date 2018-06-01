package Lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;

public class LuceneHelper 
{
	/** Indexes a single document **/
	public static Document IndexDocument(IndexWriter writer, String filePath) throws Exception 
	{
		// make a new, empty document
		Document document = new Document();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
		{
			//add the path of the file as a field named "filepath".  Use a
			//field that is indexed (i.e. searchable), but don't tokenize 
			//the field into separate words and don't index term frequency
			//or positional information
			document.add(new StringField(Constants.FILE_PATH, filePath, Field.Store.YES));
			
			//add the contents of the file to a field named "contents". Specify a Reader,
			//so that the text of the file is tokenized and indexed, but not stored.
			document.add(new TextField(Constants.CONTENT, br));
			
			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) 
			{
				//new index, so we just add the document (no old document can be there):
				System.out.println("adding " + filePath);
				writer.addDocument(document);
		    } 
			else 
			{
				//existing index (an old copy of this document may have been indexed) so 
				//we use updateDocument instead to replace the old one matching the exact 
				//path, if present:
				System.out.println("updating " + filePath);
				writer.updateDocument(new Term("path", filePath), document);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return document;
	}
	
	public static CharArraySet GetMostFrequentWords(IndexReader reader, List<String> stopWords) throws Exception 
	{

		TermStats[] states = HighFreqTerms.getHighFreqTerms(reader, Constants.STOP_WORDS_COUNT, Constants.CONTENT, new HighFreqTerms.TotalTermFreqComparator());
		List<TermStats> stopWordsCollection = Arrays.asList(states);
		
		System.out.print("Stop Words: ");
		for (TermStats term : states)
		{
		    System.out.print(term.termtext.utf8ToString() + " "); 
		    stopWords.add(term.termtext.utf8ToString());
		}
		System.out.println();
		return new CharArraySet(stopWordsCollection, true);
	}

	public static void SearchIndexForQueries(Map<Integer, String> queries) 
	{
		for (Map.Entry<Integer, String> entry : queries.entrySet())
		{
		    try 
		    {
				System.out.println("Search for query " + entry.getKey() + ": " + entry.getValue());
				SearchQuery(entry.getValue());    
			} 
		    catch (Exception e) {
				e.printStackTrace();
			}
		  
		}
	}
	
	public static void SearchQuery(String searchQuery) throws IOException, ParseException
	{
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(Constants.DOCUMENTS_INDEX_PATH)));
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		QueryParser queryParser = new QueryParser(Constants.CONTENT, new StandardAnalyzer());
		Query query = queryParser.parse(searchQuery);
		TopDocs results = indexSearcher.search(query, 1000);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = Math.toIntExact(results.totalHits);
		System.out.println(numTotalHits + " total matching documents");
		 // Iterate through the results:
	    /*for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = indexSearcher.doc(hits[i].doc);
		  System.out.println("Search for query " + entry.getKey() + ": " + entry.getValue());
	    }*/

	}
}