
  /*******************************************************************
  * PostService.java
  * Generated by Backendless Corp.
  ********************************************************************/
		
package sharehome.com.androidsharehome2.backend;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import java.util.*;

import sharehome.com.androidsharehome2.backend.Post;

public class PostService
{
    static final String BACKENDLESS_HOST = "https://api.backendless.com";
    static final String SERVICE_NAME = "PostService";
    static final String SERVICE_VERSION_NAME = "1.0.0";
    static final String APP_VERSION = "v1";
    static final String APP_ID = "19E73C32-D357-313A-FF64-12612084E000";
    static final String SECRET_KEY = "19F8B6BD-34A1-655C-FF3E-9BD31F1B0C00";

    private static PostService ourInstance = new PostService();

    private PostService(  )
    {
    }

    public static PostService getInstance()
    {
        return ourInstance;
    }

    public static void initApplication()
    {
        Backendless.setUrl( PostService.BACKENDLESS_HOST );
        // if you invoke this sample inside of android application, you should use overloaded "initApp" with "context" argument
        Backendless.initApp( PostService.APP_ID, PostService.SECRET_KEY, PostService.APP_VERSION );
    }


    
    public int addUpVote(java.lang.String postId, java.lang.String userId)
    {
        Object[] args = new Object[]{postId, userId};
        return Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "addUpVote", args, int.class );
    }
    
    public void addUpVoteAsync(java.lang.String postId, java.lang.String userId, AsyncCallback<Integer> callback)
    {
        Object[] args = new Object[]{postId, userId};
        Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "addUpVote", args, Integer.class, callback);
    }
    
    public int addDownVote(java.lang.String postId, java.lang.String userId)
    {
        Object[] args = new Object[]{postId, userId};
        return Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "addDownVote", args, int.class );
    }
    
    public void addDownVoteAsync(java.lang.String postId, java.lang.String userId, AsyncCallback<Integer> callback)
    {
        Object[] args = new Object[]{postId, userId};
        Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "addDownVote", args, Integer.class, callback);
    }
    
    public java.lang.String createNewPost(java.lang.String userId, java.lang.String groupId, java.lang.String postTitle, java.lang.String postContent)
    {
        Object[] args = new Object[]{userId, groupId, postTitle, postContent};
        return Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "createNewPost", args, java.lang.String.class );
    }
    
    public void createNewPostAsync(java.lang.String userId, java.lang.String groupId, java.lang.String postTitle, java.lang.String postContent, AsyncCallback<java.lang.String> callback)
    {
        Object[] args = new Object[]{userId, groupId, postTitle, postContent};
        Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "createNewPost", args, java.lang.String.class, callback);
    }
    
    public boolean deletePost(java.lang.String postId, java.lang.String userId)
    {
        Object[] args = new Object[]{postId, userId};
        return Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "deletePost", args, boolean.class );
    }
    
    public void deletePostAsync(java.lang.String postId, java.lang.String userId, AsyncCallback<Boolean> callback)
    {
        Object[] args = new Object[]{postId, userId};
        Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "deletePost", args, Boolean.class, callback);
    }
    
    public sharehome.com.androidsharehome2.backend.Post getPostById(java.lang.String postId)
    {
        Object[] args = new Object[]{postId};
        return Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "getPostById", args, sharehome.com.androidsharehome2.backend.Post.class );
    }
    
    public void getPostByIdAsync(java.lang.String postId, AsyncCallback<sharehome.com.androidsharehome2.backend.Post> callback)
    {
        Object[] args = new Object[]{postId};
        Backendless.CustomService.invoke( SERVICE_NAME, SERVICE_VERSION_NAME, "getPostById", args, sharehome.com.androidsharehome2.backend.Post.class, callback);
    }
    
}
