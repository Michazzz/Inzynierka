package pl.edu.zut.mwojtalewicz.Utils;

public class SearchFriendsItem {
	
	private String uniqueID;
	private String name;
	private String lastname;
	private String email;
	private String uid;
		
	public SearchFriendsItem(String uniqueID, String name, String lastname, String email, String uid)
	{
		this.uniqueID = uniqueID;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.uid = uid;
	}
	
	public String getUniqueID(){
		return uniqueID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLastname(){
		return lastname;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getUid(){
		return uid;
	}
}
