package sharehome.com.androidsharehome2;

import java.util.ArrayList;


/**
 * Created by aaron on 4/6/17.
 */

public class Task {
    public String name;
    public String timePeriod;
    private ArrayList<User> users;
    User recentlyCompleted;

    public Task(String name, String timePeriod, ArrayList<User> users){
        this.name = name;
        this.timePeriod = timePeriod;
        this.users = users;
    }

    void changeName(String newName){
        name = newName;
    }

    void changeTimePeriod(String newTimePeriod){
        timePeriod = newTimePeriod;
    }

}
