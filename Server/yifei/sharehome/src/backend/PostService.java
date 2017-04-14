package backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.servercode.IBackendlessService;

import java.util.Map;

public class PostService implements IBackendlessService{
	public void  createNewPost(String userId, String groupId,String postName,String description){
		if(groupId == null || userId== null || postName== null|| description == null ||
				groupId.trim() == "" || userId.trim() == ""|| postName.trim() ==""|| description.trim()==""){
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

		//create a new post if group and user exist
		Post post = new Post();
		post.setPostName(postName);
		post.setUserId(userId);
		post.setCon(0);
		post.setPro(0);
		post.setDescription(description);
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






