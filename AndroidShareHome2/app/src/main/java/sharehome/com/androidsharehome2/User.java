package sharehome.com.androidsharehome2;

import java.util.ArrayList;

/**
 * Created by aaron on 4/6/17.
 */

public class User {
    // Authentication through Amazon so don't need username and password
    String username;
    String phoneNumber;
    String UserID;
    private ArrayList<Group> groups;

    public User(String name, String phoneNumber){
        username = name;
        this.phoneNumber = phoneNumber;

    }
    /*
    This function creates a group and adds it to the users groups
     */
    public Group createNewGroup(String groupName){
        Group group =  new Group(groupName, this);
        groups.add(group);
        return group;
    }

    public Group searchForGroup(String groupName){
        // TODO: communicate with server
        return null;
    }

    public void removeFromGroup(String groupID){
        // TODO: remove user from group on server
        // Server needs groupID and userID

        // Remove User from group in user's list
        // Remove group from user's list
    }

    public void addToGroup(){
        
    }
}
