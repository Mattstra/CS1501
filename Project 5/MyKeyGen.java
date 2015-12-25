import java.util.*;
import java.math.BigInteger;
import java.io.*;

public class MyKeyGen
{
	public MyKeyGen()
	{
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		/* P and Q are random 512 bit prime numbers
		 * N is the product of P and Q
		 * phi represents phi(N) as (P-1)*(Q-1)
		 * E is the value such that 1 < E < H
		 * D is E inverse mod(phi)
		 */
		BigInteger P, Q, N, phi, E = null, D;
		MyKeyGen gen = new MyKeyGen();
		Boolean again = true;
		Random rnd = new Random();
		
		P = BigInteger.probablePrime(512, rnd);
		Q = BigInteger.probablePrime(512, rnd);
		N = P.multiply(Q);
		phi = (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
		
		while(again == true)
		{
			Random rand = new Random();
			E = new BigInteger(512, rand);
			
			//check to see if 1 < E < phi
			if((E.compareTo(phi) == -1) && (E.compareTo(BigInteger.ZERO) == 1))
			{
				//make sure gcd of E and phi is 1
				if(E.gcd(phi).equals(BigInteger.ONE))
					again = false;
				
				else
					again = true;
			}
		}
		
		D = E.modInverse(phi);
		
		//save key pairs to .rsa files
		File publicFile = new File("pubkey.rsa");
		File privateFile = new File("privkey.rsa");
		ObjectOutputStream publicKey = new ObjectOutputStream(new FileOutputStream(publicFile));
		ObjectOutputStream privateKey = new ObjectOutputStream(new FileOutputStream(privateFile));
		
		publicKey.writeObject(E.toByteArray());
		publicKey.writeObject(N.toByteArray());
		privateKey.writeObject(D.toByteArray());
		privateKey.writeObject(N.toByteArray());
		
		
		publicKey.close();
		privateKey.close();
		
		System.out.println("public and private keys created\n");
	}
}

