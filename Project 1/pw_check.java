import java.awt.*;
import java.io.*;
import java.util.*;



public class pw_check
{	
	//Declare scanner
	Scanner scan;
	
	//index used to get letter
	int index = 0;
	
	//defining Node class
	//each node points to a child node and a sibling node
	private static class Node
	{
		private Object value;
		private Object key;
		Node child;
		Node sibling;
	}
	
	//defining root node
	private Node root = new Node();
	
	//declaring current node
	private Node currentNode;
	
	//holds the current letter of the word
	private Object letter;
	
	private Object endWord = '^';
	
	public void printWord(Object str)
	{
		PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new FileOutputStream("my_dictionary.txt", true));

		}

		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}
		printer.println(str);
		
		printer.close();
	}
	
	
	//v is the current value of the node, r is the original word
	public void checkEndWord(Object v, String r)
	{
		if(v == endWord)
		{
			currentNode.key = r;
			printWord(currentNode.key);
		}
	}
	
	//w is the original word, e is the word with ^
	public void checkChild(String w, String e)
	{
		//for when word is an empty string
		if( w.isEmpty())
		{
			
		}
		
		else
		{
			//check to see if current is root or not
			if(currentNode == root)
			{
				//check to see if a child node exists
				if(currentNode.child == null)
				{
					//creates child node and navigates to it
					createChild(w);
					
					//created node for first letter in word so keep creating child nodes until
					//end of word
					do
					{
						//increment the index to get the next letter and create a node
						index++;
						letter = e.charAt(index);
						createChild(w);
					}
					while(currentNode.value != endWord);
					
				}
				
				//when the current node already has a child node, 
				else
				{
					//check to see if child node matches letter
					if(currentNode.child.value == letter)
					{
						//this section handles all words that start with T
						//navigate to T node
						currentNode = currentNode.child;
						
						
						
						//loop through child nodes to see if subsequent letters match each node
						do
						{
							//get next letter and compare to next child node
							index++;
							letter = e.charAt(index);
							currentNode = currentNode.child;
							
							if(currentNode.value != letter)
							{
								//found mismatch, break loop
								break;
							}
							
							else
							{
								//letter matches so continue searching
							}
						}
						while(currentNode.child != null);
						
						//check to see if child node and letter match
						if(currentNode.value != letter)
						{
							checkSibling(w,e);
						}
					
						else
						{
							
						}
					}
					
					//if it doesn't match the letter then check sibling nodes if they exist
					else
					{
						//navigates to root.child if current node is root
						if(currentNode == root)
						{
							currentNode = currentNode.child;
							checkSibling(w, e);
						}
						
						else
						{
							checkSibling(w,e);
						}
					}
				}
			}
			
			//when current node isn't root
			else
			{
				//check to see if child nodes already exist
				if(currentNode.child == null)
				{
					do
					{
						//increment the index to get the next letter and create a node
						index++;
						letter = e.charAt(index);
						createChild(w);
					}
					while(currentNode.value != endWord);
				}
				
				//If there are already child nodes, get the next letter and compare child value
				//to it
				else
				{
					//if the node matches then traverse child nodes until a mismatch is found
					if(currentNode.value == letter)
					{
						do
						{
							index++;
							letter = e.charAt(index);
							currentNode = currentNode.child;
							
							if(currentNode.value != letter)
							{
								//found mismatch, break loop
								break;
							}
							
							else
							{
								//letter matches so continue searching
							}
						}
						while(currentNode.child != null);
					}
					
					//check to see if child node and letter match
					if(currentNode.value != letter)
					{
						checkSibling(w,e);
					}
					
				}
			}
		}
	}
		
	
	
	//Creates a new child node and sets it as the current.
	//origWord is the unmodified word that is stored as the endWord marker's key
	public void createChild(String origWord)
	{

		currentNode.child = new Node();
		currentNode.child.value = letter;
		currentNode = currentNode.child;
		
		if(currentNode.value == endWord)
		{
			checkEndWord(letter,origWord);
		}
	}
		

	//b is the original word, c is the word with ^
	public void checkSibling(String b, String c)
	{
		//see if there any sibling nodes exist
		if(currentNode.sibling == null)
		{
			//creates sibling node and navigates to it
			createSibling(b);
			
			//If created node was endWord node then get next word
			if(currentNode.value == endWord)
			{
				//do nothing get next word
			}
			
			else
			{
				//create child nodes for word by calling checkChild
				checkChild(b, c);
			}
		}
		
		//when a sibling node exists
		else
		{
			//navigate through sibling nodes until letter is found or create one for letter
			do
			{
				currentNode = currentNode.sibling;
				if(currentNode.value != letter)
				{
					//do nothing and continue searching
				}
				
				//if the letter is found then exit loop
				else
				{
					break;
				}
			}
				
			while(currentNode.sibling != null);
			
			//If the letter is found then call checkChild
			if(currentNode.value == letter)
			{
				checkChild(b, c);
			}
			
			//The letter wasn't found so create new sibling node
			else
			{
				createSibling(b);
				checkChild(b,c);
			}
		}
	}
	
	//Creates a sibling node. dictionaryW is the original word that is stored as the key
	//in the endWord marker
	public void createSibling(String dictionaryW)
	{
		currentNode.sibling = new Node();
		currentNode = currentNode.sibling;
		currentNode.value = letter;
		
		if(currentNode.value == endWord)
		{
			checkEndWord(letter,dictionaryW );
		}
	}
	
	//function to open file
		public void loadFile()
		{
			try
			{
				scan = new Scanner(new FileInputStream("dictionary.txt"));
			}

			catch(FileNotFoundException e)
			{
				System.out.println("dictionary.txt was not found \n");
			}
		}
		
		//reads words from file and stores them
		public void readFile()
		{
			String words;
			
			do
			{
				words = scan.nextLine();
				addWords(words);
			}
			while(scan.hasNextLine());

			scan.close();
		}
		
		
		//stores words into the trie
		public void addWords(String words)
		{
			//start at root node
			currentNode = root;
			
			//use '^' to mark end of word
			String newWord = words + '^';
			
			//for loop to go through each character in the word
			for(int i = 0; i < newWord.length(); i++)
			{
				letter = newWord.charAt(i);
				index = i;
				
				//Begin to add words to trie
				checkChild(words, newWord);	
				
				//if index was modified then break loop
				if(index > i)
				{
					break;
				}
			}
		}
		
		
		
		public pw_check()
		{
			
		}
		
		public static void main(String[] args)
		{
			pw_check passCheck = new pw_check();
			
			//If there are no command line arguments then prompt the user to enter passwords
			//until they are done
			if(args.length < 1)
			{
				String runAgain;
				passCheck.scan = new Scanner(System.in);
				
				System.out.println("Passwords must be 5 characters; one to 3 lowercase letters, one to two numbers, or one to two of the following symbols: !, @, $, %, &, * \n"); 
				do
				{
					System.out.println("Enter a Password: \n");
					String userPass = passCheck.scan.nextLine();
					
					System.out.println("Do you want to enter another password? (y/n) \n");
					runAgain = passCheck.scan.nextLine();
					
				}
				while(runAgain.equals("y"));
			}
			
			//when there is the -g argument
			else
			{
				//load dictionary
				passCheck.loadFile();
				//read dictionary and generate my_dictionary.txt
				passCheck.readFile();
			}

		}
}