// Imports the Google Cloud client library
import java.io.IOException;
import java.util.Map;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class QuickstartSample {
  public static void main(String... args) {
    try
    {
    		//QuickstartSample.testAnalyzingSentiment();
    		QuickstartSample.testAnalyzingEntities();
    }
    catch(Exception e)
    {
    		e.printStackTrace();
    }
  }
  
  private static void testAnalyzingSentiment() throws IOException 
  {
	// Instantiates a client
	    try (LanguageServiceClient language = LanguageServiceClient.create()) {

	      // The text to analyze
	      String text = "Hello, world!";
	      Document doc = Document.newBuilder()
	          .setContent(text).setType(Type.PLAIN_TEXT).build();

	      // Detects the sentiment of the text
	      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

	      System.out.printf("Text: %s%n", text);
	      System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
	    }
  }
  
  private static void testAnalyzingEntities() throws IOException
  {
	  String text = "The five mechanisms of evolution are dna translation and dna transcription";
	  
	// Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
	  try (LanguageServiceClient language = LanguageServiceClient.create()) {
	    Document doc = Document.newBuilder()
	        .setContent(text)
	        .setType(Type.PLAIN_TEXT)
	        .build();
	    AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
	        .setDocument(doc)
	        .setEncodingType(EncodingType.UTF16)
	        .build();

	    AnalyzeEntitiesResponse response = language.analyzeEntities(request);

	    // Print the response
	    for (Entity entity : response.getEntitiesList()) {
	      System.out.printf("Entity: %s", entity.getName());
	      System.out.printf("Salience: %.3f\n", entity.getSalience());
	      System.out.println("Metadata: ");
	      for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
	        System.out.printf("%s : %s", entry.getKey(), entry.getValue());
	      }
	      for (EntityMention mention : entity.getMentionsList()) {
	        System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
	        System.out.printf("Content: %s\n", mention.getText().getContent());
	        System.out.printf("Type: %s\n\n", mention.getType());
	      }
	    }
	  }
  }
}