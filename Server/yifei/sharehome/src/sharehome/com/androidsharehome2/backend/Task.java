package sharehome.com.androidsharehome2.backend;
import java.util.List;

import com.backendless.Backendless;

public class Task {
	private String taskName;
	private String duration;
	private String objectId;
	private String ownerId;
	private String membersIdList;
	private String userOnDuty;
	private String groupId;
	
	public String getGroupId(){
		return this.groupId;
	}
	
	public String getTaskName(){
		return this.taskName;
	}
	public String getDuration(){
		return this.duration;
	}
	public String getObjectId(){
		return this.objectId;
	}
	public String getOwnerId(){
		return this.ownerId;
	}
	public String getMembersIdList(){
		return this.membersIdList;
	}
	public String getUserOnDuty(){
		return this.userOnDuty;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}
	
	public void setDuration(String duration){
		this.duration = duration;
	}

	public void setMembersIdList( String membersIdList){
		this.membersIdList = membersIdList;
	}
	
	public void setUserOnDuty(String userOnDuty){
		this.userOnDuty = userOnDuty;
	}
	
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	
	public void addMember(String memberId){
		this.membersIdList += memberId + ",";
		Backendless.Persistence.save(this);
	}
}




