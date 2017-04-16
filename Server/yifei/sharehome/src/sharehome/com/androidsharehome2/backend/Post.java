package sharehome.com.androidsharehome2.backend;

public class Post {
	private String postTitle;
	private String objectId;
	private String ownerId;
	private int upVote = 0;
	private int downVote = 0;
	private String content;
	private String userId;
	private String groupId;
	private boolean anonymous;

	
	public String getGroupId(){
		return this.groupId;
	}
	public String getUserId(){
		return this.userId;
	}
	public String getPostTitle(){
		return this.postTitle;
	}
	public String getObjectId(){
		return this.objectId;
	}
	public String getOwnerId(){
		return this.ownerId;
	}
	public int getUpVote(){
		return this.upVote;
	}
	public int getDownVote(){
		return this.downVote;
	}
	public int gerCon(){
		return this.downVote;
	}
	public String getContent(){
		return this.content;
	}
	public void setPostTitle(String newPostName){
		this.postTitle = newPostName;
	}
	public void setUpVote(int upVote){
		this.upVote = upVote;
	}
	public void setDownVote(int downVote){
		this.downVote = downVote;
	}
	public void addUpVote(){
		this.upVote ++;
	}
	public void addDownVote(){
		this.downVote ++;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public void setContent(String content){
		this.content = content;
	}

	public boolean getAnonymous() {return this.anonymous;}
	public void setAnonymous (boolean newAnonymous) { this.anonymous = newAnonymous;}
}

