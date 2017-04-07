package sharehome.com.androidsharehome2;

import java.util.ArrayList;

/**
 * Created by aaron on 4/6/17.
 */

public class Group {
    public String name;
    private String UniqueID;
    private ArrayList<User> users;
    private User groupLeader;

    public Group(String name, User groupLeader){
        this.name = name;
        UniqueID = "Hello, World!";
    }

    public String getGroupID(){
        return UniqueID;
    }

    public User getGroupLeader(){
        return groupLeader;
    }


}
