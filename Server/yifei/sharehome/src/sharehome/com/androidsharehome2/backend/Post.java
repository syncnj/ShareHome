package sharehome.com.androidsharehome2.backend;

import com.backendless.Backendless;

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
	private String upVoteList;
	private String downVoteList;
	
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
//	public int gerCon(){
//		return this.downVote;
//	}


	public String getUpVoteList(){return this.upVoteList;}
	public String getDownVoteList() {return this.downVoteList;}

	public void setUpVoteList(String newList){this.upVoteList = newList;}
	public void setDownVoteList(String newList) {this.downVoteList = newList;}
	
	
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
		//Backendless.Persistence.save(this);
	}
	public void addDownVote(){
		this.downVote ++;
		//Backendless.Persistence.save(this);
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

