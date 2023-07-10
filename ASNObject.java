//An ASN's number and its degree which is used for comparison and sorting for steps 4-7
public class ASNObject{
	//instance variables

	//the ASN number
	String id;
	//the degree/connection size/number of unique links of the ASN
	int value;

	//constructor
	public ASNObject (String id, int value){
		this.id = id;
		this.value = value;
	}

	//retrieves the ASN number
	public String getId() {
		return this.id;
	}

	//retrieves the connection size
	public int getValue() {
		return this.value;
	}

	//prints the ASN and the degree in the format shown in the lab3 handout
	public String toString() {
        return "        ASN" + this.id + " degree = " + this.value + "";
    }
}