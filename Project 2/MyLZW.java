/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/
import java.math.*;

public class MyLZW {
    private static int R = 256;				// number of input chars
    private static int W = 9;				// codeword width starts at 9 bits
    private static int L = 512;				// number of codewords start at 512
    private static float readbitsIn = 0;
    private static float compressedBits = 0;
    private static float origRatio= 0;					
    private static float currentRatio = 0;
    
	private static boolean dnMode = false;					//Do nothing mode
	private static boolean rMode = false;					//Reset mode
	private static boolean mMode = false;					//Monitor mode
	private static boolean monitor = false;					//flag for when to monitor compression ratio

    public static void compress() { 
    	
    	if(dnMode == true)
    	{
    		BinaryStdOut.write(0,8);
    	}
    	
    	if(rMode == true)
    	{
    		BinaryStdOut.write(1,8);
    	}
    	
    	if(mMode == true)
    	{
    		BinaryStdOut.write(2,8);
    	}
    	
    	
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.          
            //add bit width of s to compressedbits 
            compressedBits += W;
            
            //add length of s in bits to readBits
            int t = s.length();
            readbitsIn += t*8;
            
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
            
            
            if(code == L) //codebook is full so increase capacity
            {
            	if((W == 16)&& (dnMode == true))
            	{
            		//do nothing mode
            		
            	}
            	
            	//reset mode
            	else if((W == 16) && (rMode == true) && (code == 65536))
            	{
            		
            		W = 9;
    				L = 512;
    				
    		        st = new TST<Integer>();
    		        for (int i = 0; i < R; i++)
    		            st.put("" + (char) i, i);
    		        
    		        code = R+1;  // R is codeword for EOF
            	}
            	
            	
            	//monitor mode
            	else if((W == 16) && (mMode == true) &&(code == 65536))
            	{
            		//get compression ratio
            		currentRatio = getRatio();
            		
            		//begin monitoring
            		if(monitor == false)
            		{
            			monitor = true;
            			origRatio = currentRatio;
            		}
            		
              		if((monitor == true) && ((origRatio / currentRatio) > 1.1))
              		{
              			System.err.println("ratio is over 1.1 resetting codebook");
              				monitor = false;
              				origRatio = 0;
              				currentRatio = 0;
              				
                      		W = 9;
              				L = 512;
              				
              		        st = new TST<Integer>();
              		        for (int i = 0; i < R; i++)
              		            st.put("" + (char) i, i);
              		        
              		        code = R+1;  // R is codeword for EOF
              		}
            	}
            		
            
            	if(W!=16)
            	{	
            		W = W+1;
                	L = (int) Math.pow(2, W);
            	}
            }
        }
        BinaryStdOut.write(R, W);
        
        BinaryStdOut.close();
    } 

    public static void expand() {
    	//read compression mode
    	int mode = BinaryStdIn.readInt(8);
    	switch(mode)
    	{
    		case 0:
    			dnMode = true;
    			break;
    			
    		case 1:
    			rMode = true;
    			break;
    			
    		case 2:
    			mMode = true;
    			break;
    			
    		default:
    			break;
    	}
    	
    	
        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF, i= 257

        int codeword = BinaryStdIn.readInt(W);
        readbitsIn +=W;
        
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            readbitsIn += val.length() * 8;		//get number of bits for lenght of val
            
            codeword = BinaryStdIn.readInt(W);
            compressedBits += W;
            
            if (codeword == R) break;
            String s = st[codeword];						//get next codeword
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            
            if((i+1) == L)								//dictionary is full so increase bit width
            {
            	if((W == 16)&& (dnMode == true))
            	{
            		//do nothing mode
            		
            	}
            	
            	else if((W == 16)&& (rMode == true) && ((i+1) == 65536))
            	{
            		W = 9;
            		L = 512;
            		st = new String[65536];
            		// initialize symbol table with all 1-character strings
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";                        // (unused) lookahead for EOF, i= 257

                    codeword = BinaryStdIn.readInt(W);
                    if (codeword == R) return;           // expanded message is empty string
                    val = st[codeword];
            		
            	}
            	
            	//monitor mode
            	else if((W == 16) && (mMode == true) &&((i+1) == 65536))
            	{
            		//get compression ratio
            		currentRatio = getRatio();
            		
            		//begin monitoring
            		if(monitor == false)
            		{
            			monitor = true;
            			origRatio = currentRatio;
            		}
            		
              		if((monitor == true) && ((origRatio / currentRatio) > 1.1))
              		{
              			System.err.println("ratio is over 1.1 resetting codebook");
              				monitor = false;
                      		W = 9;
              				L = 512;
              				
              				st = new String[65536];
                    		// initialize symbol table with all 1-character strings
                            for (i = 0; i < R; i++)
                                st[i] = "" + (char) i;
                            st[i++] = "";                        // (unused) lookahead for EOF, i= 257

                            codeword = BinaryStdIn.readInt(W);
                            
                            if (codeword == R) return;           // expanded message is empty string
                            val = st[codeword];
                    		
              		}
            	}
            	
            	
            	if(W != 16)
            	{
                	W = W+1;
                	L = (int)Math.pow(2,W);
            	}
            	
            }
            val = s;
            
        }
        BinaryStdOut.close();
    }


 public static void main(String[] args) {
    	
        if(args[0].equals("-"))
        {
        	if(args[1].equals("n"))
        	{
        		dnMode = true;
        	}
        	if(args[1].equals("r"))
        	{
        		rMode = true;
        	}
        	if(args[1].equals("m"))
        	{
        		mMode = true;
        	}
        	compress();
        }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
 
 public static float getRatio()
 {
	 float ratio = readbitsIn / compressedBits;
	 return ratio;
 }


}
