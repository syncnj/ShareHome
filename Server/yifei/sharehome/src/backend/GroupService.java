package backend;


import com.backendless.*;
import com.backendless.servercode.IBackendlessService;

public class GroupService implements IBackendlessService{

	public void createNewGroup(String groupName, String leaderId){
		if(groupName == null || ownerId == null){
			throw new IllegalArgumentException("Missing input message!!!");
		}
		Group group = new Group();
		group.setGroupName(groupName);
		group.setLeaderId(leaderId);
		group.addTeamMember(leaderId);
		Group savedGroup = Backendless.Persistence.save( group );
	}
	
	public boolean addMember(String newGroupMemberId,String groupId){
		if(newGroupMemberId == null || groupId == null){
			throw new IllegalArgumentException("Missing input message!!!");
		}
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		if(group == null){
			return false;
		}
		else{
			group.addTeamMember(newGroupMemberId);
			return true;
		}
	}
	 public String getMemberList(String groupId){
		 if( groupId == null){
				throw new IllegalArgumentException("Missing input message!!!");
			}
		 Group group = Backendless.Persistence.of(Group.class).findById(groupId);
			if(group == null){
				return null;
			}
			else{
			
				return group.getTeamMembersList();
			}
	 }
	
	 
	public boolean deleteMember(String groupId, String memberId) throws Exception{
		if(groupId== null || memberId == null){
			throw new IllegalArgumentException("Missing input message!!!");
		}
		 Group group = Backendless.Persistence.of(Group.class).findById(groupId);
			if(group == null){
				return false;
			}
			else{
				String memberList =group.getTeamMembersList();
				if(memberList.contains(memberId)){
					int indexOfFirstChar = memberList.indexOf(memberId);
					if(indexOfFirstChar == 0){  // member is owner
						throw new Exception("owner can not be deleted!!!");
					}
					else if (indexOfFirstChar + memberId.length() + 1 == memberList.length()){
						// member is last user in group
						
						
						group.setTeamMembersList(memberList.substring(0, indexOfFirstChar));
						
						return  true;
					}
					else{
						// member is neither owner nor the last user
						String beforeDeletedMember = group.getTeamMembersList().substring(0, indexOfFirstChar);
						String afterDeletedMember = group.getTeamMembersList().substring((indexOfFirstChar+memberId.length()+1), group.getTeamMembersList().length());
						group.setTeamMembersList(beforeDeletedMember+afterDeletedMember);
						return true;
					}
				}
				return false ;
			}
	}
	
}
