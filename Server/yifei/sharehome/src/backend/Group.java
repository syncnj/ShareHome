package backend;

//import com.backendless.*;
public class Group {
	private String groupName;
	private String ownerId;
	private String objectId; // group unique id
	private String teamMembersList;
	private String leaderId;
	private String postIdList;
	
	public String getPostIdList(){
		return postIdList;
	}
	public String getLeaderId(){
		return leaderId;
	}
	public String getObjectId() {
	    return objectId;
	  }
	public String getGroupName(){
		return groupName;
	}
	public String getOwnerId(){
		return ownerId;
	}
	public String getTeamMembersList(){
		return teamMembersList;
	}
	public void setGroupName( String groupName ) {
	    this.groupName= groupName;
	  }
//	public void setOwnerId( String ownerId ) {
//	    this.ownerId= ownerId;
//	  }
//	public void setObjectId (String objectId ) {
//	    this.objectId= objectId;
//	  }
	public void setLeaderId(String newLeaderId){
		this.leaderId= newLeaderId;
	}
	public void addTeamMember (String teamMember){
		this.teamMembersList += teamMember +",";
	}
	public void setTeamMembersList(String newTeamMembersList){
		this.teamMembersList = newTeamMembersList;
	}
	public void concatMembersLists(String teamMembersList){
		this.teamMembersList += teamMembersList;
	}
	public void setPostIdList(String postIdList){
		this.postIdList = postIdList;
	}
	public void addPost(String PostId){
		this.postIdList += PostId + ",";
	}
}
