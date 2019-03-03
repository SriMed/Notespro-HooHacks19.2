// Imports the Google Cloud client library
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.PartOfSpeech;
import com.google.cloud.language.v1.PartOfSpeech.Tag;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;

public class NLPNotes {
	private static List<NoteWord> words;
    private static List<String> helpfulWikis;
    private static PrintWriter notesOUT;
    private static String text;
    private static List<String> keyWords;
    public static void main(String... args) 
  {
	words = new ArrayList<NoteWord>();
	helpfulWikis = new ArrayList<String>();
	keyWords = new ArrayList<String>();
    try
    {
    	notesOUT = new PrintWriter(new FileWriter("notes.html"));
    	text = readInFromFile(new File("/Users/sripriya/eclipse-workspace/Notes2/src/history.txt"));
    	NLPNotes.testCategorizing();
    	NLPNotes.testSyntax();
    	notesOUT.println("\n");
    	NLPNotes.testAnalyzingEntities();
    	notesOUT.close();
    	System.out.print("Finished");
    }
    catch(Exception e)
    {
    		e.printStackTrace();
    }
  }
    private static List<String> listFromFile(File f)
	{
		List<String> list = new ArrayList<String>();
		Scanner infile = null;
	      try
	      {
	         infile = new Scanner(f);
	         while(infile.hasNext())
	         {
	         list.add(infile.next());
	         }
	      }
	      catch(Exception e)
	      {
	         System.out.println("Sorry. File could not be read in properly. Please try again.");
	      }
	      infile.close();
	      return list;
	}
private static String readInFromFile(File f)
	{
		String text = "";
		Scanner infile = null;
	      try
	      {
	         infile = new Scanner(f);
	         while(infile.hasNext())
	         {
	         text += infile.nextLine();
	         }
	      }
	      catch(Exception e)
	      {
	         System.out.println("Sorry. File could not be read in properly. Please try again.");
	      }
	      infile.close();
	      return text;
	}
	private static void testAnalyzingEntities() throws IOException
	{
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
		    notesOUT.println("Key Words:");
		    // Print the response
		    for (Entity entity : response.getEntitiesList()) {
		      //System.out.printf("Entity: %s", entity.getName());
		      /*
		      System.out.printf("Salience: %.3f\n", entity.getSalience());
		      System.out.println("Metadata: ");
		      for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
		        System.out.printf("%s : %s", entry.getKey(), entry.getValue());
		      }
		      for (EntityMention mention : entity.getMentionsList()) {
		        System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
		        System.out.printf("Content: %s\n", mention.getText().getContent());
		        System.out.printf("Type: %s\n\n", mention.getType());
		      } */
		      String n = entity.getName().toString();
		      if(keyWords.contains(n) == false)
		      {
		    	  keyWords.add(n);
		    	  notesOUT.println(" -" + n);
		      }
		    }
		    System.out.println("Finished printing key words");
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	  }
  private static List<Token> testSyntax() throws IOException
  {
	List<String> cw = listFromFile(new File("/Users/sripriya/eclipse-workspace/Notes2/src/commonwords.txt"));
	List<String> arrow = listFromFile(new File("/Users/sripriya/eclipse-workspace/Notes2/src/arrow.txt"));
	// Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
	  try (LanguageServiceClient language = LanguageServiceClient.create()) 
	  {
	    Document doc = Document.newBuilder()
	        .setContent(text)
	        .setType(Type.PLAIN_TEXT)
	        .build();
	    AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
	        .setDocument(doc)
	        .setEncodingType(EncodingType.UTF16)
	        .build();
	    // analyze the syntax in the given text
	    AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
	    // print the response
	    try
	    {
	      for (Token token : response.getTokensList()) {
		      //System.out.printf("\tText: %s\n", token.getText().getContent());
		      //System.out.printf("PartOfSpeechTag: %s\n", token.getPartOfSpeech().getTag());
		      //my code
		      String w = token.getText().toString();
		      String p = token.getPartOfSpeech().toString();
		      w = w.substring(w.indexOf("\"") + 1, w.lastIndexOf("\""));
		      p = p.substring(p.indexOf(" "), p.indexOf(" ") + 5);
		      p = p.trim();
		      NoteWord ducky = new NoteWord(w, p);
		      words.add(ducky);
		    }
	      int counting = 1;
	      Iterator<NoteWord> it = words.iterator();
	      while(it.hasNext())
	      {
	    	  NoteWord k = it.next();
	    	  String wo = k.getWord();
	    	  if(cw.contains(wo))
	    	  {
	    		  it.remove();
	    	  }
	    	  else if(arrow.contains(wo))
	    	  {
	    		  notesOUT.print(" --> ");
	    	  }
	    	  else
	    	  {
	    	  if(k.getPOS().equals("NUM"))
	    	  {
	    		  notesOUT.print(" - " + wo);
	    	  }
	    	  else if(k.getPOS().equals("PUNC"))
	    	  {
	    		  if(wo.equals("."))
	    		  {
		    		  notesOUT.println();
		    		  notesOUT.print("\t");
	    		  }
	    		  if(wo.equals(","))
	    		  {
	    			  notesOUT.print(wo);
	    		  }
	    		  it.remove();
	    	  }
	    	  else if(wo.equals("and"))
	    	  {
	    		  notesOUT.print(" & ");
	    	  }
	    	  else if(k.getPOS().equals("NOUN") || k.getPOS().equals("ADJ") || k.getPOS().equals("ADV") || k.getPOS().equals("VERB"))
	    	  {
	    		  notesOUT.print(" ");
	    		  notesOUT.print(wo);
	    	  }
	    	  counting++;
	    	  }
	    	  if(counting%10 == 0)
	    	  {
	    		  notesOUT.println();
	    	  }
	      }
	      }
	      catch(Exception e)
	      {
	         e.printStackTrace();
	      }
	    System.out.println("Finished printing notes");
	    return response.getTokensList();
	  }
  }
private static void testCategorizing() throws IOException
{
	  try (LanguageServiceClient language = LanguageServiceClient.create()) {
		  // set content to the text string
		  Document doc = Document.newBuilder()
		      .setContent(text)
		      .setType(Type.PLAIN_TEXT)
		      .build();
		  ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
		      .setDocument(doc)
		      .build();
		  // detect categories in the given text
		  ClassifyTextResponse response = language.classifyText(request);

		  List<ClassificationCategory> c = response.getCategoriesList();
		  String cat = c.get(0).getName();
		  cat = cat.substring(cat.lastIndexOf("/") + 1);
		  notesOUT.println(cat + "\n");
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
}
}
