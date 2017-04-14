package backend;

public class Post {
	private String postName;
	private String objectId;
	private String ownerId;
	private int pro;
	private int con;
	private String description;
	private String userId;
	private String groupId;
	
	public String getGroupId(){
		return this.groupId;
	}
	public String getUserId(){
		return this.userId;
	}
	public String getPostName(){
		return this.postName;
	}
	public String getObjectId(){
		return this.objectId;
	}
	public String getOwnerId(){
		return this.ownerId;
	}
	public int getPro(){
		return this.pro;
	}
	public int gerCon(){
		return this.con;
	}
	public String getDescription(){
		return this.description;
	}
	public void setPostName(String newPostName){
		this.postName= newPostName;
	}
	public void setPro(int pro){
		this.pro=pro;
	}
	public void setCon(int con){
		this.con=con;
	}
	public void addPro(int number){
		this. pro += number;
	}
	public void addCon(int number){
		this.con += number;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public void setDescription(String description){
		this.description= description;
	}
}

