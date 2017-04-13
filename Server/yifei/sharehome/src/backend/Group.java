package backend;

//import com.backendless.*;
public class Group {
	private String groupName;
	private String ownerId;
	private String objectId; // group unique id
	private String teamMembersList;
	
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
	public void setOwnerId( String ownerId ) {
	    this.ownerId= ownerId;
	  }
	public void setObjectId (String objectId ) {
	    this.objectId= objectId;
	  }
	public void setTeamMembersList (String teamMembersList){
		this.teamMembersList += teamMembersList +",";
	}
}
