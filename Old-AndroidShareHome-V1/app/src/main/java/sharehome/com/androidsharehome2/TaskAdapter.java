package sharehome.com.androidsharehome2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import sharehome.com.androidsharehome2.backend.Task;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by zhigong on 5/3/2017.
 */

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, List<Task> tasks){
        super(context, 0, tasks);
    }
    public View getView(int position, final View convertView, ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }
        final Task currentTask = getItem(position);

        // Task Name
        TextView taskName = (TextView) listItemView.findViewById(R.id.TaskName);
        taskName.setText(currentTask.getTaskName());

        // User's turn
        TextView userInCharge = (TextView) listItemView.findViewById(R.id.UserInCharge);
        String next = "\nNext: ";
        if(currentTask.getNextUserNameOnDuty() != null && !currentTask.getNextUserNameOnDuty().equals("")) {
            next += currentTask.getNextUserNameOnDuty();
        } else {
            next = "";
        }
        userInCharge.setText(currentTask.getUserOnDuty() + next);

        // Days left
        TextView daysLeft = (TextView) listItemView.findViewById(R.id.DaysLeft);
        if(currentTask.getStartTime().compareTo(new Date(0L)) == 0){
            daysLeft.setText("Time Left");
        }
        else{
            Date timeTaskDue = currentTask.getStartTime();
            Date current = new Date(System.currentTimeMillis());
            // TODO: Ensure correct time left is displaying
            Long secondsLeft = (timeTaskDue.getTime() - current.getTime()) / 1000;
            Long days = secondsLeft / (60 * 60 * 24);
            Long hours = (secondsLeft - 24 * 60 * 60 * (days)) / 3600;
            daysLeft.setText(days + " days and " + hours + " hours left");
        }
        return listItemView;
    }
}
