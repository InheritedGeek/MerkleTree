package cryptosystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MerkleTree {
	
	private static BufferedReader br;

	public static void main(String[] args) throws IOException {
		
		String filename = "CrimeLatLonXY.csv";
		br = new BufferedReader(new FileReader(new File(filename)));
		String line;
		
		SinglyLinkedList sl = new SinglyLinkedList();
		SinglyLinkedList slhash = new SinglyLinkedList();
		ArrayList<String> value = new ArrayList<String>();
		
		int cnt =0;
		// Inititalising values in two separate linked lists
		// and calling recursive function
		while ((line = br.readLine()) != null) {			
			if (cnt ==0) {
				sl.head = new ObjectNode(line, sl.tail);
				try {
					String hashval = h(line);
					slhash.head = new ObjectNode(hashval, slhash.tail);
					value.add(hashval);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				sl.addAtEndNode(new ObjectNode(line, sl.tail));
				try {
					String hashval = h(line);
					slhash.addAtEndNode(new ObjectNode(hashval, slhash.tail));
					value.add(hashval);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			cnt++;
		}
		
		String output = Recursion(value).get(0);
		System.out.println("The Merkel root for "+filename+" is "+"\n"+ output);
	}
	
	// Recursive process to store data in ArrayList and recursively calling
	// until the length of the list is 1.
	// One every call we calculate the joint hash value for two consecutive indexes
	// Then update the first index with that value and deleting the next index
	public static ArrayList<String> Recursion(ArrayList<String> value) {
		
		if (value.size() ==1)
			return value;
		
		int indx =0;
		
		if (value.size() %2 !=0)
			value.add(value.get(value.size()-1));
		
		int maxval = value.size()/2;
		for (int i =0;i< maxval;i++) {
			
			String val = value.get(indx) + value.get(indx+1) ;
			String hasval = null;
			
			try {
				hasval = h(val);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			value.set(indx, hasval);
			value.remove(indx+1);
			indx++;
		}
		return Recursion(value);
		
	}
	
	// Calculating SHA-256 has of a String
	public static String h(String text) throws NoSuchAlgorithmException {
		 MessageDigest digest = MessageDigest.getInstance("SHA-256");
		 
		 byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		 
		 StringBuffer sb = new StringBuffer();

		 for (int i = 0; i <= 31; i++) {
			 byte b = hash[i];
			 sb.append(String.format("%02X", b));
		 }
		 return sb.toString();
	}
}
