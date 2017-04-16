package backend;


import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.servercode.IBackendlessService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupService implements IBackendlessService{

	public void createNewGroup(String groupName, String leaderId){

		if(groupName == null || leaderId== null || groupName.trim() == "" || leaderId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}

		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		String whereClause = "objectId = '" + leaderId.trim() + "'";
		dataQuery.setWhereClause( whereClause );

		BackendlessCollection<BackendlessUser> userResult = Backendless.Data.of( BackendlessUser.class).find(dataQuery);

		if (userResult.getCurrentPage().isEmpty() ){
			throw new RuntimeException("Leader ID is invalid or could not be found in the database");
		}
		
		
		
		// Now find if group name already exist:
//		whereClause = "groupName = '" + groupName.trim() + "'";
//		dataQuery.setWhereClause( whereClause );
//
//		BackendlessCollection<Map> result = Backendless.Persistence.of( "Group" ).find( dataQuery);
//
//		if ( !result.getCurrentPage().isEmpty()){
//			throw new RuntimeException("Group already exist in the table: " + result.getCurrentPage());
//
//		}


		Group group = new Group();
		group.setGroupName(groupName);
		group.setLeaderId(leaderId);
		group.setTeamMembersList(leaderId +",");
		group.setPostIdList("");
		
	 	Backendless.Persistence.save( group );
	 	BackendlessUser user =Backendless.UserService.findById(leaderId);
	 	user.setProperty("groupId", group.getObjectId());
	 	Backendless.UserService.update(user);
	}
	
	public boolean addMember(String newGroupMemberId,String groupId){
		newGroupMemberId = newGroupMemberId.trim();
		if(newGroupMemberId == null || groupId == null || groupId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}

		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		if(group == null){
			throw new RuntimeException("Group can't be found using groupID: " + groupId);
			//return false;
		}

		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		String whereClause = "objectId = '" + newGroupMemberId.trim() + "'";
		dataQuery.setWhereClause( whereClause );

		BackendlessCollection<BackendlessUser> userResult = Backendless.Data.of( BackendlessUser.class).find(dataQuery);

		if (userResult.getCurrentPage().isEmpty() ){
			throw new RuntimeException("newGroupMemberId is invalid or could not be found in the user database");
		}

		if (group.getTeamMembersList().contains(newGroupMemberId)){
			throw new RuntimeException("Member already in group");
		}

		group.addTeamMember(newGroupMemberId);
		Backendless.Persistence.save( group );
		
		BackendlessUser user =Backendless.UserService.findById(newGroupMemberId);
	 	user.setProperty("groupId", group.getObjectId());
	 	Backendless.UserService.update(user);
		
		return true;

	}

	 public String getMemberList(String groupId){
		 if( groupId == null){
				throw new IllegalArgumentException("Missing input message!!!");
			}
		 Group group = Backendless.Persistence.of(Group.class).findById(groupId.trim());
			if(group == null){
				throw new RuntimeException("Group can't be found in the database");
			}
			else{
				return group.getTeamMembersList();
			}
	 }
	
	 
	public boolean deleteMember(String groupId, String memberId) throws Exception{
		if(groupId== null || memberId == null) {
			throw new IllegalArgumentException("Missing input message!!!");
		}
		if ( groupId.trim() == ""  || memberId.trim().isEmpty()){
			throw new IllegalArgumentException("Input is empty:" + memberId.trim());
		}

		 Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		if (group == null) {
			throw new RuntimeException("Group can't be found in the database");
		}

		String memberList = group.getTeamMembersList();

		if (memberList.contains(memberId)) {
			int indexOfFirstChar = memberList.indexOf(memberId);
			if (indexOfFirstChar == 0) {  // member is owner
				throw new Exception("owner can not be deleted!!!");

			}


			else if (indexOfFirstChar + memberId.length() + 1 == memberList.length()) {
				// member is last user in group
				if ( memberList.charAt(indexOfFirstChar - 1) != ',' ){
					throw new Exception("Member can't be found");
				}

				group.setTeamMembersList(memberList.substring(0, indexOfFirstChar));
				Backendless.Persistence.save(group);
				
				BackendlessUser user =Backendless.UserService.findById(memberId);
			 	user.setProperty("groupId", "");
			 	Backendless.UserService.update(user);
			 	
				return true;
			} else {
				// member is neither owner nor the last user

				if ( memberList.charAt(indexOfFirstChar - 1) != ',' || memberList.charAt(memberList.lastIndexOf(memberId) + 1) != ','){
					throw new Exception("Member can't be found");
				}

				String beforeDeletedMember = group.getTeamMembersList().substring(0, indexOfFirstChar);
				String afterDeletedMember = group.getTeamMembersList().substring((indexOfFirstChar + memberId.length() + 1), group.getTeamMembersList().length());
				group.setTeamMembersList(beforeDeletedMember + afterDeletedMember);
				Backendless.Persistence.save(group);
				
				BackendlessUser user =Backendless.UserService.findById(memberId);
			 	user.setProperty("groupId", "");
			 	Backendless.UserService.update(user);
				
				return true;
			}
		}
		throw new Exception("Member can't be found");

	}
	
	public List<Post> getAllPost(String groupId){
		 if( groupId == null){
				throw new IllegalArgumentException("Missing input message!!!");
			}
		// Group group = Backendless.Persistence.of(Group.class).findById(groupId.trim());
		 
//		 QueryOptions queryOptions = new QueryOptions();
//		 List<String> sortBy = new ArrayList<String>();
//		 sortBy.add("created DESC");
//		 queryOptions.setSortBy(sortBy);
//		 
//		 String whereClause = "groupId = '" + groupId.trim() + "'";
//		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
//		 dataQuery.setWhereClause(whereClause);
//		 dataQuery.setQueryOptions(queryOptions);
//		 
//		 BackendlessCollection<Post> postResult = Backendless.Data.of(Post.class).find(dataQuery);
//		 return postResult.getCurrentPage();
		 
		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
			String whereClause = "groupId = '" + groupId.trim() + "'";
			dataQuery.setWhereClause( whereClause );

			BackendlessCollection<Post> postResult = Backendless.Data.of( Post.class).find(dataQuery);
			return postResult.getCurrentPage();
		
		
	}
	
	
}
