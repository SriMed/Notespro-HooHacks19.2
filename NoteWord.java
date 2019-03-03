
public class NoteWord {
		private String myWord;
		private String myPOS;
		public NoteWord(String word, String partofs)
		{
			myWord = word;
			myPOS = partofs;
		}
		public String getWord()
		{
			return myWord;
		}
		public String getPOS()
		{
			return myPOS;
		}

}
