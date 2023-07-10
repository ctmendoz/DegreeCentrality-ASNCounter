/*
CSCI 4460 - Lab 3 - Network Measurement and Analysis

@author Carlos Mendoza
@version 1.0
*/

import java.io.*;
import java.util.*;

public class asnCounter{

	static String currentDir = System.getProperty("user.dir");


	//The list of unique ASNs (used in Steps 1, 4, 7, 8)
	static ArrayList<String> uniqueASN = new ArrayList<String>();

	//The list of exclusively provider ASNs (used in Step 5)
	static ArrayList<String> uniqueProviderASN = new ArrayList<String>();

	//The list of exclusively peer ASNs (used in Step 6)
	static ArrayList<String> uniquePeerASN = new ArrayList<String>();

	//The list of exclusively tier 1 (all except customers) ASNs (used in Step 7)
	static ArrayList<String> tier1ASN = new ArrayList<String>();

	//The list of exclusively stub (all except providers) ASNs (used in Step 8)
	static ArrayList<String> stubASN = new ArrayList<String>();

	//The list of ASNs with their degree values (used in Step 4)
	static ArrayList<ASNObject> asnObjectList = new ArrayList<ASNObject>();

	//The list of Provider ASNs with their degree values (used in Step 5)
	static ArrayList<ASNObject> asnObjectListProvider = new ArrayList<ASNObject>();

	//The list of Peer ASNs with their degree values (used in Step 6)	
	static ArrayList<ASNObject> asnObjectListPeer = new ArrayList<ASNObject>();

	//The list of Tier 1 ASNs with their degree values (used in Step 7)
	static ArrayList<ASNObject> asnObjectListTier1 = new ArrayList<ASNObject>();

	//A hash set of the unique links (used for Step 2)
	static HashSet<String> uniqueLinks = new HashSet<String>();

	//A hash set of the unique p2p links (used for Step 3)
	static HashSet<String> uniqueLinksP2P = new HashSet<String>();

	//A hash set of the unique p2c links (used for Step 3)
	static HashSet<String> uniqueLinksP2C = new HashSet<String>();

	//A string builder used to save ASN1 or ASN2 before checking the relationship of the row (used in Steps 1-3 and 5)
	static StringBuilder sb = new StringBuilder(80);

	//A string builder used to save the first peer ASN in a link used later on in the row to see if the peer is unique (used for Step 6)
	static StringBuilder peer1SB = new StringBuilder(80);

	//A string builder used to save the second peer ASN in a link used later on in the row to see if the peer is unique (used for Step 6)
	static StringBuilder peer2SB = new StringBuilder(80);

	//A boolean variable that helps determine whether or not if an ASN1 is a provider and thus decides if the ASNs degree value should be implemented or if the ASN is not a stub ASN (used in Steps 5 and 8)
	static boolean maybeProvider = false;

	//A boolean variable that helps determine whether or not if an ASN2 is a customer and thus decides if the ASN is not a tier 1 ASN (used in Step 7)
	static boolean maybeCustomer = false;

	//A boolean variable that determines if an ASN is unique (used in Steps 1 and 5)
	static boolean unique = true;

	//A boolean variable that determines if ASN1 peer is unique and thus can be added to the uniquePeerASN list assuming that the realtionship is p2p (used in Step 6)
	static boolean uniquePeer1 = true;

	//A boolean variable that determines if ASN2 peer is unique and thus can be added to the uniquePeerASN list assuming that the realtionship is p2p (used in Step 6)
	static boolean uniquePeer2 = true;

	//A boolean variables used to determine if an ASN1 in asnObjectListPeer had its value incremented in case if the value has to be decremented due to the realtionship not being p2p (used in Step 6)	
	static boolean incremented1 = false;

	//A boolean variables used to determine if an ASN2 in asnObjectListPeer had its value incremented in case if the value has to be decremented due to the realtionship not being p2p (used in Step 6)	
	static boolean incremented2 = false;
	
	//A counter used make the top 10 node degree distribution lists easier to read (used in Steps 4-7)
	static int top10Counter = 1;

	//The value of the current ASN being analyzed to see how many connections it has throughout the dataset. Used to create an ASNObject which is then placed in an ASNObject list; the key to sorting an ASNObject list (used in Steps 4-7)
	static int currentValue;

	public static void main (String[] args){
		try{
		//***STEP 1 - Show how many unique ASNs are in the dataset***
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));


		while ((line = reader.readLine()) != null){
			String[] row = line.split("\\|");
			int i = 1;
				for (String index : row){
					if (i == 1){
						unique = true;
						try{
						for (int aIndex = 0; uniqueASN.get(aIndex) != null; aIndex++){
							if (index.matches(uniqueASN.get(aIndex))){
								unique = false;
							}
						}
						}
						catch(IndexOutOfBoundsException e){
						if (unique == true){
							uniqueASN.add(index);
						}
						}
					}
					if (i == 2){
						unique = true;
						try{
						for (int aIndex = 0; uniqueASN.get(aIndex) != null; aIndex++){
							if (index.matches(uniqueASN.get(aIndex))){
								unique = false;
							}
						}
						}
						catch(IndexOutOfBoundsException e){
						if (unique == true){
							uniqueASN.add(index);
						}
						}
					}
					i++;
				}
		}
		System.out.println("STEP 1: The number of unique ASNs in the dataset is " + uniqueASN.size() + ".");
	}
	catch(IOException e){
		e.printStackTrace();
	}
	//***END OF STEP 1***
	try{
		step2();
		step3();
		step4();
		step5ASNCollection();
		step5();
		step6ASNCollection();
		step6();
		step7ASNCollection();
		step7();
		step8();
	}
	catch(IOException e){
		e.printStackTrace();
	}
	}

	//***STEP 2 - Show how many unique links are in the dataset***
	public static void step2() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));


		while ((line = reader.readLine()) != null){
			String[] row = line.split("\\|");
			int i = 1;
				for (String index : row){
					if (i == 1){
						sb.append(index+"-");
					}
					if (i == 2){
						sb.append(index);
						uniqueLinks.add(sb.toString());
					}
					i++;
				}
				sb.setLength(0);
		}
		System.out.println("\nSTEP 2: The number of unique links in the dataset is " + uniqueLinks.size()/2 + ".");
	}
	//***END OF STEP 2***


	//***STEP 3 - Show how many unique p2p and p2c relations are in the dataset***
	public static void step3() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));


		while ((line = reader.readLine()) != null){
			String[] row = line.split("\\|");
			int i = 1;
				for (String index : row){
					if (i == 1){
						sb.append(index+"-");
					}
					if (i == 2){
						sb.append(index);
					}
					if(i == 3){
						//if p2p
						if (index.matches("0") == true){
							uniqueLinksP2P.add(sb.toString());
						}
						//if p2c
						if (index.matches("-1") == true){
							uniqueLinksP2C.add(sb.toString());
						}
					}
					i++;
				}
				sb.setLength(0);
		}
		System.out.println("\nSTEP 3: The number of unique p2p relations is " + uniqueLinksP2P.size()/2 + " and the number of unique p2c relations is " + uniqueLinksP2C.size()/2 + ".");
	}
	//***END OF STEP 3***


	//***STEP 4 - Find the top 10 largest ASNs in terms of connection size***	
	public static void step4() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		
		
		for (int a = 0; uniqueASN.size() != a; a++){
			String currentASN = uniqueASN.get(a);
			currentValue = 0;
			
			reader = new BufferedReader(new FileReader(textFile));	
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 1){
						if (currentASN.matches(index) == true){
							currentValue++;
						}
					}
					if (i == 2){
						if (currentASN.matches(index) == true){
							currentValue++;
						}
					}
					i++;
				}	
			}
			ASNObject newASN = new ASNObject(currentASN, currentValue/2);
			asnObjectList.add(newASN);
		}	
		sortStep4();
		System.out.println("\nSTEP 4: Largest ASNs");
		
		for (int i = 1; i != 11; i++){
			System.out.println( top10Counter + ". " + asnObjectList.get(asnObjectList.size()-i).toString());
			top10Counter++;		
		}
			top10Counter = 1;

	}

	public static void sortStep4(){
		int numElements = asnObjectList.size();
		for (int last = numElements-1; last > 0; last--) {
			boolean swapPerformed = false;
			for (int i = 0; i < last; i++){
				if (asnObjectList.get(i).getValue() > asnObjectList.get(i+1).getValue()){
					swapStep4(i, i+1);
					swapPerformed = true;
				}
			}

			if (swapPerformed != true){
				break;
			}
		}
	}

	public static void swapStep4 (int first, int second){
		Collections.swap(asnObjectList, first, second);
	}
	//***END OF STEP 4***

	//***STEP 5: Find the top 10 largest provider ASNs in terms of connection***
	public static void step5ASNCollection() throws IOException{
		//Must first get the unique ASNs that are providers
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));


		while ((line = reader.readLine()) != null){
			String[] row = line.split("\\|");
			int i = 1;
				for (String index : row){
					if (i == 1){
						unique = true;
						try{
						for (int aIndex = 0; uniqueProviderASN.get(aIndex) != null; aIndex++){
							if (index.matches(uniqueProviderASN.get(aIndex))){
								unique = false;
							}
						}
						}
						catch(IndexOutOfBoundsException e){
							if (unique == true){
								sb.append(index);
							}
						}
					}
					//checking if the relationship is p2c or not
					if (i == 3 && index.matches("-1") && unique == true){
						uniqueProviderASN.add(sb.toString());
					}
					i++;
				}
			sb.setLength(0);
		}	
	}

	public static void step5() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));
		
		for (int a = 0; uniqueProviderASN.size() != a; a++){
			String currentASN = uniqueProviderASN.get(a);
			currentValue = 0;
				
			reader = new BufferedReader(new FileReader(textFile));
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 1){
						maybeProvider = false;
						if (currentASN.matches(index) == true){
							maybeProvider = true;
						}
					}
					if (i == 3 && index.matches("-1") && maybeProvider == true){
						
							currentValue++;
						
					}
					i++;
				}	
			}
			ASNObject newASN = new ASNObject(currentASN, currentValue);
			asnObjectListProvider.add(newASN);
		}	
		sortStep5();
		System.out.println("\nSTEP 5: Largest Provider ASNs");
		
		for (int i = 1; i != 11; i++){
			System.out.println(top10Counter + ". " +asnObjectListProvider.get(asnObjectListProvider.size()-i).toString());
			top10Counter++;
		}
			top10Counter = 1;

	}

	public static void sortStep5(){
		int numElements = asnObjectListProvider.size();
		for (int last = numElements-1; last > 0; last--) {
			boolean swapPerformed = false;
			for (int i = 0; i < last; i++){
				if (asnObjectListProvider.get(i).getValue() > asnObjectListProvider.get(i+1).getValue()){
					swapStep5(i, i+1);
					swapPerformed = true;
				}
			}

			if (swapPerformed != true){
				break;
			}
		}
	}

	public static void swapStep5 (int first, int second){
		Collections.swap(asnObjectListProvider, first, second);
	}
	//***END OF STEP 5***


	//***STEP 6: Find the top 10 largest p2p ASNs in terms of connection size***
	public static void step6ASNCollection() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));


		while ((line = reader.readLine()) != null){
			String[] row = line.split("\\|");
			int i = 1;
				for (String index : row){
					if (i == 1){
						uniquePeer1 = true;
						try{
						for (int aIndex = 0; uniquePeerASN.get(aIndex) != null; aIndex++){
							if (index.matches(uniquePeerASN.get(aIndex))){
								uniquePeer1 = false;
							}
						}
						}
						catch(IndexOutOfBoundsException e){
							if (uniquePeer1 == true){
								peer1SB.append(index);
							}
						}
					}

					if (i == 2){
						uniquePeer2 = true;
						try{
						for (int aIndex = 0; uniquePeerASN.get(aIndex) != null; aIndex++){
							if (index.matches(uniquePeerASN.get(aIndex))){
								uniquePeer2 = false;
							}
						}
						}
						catch(IndexOutOfBoundsException e){
							if (uniquePeer2 == true){
								peer2SB.append(index);
							}
						}
					}
					if (i == 3 && index.matches("0") && uniquePeer1 == true){
						uniquePeerASN.add(peer1SB.toString());
					}
					if (i == 3 && index.matches("0") && uniquePeer2 == true){
						uniquePeerASN.add(peer2SB.toString());
					}
					i++;
				}
			peer1SB.setLength(0);
			peer2SB.setLength(0);
		}	
	}

	public static void step6() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));
		
		for (int a = 0; uniquePeerASN.size() != a; a++){
			String currentASN = uniquePeerASN.get(a);
			currentValue = 0;
				
			reader = new BufferedReader(new FileReader(textFile));
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 1){
						incremented1 = false;
						if (currentASN.matches(index) == true){
							currentValue++;
							incremented1 = true;
						}
					}
					if (i == 2){
						incremented2 = false;
						if (currentASN.matches(index) == true){
							currentValue++;
							incremented2 = true;
						}
					}

					if (i == 3 && index.matches("-1")){
						if (incremented1 == true){
							currentValue--;
						}
						if (incremented2 == true){
							currentValue--;
						}
					}
					i++;
				}
			}	 
			ASNObject newASN = new ASNObject(currentASN, currentValue/2);
			asnObjectListPeer.add(newASN);
		}	
		sortStep6();
		System.out.println("\nSTEP 6: Largest p2p ASNs");
		
		for (int i = 1; i != 11; i++){
			System.out.println(top10Counter + ". " + asnObjectListPeer.get(asnObjectListPeer.size()-i).toString());
			top10Counter++;
		}
			top10Counter = 1;

	}

	public static void sortStep6(){
		int numElements = asnObjectListPeer.size();
		for (int last = numElements-1; last > 0; last--) {
			boolean swapPerformed = false;
			for (int i = 0; i < last; i++){
				if (asnObjectListPeer.get(i).getValue() > asnObjectListPeer.get(i+1).getValue()){
					swapStep6(i, i+1);
					swapPerformed = true;
				}
			}

			if (swapPerformed != true){
				break;
			}
		}
	}

	public static void swapStep6 (int first, int second){
		Collections.swap(asnObjectListPeer, first, second);
	}
	//***END OF STEP 6***


	//***STEP 7: Find top 10 largest tier 1 ASNs in terms of connection size***
	public static void step7ASNCollection() throws IOException{
		tier1ASN = (ArrayList)uniqueASN.clone();

		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));
		for (int a = 0; tier1ASN.size() != a; a++){
			String currentASN = tier1ASN.get(a);
				
			reader = new BufferedReader(new FileReader(textFile));
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 2){
						maybeCustomer = false;
						if (currentASN.matches(index) == true){
							maybeCustomer = true;
						}
					}

					if (i == 3 && index.matches("-1") && maybeCustomer == true){
						tier1ASN.remove(a);
					}
					i++;
				}
			}
		}
	}

	public static void step7() throws IOException{
		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		
		
		for (int a = 0; tier1ASN.size() != a; a++){
			String currentASN = tier1ASN.get(a);
			currentValue = 0;
			
			reader = new BufferedReader(new FileReader(textFile));	
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 1){
						if (currentASN.matches(index) == true){
							currentValue++;
						}
					}
					if (i == 2){
						if (currentASN.matches(index) == true){
							currentValue++;
						}
					}
					i++;
				}	
			}
			ASNObject newASN = new ASNObject(currentASN, currentValue/2);
			asnObjectListTier1.add(newASN);
		}	
		sortStep7();
		System.out.println("\nSTEP 7: Largest Tier 1 ASNs");
		
		for (int i = 1; i != 11; i++){
			System.out.println( top10Counter + ". " + asnObjectListTier1.get(asnObjectListTier1.size()-i).toString());
			top10Counter++;		
		}
			top10Counter = 1;

	}

	public static void sortStep7(){
		int numElements = asnObjectListTier1.size();
		for (int last = numElements-1; last > 0; last--) {
			boolean swapPerformed = false;
			for (int i = 0; i < last; i++){
				if (asnObjectListTier1.get(i).getValue() > asnObjectListTier1.get(i+1).getValue()){
					swapStep7(i, i+1);
					swapPerformed = true;
				}
			}

			if (swapPerformed != true){
				break;
			}
		}
	}

	public static void swapStep7 (int first, int second){
		Collections.swap(asnObjectListTier1, first, second);
	}
	//***END OF STEP 7***


	//***STEP 8: Show how many stub AS are in the dataset***
	public static void step8() throws IOException{
		stubASN = (ArrayList)uniqueASN.clone();

		String textFile = currentDir + "/ASRelationship-2022-10-01.txt";		
		BufferedReader reader = null;
		String line = "";
		
		reader = new BufferedReader(new FileReader(textFile));
		
		for (int a = 0; stubASN.size() != a; a++){
			String currentASN = stubASN.get(a);
				
			reader = new BufferedReader(new FileReader(textFile));
			while ((line = reader.readLine()) != null){
				String[] row = line.split("\\|");
				int i = 1;
				
				for (String index : row){
					if (i == 1){
						maybeProvider = false;
						if (currentASN.matches(index) == true){
							maybeProvider = true;
						}
					}

					if (i == 3 && index.matches("-1") && maybeProvider == true){
						stubASN.remove(a);
					}
					i++;
				}
			}
		}

		System.out.print("\nSTEP 8: There are " + stubASN.size() + " stub ASNs in the dataset.");
	}
	//***END OF STEP 8
}