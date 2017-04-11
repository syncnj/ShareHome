package backend;


import com.backendless.*;
import com.backendless.servercode.IBackendlessService;

public class GroupService implements IBackendlessService{

	public void createNewGroup(String groupName, String ownerId){
		Group group = new Group();
		group.setGroupName(groupName);
		group.setOwnerId(ownerId);
		group.setTeamMembersList(null);
		Group savedGroup = Backendless.Persistence.save( group );
	}
	
	public boolean addNewMember(String newGroupMemberId,String groupId){
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		if(group == null){
			return false;
		}
		else{
			group.setTeamMembersList(newGroupMemberId);
			return true;
		}
	}
}
