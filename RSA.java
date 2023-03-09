import java.util.*;

public class RSA
{
	public static void main(String[] args) {
	    
		// to find random set of p,q and e and find equivalent N, phi(N) and d.
		/*long p = findp();
		long q = findq();
		long N = p * q;
		long phiN = (p - 1) * (q - 1);
		long e = finde(phiN);
		long d = modInverse(e, phiN);*/
		
		
		// my set of keys 
		long my_p = 60149L;    //private
		long my_q = 38639L;    //private
		long my_e = 1430751485L; //public key
		long my_N = my_p * my_q;    //public key
		long my_phiN = (my_p - 1) * (my_q - 1);  //private
		long my_d = 492580885L;   //private key (d=modInverse(my_e,my_phiN;)
		System.out.println("#MY DATA\nmy p = " + my_p + "\nmy q = " + my_q + 
		"\nmy N = " + my_N + "\nmy phi(N) = " + my_phiN + "\nmy e = " + my_e + 
		"\nmy d = " + my_d);
		
		
		// partner's public key 
		long partner_e = 1460662383L;          
		long partner_N = 2493823987L;            
		System.out.println("\n\n#MY PARTNER'S DATA\npartner's e: " + partner_e +
		"\npartner's N: " + partner_N);
		
		
		// Encryption of my message using my partner's Public Key
		String my_plainText = "Winter is here!";
		System.out.println("\n\n#PART 1: RSA ENCRYPTION AND DECRYPTION\n" + 
		"\n#ENCRYPTION\nMy Plain Text: " + my_plainText);
		System.out.println("Encrypting");
		System.out.println("Corresponding Cipher Text: " + 
		encrypt(stringToChunks(my_plainText), partner_e, partner_N));
		
		
		// Decryption of partner's cipher text using my Private Key
	    List<Long> partner_cipherText = new ArrayList<Long>(Arrays.asList(196921526L, 1943625372L, 910383063L, 967597124L, 156851374L));
		System.out.println("\n#DECRYPTION\nPartner's Cipher Text: " + 
		partner_cipherText);
		System.out.println("Decrypting");
		System.out.println("Recovered Text: " + 
		decrypt(partner_cipherText, my_d, my_N));
		
		
		// Signature: Signer
		String my_Name = "Dhruv Haribhakti";
		System.out.println("\n\n#PART 2: RSA SIGNATURE\n\n#MY SIGN\n" + 
		"My message: " + my_Name);
		List<Long> hexi = encrypt(stringToChunks(my_Name), my_d, my_N);
		System.out.println("My Signature: " + hexi);
		
		
		// Signature: Verifier
		List<Long> partner_Sign = new ArrayList<Long>(Arrays.asList(291967533L, 583613464L, 1490286841L, 957175031L, 1827922398L, 956616749L, 1757707138L, 513002766L)); 
		String partner_Name = "Bordeau--Aubert Korantin";
		System.out.println("\n#VERIFICATION\nPartner's Name: " + 
		partner_Name + "\nPartner's Signature: " + partner_Sign);
		String decr_Name = decrypt(partner_Sign, partner_e, partner_N);
		System.out.println("Decrypted Message: " + decr_Name);
		System.out.print("VERIFICATION: ");
		if (partner_Name.equals(decr_Name)){
		    System.out.println("Message Verified.");
		}
		else{
		    System.out.println("Message Not Verified.");
		}
		
	}  
	
	public static boolean isPrime(long number){
	    for(int i = 2; i <= number/2; i++){
	        if(number % i == 0)
	            return false;
	    }
	    return true;
	}
	
	public static long findp(){
	    long p = 32768L + (long) (Math.random() * (65535L - 32768L));
	    while(!isPrime(p))
	        p = 32768L + (long) (Math.random() * (65535L - 32768L));
	    return p;
	}
	
	public static long findq(){
	    Random rand = new Random();
	    long q = 32768L + (long) (Math.random() * (65535L - 32768L));
	    while(!isPrime(q))
	        q = 32768L + (long) (Math.random() * (65535L - 32768L));
	    return q;
	}
	
	public static long finde(long phiN){
	    Random rand = new Random();
	    long e = 100L + (long) (Math.random() * (phiN - 100L));
	    while(!isRprime(e, phiN))
	        e = 100L + (long) (Math.random() * (phiN - 100L));
	    return e;
	}
	
	public static long modInverse(long e, long phiN){
        for (int i = 1; i < phiN; i++)
            if (((e%phiN) * (i%phiN)) % phiN == 1)
                return (long) i;
        return 1;
    }
	
	public static boolean isRprime(long e, long phiN){
	    long gcd = 1;
	    for(long i = 1; i <= e && i <= phiN; i++){
	        if(e % i == 0 && phiN % i == 0)
	            gcd = i;
	    }
	    if(gcd == 1)
	        return true;
	    return false;
	}
	
	public static List<String> stringToChunks(String encr){
	    List<String> chunks = new ArrayList<String>();
	    for(int i = 0; i < encr.length(); i += 3){
	        chunks.add(encr.substring(i, Math.min(encr.length(), i + 3)));
	    }
	    return chunks;
	}
	
	public static List<Long> encrypt(List<String> chunk, long e, long N){
	    List<Long> plainText = new ArrayList<Long>();
	    System.out.println("Chunk list: " + chunk);
	    for(int i = 0; i < chunk.size(); i++){
	        StringBuilder str = new StringBuilder();
	        str.append("0x");
            for (char c: chunk.get(i).toCharArray()) {
                str.append(String.format("%x", (int) c));
            }
            //System.out.println("Hex of chunk " + chunk.get(i) + ": " + str);
            plainText.add(Long.decode(str.toString()));
            //System.out.println("Integer of chunk " + chunk.get(i) + ": " + plainText.get(i));
	    }
	    for(int i = 0; i < plainText.size(); i++){
	        long rem = plainText.get(i);
	        long prod = 1;
	        String str = Long.toBinaryString(e);
	        for(int j = str.length()-2; j >= 0; j--){
	            rem = (rem * rem) % N;
	            if(str.charAt(j) == '1'){
	                prod = (prod * rem) % N;
	            }
	        }
	        prod = (prod * plainText.get(i)) % N;
	        plainText.set(i, prod);
	    }
	    return plainText;
    }
    
    public static String decrypt(List<Long> cipher, long d, long N){
        for(int i = 0; i < cipher.size(); i++){
	        long rem = cipher.get(i);
	        long prod = 1;
	        String str = Long.toBinaryString(d);
	        for(int j = str.length()-2; j >= 0; j--){
	            rem = (rem * rem) % N;
	            if(str.charAt(j) == '1'){
	                prod = (prod * rem) % N;
	            }
	        }
	        prod = (prod * cipher.get(i))%N;
	        cipher.set(i, prod);
	        //System.out.println("Integer of chunk" + i + " " + cipher.get(i));
	    }
	    List<String> hexString = new ArrayList<String>();
	    for(int i = 0; i < cipher.size(); i++){
	        String hex = Long.toHexString(cipher.get(i));
	        hexString.add(hex);
	    }
	    //System.out.println("Hex String: " + hexString);
	    
	    List<String> chunkList = new ArrayList<String>();
	    StringBuilder recoveredText = new StringBuilder();
        for(int i = 0; i < hexString.size(); i++){
            int inc = 0;
	        String temp = "";
	        String chunk = "";
	        for(int j = 0; j < hexString.get(i).length(); j++){
	            temp += (hexString.get(i).charAt(j));
	            if(j % 2 != 0){
	                String y = temp;
	                int x = Integer.parseInt(y,16);
	                temp = "";
	                recoveredText.append((char) x);
	                chunk += (char) x;
	            }
	        }
	        chunkList.add(chunk);
	        chunk = "";
	    }
	    System.out.println("Chunk List: " + chunkList);
	    return recoveredText.toString();
    }
}
