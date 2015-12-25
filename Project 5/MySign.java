import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MySign
{
	public MySign()
	{
		
	}
	
	public static void main(String[] args)
	{
		
		//when given command to sign
		if(args[0].equals("s"))
		{
			//generate hash
			try
			{
				Path path = Paths.get(args[1]);
				byte[] data = Files.readAllBytes(path);
				
				// create class instance to create SHA-256 hash
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				
				// process the file
				md.update(data);
				// generate a hash of the file
				byte[] digest = md.digest();
				
				
				//decrypt using privkey.rsa
				//first convert to BigInteger
				BigInteger bInt = new BigInteger(digest);
				bInt = bInt.negate();					//accounts for loss of leading zeros
				
				//get private key
				File privateFile = new File("privkey.rsa");
				ObjectInputStream privateKey = new ObjectInputStream(new FileInputStream(privateFile));
				BigInteger D = new BigInteger((byte[]) privateKey.readObject());
				BigInteger N = new BigInteger((byte[]) privateKey.readObject());
				
				//decrypt
				BigInteger decrypt = bInt.modPow(D, N);
				
				//write signed version to a file
				//store original contents of file to a string
				String orig = new String(Files.readAllBytes(Paths.get(args[1])));
				
				File signFile = new File(args[1]+ ".signed");
				ObjectOutputStream signout = new ObjectOutputStream(new FileOutputStream(signFile));
				signout.writeObject(orig);
				signout.writeObject(decrypt.toByteArray());
				signout.close();

				
				
				
				
			}
			
			catch(Exception e)
			{
				System.out.println(e);
				System.exit(1);
			}
		}
		
		//when given command to verify
		if(args[0].equalsIgnoreCase("v"))
		{
			//read contents of original file
			try
			{
				File readFile = new File(args[1]);
				ObjectInputStream readin = new ObjectInputStream(new FileInputStream(readFile));
				String readOrig = (String) readin.readObject();
				
				//generate hash
				Path path = Paths.get(args[1]);
				byte[] data = readOrig.getBytes();
				
				// create class instance to create SHA-256 hash
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				
				// process the file
				md.update(data);
				// generate a hash of the file
				byte[] digest = md.digest();
				
				//convert to BigInteger
				BigInteger origHash = new BigInteger(digest);
				origHash = origHash.negate();				//accounts for loss of leading zeros
				
				
				//read decrypted hash of original file
				BigInteger readDecrypt = new BigInteger((byte[]) readin.readObject());
				
				//get public key
				File publicFile = new File("pubkey.rsa");
				ObjectInputStream publicKey = new ObjectInputStream(new FileInputStream(publicFile));
				BigInteger E = new BigInteger((byte[]) publicKey.readObject());
				BigInteger N = new BigInteger((byte[]) publicKey.readObject());
				
				//encrypt
				BigInteger encrypt = readDecrypt.modPow(E, N);
				
				
				//see if the hashes are the same
				if(encrypt.compareTo(origHash) == 0)
					System.out.println("The signature is valid\n");
				else
					System.out.println("Bad signature\n");
				
			}
			
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}