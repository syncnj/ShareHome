package sharehome.com.androidsharehome2.backend;

import java.util.Date;
import java.util.List;

import com.backendless.Backendless;

public class Task {
	private String taskName;
	private int duration; //days
	private String objectId;
	private String ownerId;
	private String membersIdList;
	private String userNameOnDuty;
	private String groupId;
	private Date startTime;
	
	public Date getStartTime(){
		return this.startTime;
	}
	public String getGroupId(){
		return this.groupId;
	}
	
	public String getTaskName(){
		return this.taskName;
	}
	public int getDuration(){
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
		return this.userNameOnDuty;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}

	public void setMembersIdList( String membersIdList){
		this.membersIdList = membersIdList;
	}
	
	public void setUserOnDuty(String userNameOnDuty){
		this.userNameOnDuty = userNameOnDuty;
	}
	
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	
	public void addMember(String memberId){
		this.membersIdList += memberId + ",";
		
	}
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
}




