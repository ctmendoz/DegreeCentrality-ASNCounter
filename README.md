<b>I. SUMMARY:</b>
- This was an assignment for my Network Operation & Defense class. It demonstrated knowledge of Java, Autonomous Systems (AS), Internet Service Providers (ISP), Degree Centrality, and Network Measurement in general.
- The program scans through exactly 463,141 lines of text representing the different AS relationships of a specific date multiple times in order to find the total number of ASes, the number of relationships between them, and the different types of ASes with the largest degrees of measurement via Degree Centrality.
- It consists of two Java files, three text files, and a pdf report.
	- The Java Files: 
		- ASNObject.java is used to keep track of the ASes and ensure that their ASNs (Autonomous System Numbers) and degrees remain unique and true so that they can be placed into ArrayLists that are eventually sorted in order to determine the specific type of AS with the largest degrees as seen in asnCounter.java. 
		- asnCounter.java exists as the core Java file that runs the program as a whole. It keeps track of how many unique ASes there are and uses this to then keep track of the connections between them.
	- The Text Files: 
		- ASRelationship-2022-10-01.txt is the file that is expected to be read through.
		- test.txt consists of the first 37 AS relationships of ASRelationship-2022-10-01.txt and can be used to confirm asnCounter.java's viability due to its significantly lower running time in comparison to the default text file. 
		- output_V2.txt shows what running the program with the default text file being read results in.
	- The PDF File:
		- CarlosMendoza_ASN_DegreeCentrality_Report.pdf further details the function and results of the program, as well as descriptions of the different types of ASes, AS relationships, and methods of analyzing networks.

<b>II. HOW TO RUN:</b>
- In a command terminal, go to the directory where asnCounter.java is located.
- Ensure that ASNObject.java and ASRelationship-2022-10-01.txt are included in the same folder as the asnCounter.java file.
- Ensure that the java files are compiled by typing in "javac ASNObject.java" and "javac asnCounter.java".
- Since the program takes a long time to read the ASRelationship-2022-10-01.txt file (~1 hour???), you can confirm that the program works by changing lines 91, 160, 189, 227, 295, 332, 401, 457, 540, 571, and 641 in the asnCounter.java file (best done by using CTRL + F --> "ASRelationship-2022-10-01.txt") so that the program reads the test.txt file, a much shorter version of the default .txt file, included in this repository.
	- Otherwise, keep the default .txt file and run the program using "java asnCounter.java"
