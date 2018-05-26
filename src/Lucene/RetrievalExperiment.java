package Lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class RetrievalExperiment {

	private static String docsFilePath;
	private static String queryFilePath;
	private static String retrievalAlgorithmMode;
	private static String outputFilePath;
	
	static List<String> inputParameters;
	static List<String> collection;
	static List<String> stopWords;
	static Map<Integer, List<String>> queries;

	public static void main(String[] args) throws IOException, ParseException {
		
		//get parameters file data
		String inputFilePath = "./files/parameters.txt";
		inputParameters= TextFileReader.ReadFileParametres(inputFilePath);
		queryFilePath = inputParameters.get(0);
		docsFilePath = inputParameters.get(1);
		outputFilePath = inputParameters.get(2);
		retrievalAlgorithmMode = inputParameters.get(3);
			
		//create a collection from documents 
		collection = TextFileReader.CreateCollection(docsFilePath);
	
		//get 20 words that appear most frequently in the collection 
		stopWords = TextFileReader.GetStopWords(collection, 20);
		
		//get queries from the query file
		queries = TextFileReader.ReadFileQueries(queryFilePath);
			
		
		
		/*
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		String querystr = args.length > 0 ? args[0] : "lucene";
		Query q = new QueryParser("title", standardAnalyzer).parse(querystr);
		*/
		
		/*
		
		// New index
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		
		String outputDir = "C:\\priya\\workspace\\JCG\\src\\com\\javacodegeeks\\lucene\\output";
		File file = new File(inputFilePath);

		Directory directory = FSDirectory.open(Paths.get(outputDir));
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		config.setOpenMode(OpenMode.CREATE);
		// Create a writer
		IndexWriter writer = new IndexWriter(directory, config);

		Document document = new Document();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {

			document.add(new TextField("content", br));
			writer.addDocument(document);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now let's try to search for Hello
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", standardAnalyzer);
		Query query = parser.parse("Hello");
		TopDocs results = searcher.search(query, 5);
		System.out.println("Hits for Hello -->" + results.totalHits);

		// case insensitive search
		query = parser.parse("hello");
		results = searcher.search(query, 5);
		System.out.println("Hits for hello -->" + results.totalHits);

		// search for a value not indexed
		query = parser.parse("Hi there");
		results = searcher.search(query, 5);
		System.out.println("Hits for Hi there -->" + results.totalHits);
		*/
	}
}
