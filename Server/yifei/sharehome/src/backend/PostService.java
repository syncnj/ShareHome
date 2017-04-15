package backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.servercode.IBackendlessService;

import java.util.Map;


// Wondering should we change our post service and return the objectId
// also


public class PostService implements IBackendlessService{
	public String  createNewPost(String userId, String groupId,String postTitle,String postContent){
		if(groupId == null || userId== null || postTitle== null|| postContent == null ||
				groupId.trim() == "" || userId.trim() == ""|| postTitle.trim() ==""|| postContent.trim()==""){
			throw new IllegalArgumentException("Invalid input argument");
		}

		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		String whereClause = "objectId = '" + userId.trim() + "'";
		dataQuery.setWhereClause( whereClause );

		BackendlessCollection<BackendlessUser> userResult = Backendless.Data.of( BackendlessUser.class).find(dataQuery);

		if (userResult.getCurrentPage().isEmpty()){
			throw new RuntimeException(" ID is invalid or could not be found in the database");
		}


		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		// Find so that if error then return

		//Now check for duplicate:
		whereClause = "groupId = '" + groupId.trim() + "' AND postTitle = '" + postTitle.trim() + "'";
		//if (true)throw new RuntimeException(""+ whereClause);
		dataQuery.setWhereClause( whereClause );
		BackendlessCollection<Map> result = Backendless.Persistence.of( "Post" ).find( dataQuery);

		if ( !result.getCurrentPage().isEmpty()){
			throw new RuntimeException("Same Post already exist" + result.getCurrentPage());

		}

		
		
		//create a new post if group and user exist
		Post post = new Post();
		post.setPostTitle(postTitle);
		post.setUserId(userId);
		post.setGroupId(groupId);
		//post.setDownVote(0);
		//post.setUpVote(0);
		post.setContent(postContent);
		Backendless.Persistence.save(post);
		group.addPost(post.getObjectId());
		Backendless.Persistence.save(group);
		return post.getObjectId();
	}
	public void deletedPost(String postId, String userId){
		if(postId == null || userId == null || postId.trim() == "" || userId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		String whereClause = "objectId = '" + userId.trim() + "'";
		dataQuery.setWhereClause( whereClause );

		BackendlessCollection<BackendlessUser> userResult = Backendless.Data.of( BackendlessUser.class).find(dataQuery);

		if (userResult.getCurrentPage().isEmpty()){
			throw new RuntimeException(" ID is invalid or could not be found in the database");
		}
		
		Post post = Backendless.Persistence.of(Post.class).findById(postId);
		
		Backendless.Persistence.of(Post.class).remove(post);
	}
}






