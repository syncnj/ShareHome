package sharehome.com.androidsharehome2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import sharehome.com.androidsharehome2.model.ResultStringResponse;
import sharehome.com.androidsharehome2.model.Task;

import static com.facebook.FacebookSdk.getApplicationContext;
import static sharehome.com.androidsharehome2.UserActivity.client;

/**
 * Created by Jeremy on 12/10/17.
 */

public class TaskAdapter extends BaseExpandableListAdapter
{

    Context context;
    List<String> title;
    Map<String, List<String>> content;
    private static final String TAG = "TaskAdapter: ";
    public TaskAdapter(Context context, List<String> title, Map<String, List<String>> content) {
        this.context = context;
        this.title = title;
        this.content = content;
    }
    @Override
    public int getGroupCount() {
        return title.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return content.get(title.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return title.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return content.get(title.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent_task,null);
            Button delete = (Button)convertView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add delete method
                    Integer TaskID = Integer.parseInt(getChild(groupPosition,0).toString());
                    final Task task = new Task();
                    task.setTaskID(TaskID);
                    task.setTaskSolved(true);
                    if (AppHelper.getCurrgroupName() == null){return;}
                    task.setGroupName(AppHelper.getCurrgroupName());
                    Thread taskSolved = new Thread(new Runnable() {
                        Handler handler = new Handler(getApplicationContext().getMainLooper());
                        @Override
                        public void run() {
                            try{
                                final ResultStringResponse response = client.taskPost(task, AppHelper.getCurrUser(),"add");
                            }
                            catch (Exception e){
                                showDialogMessage("Failed to submit the task", e.getMessage());
//                                Toast.makeText(getApplicationContext(), "Failed to submit the task",
//                                        Toast.LENGTH_LONG).show();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Successfully requested to " +
                                                    "resolve this task to your house member" ,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    taskSolved.start();
                }
            });
        }
        TextView txtParent = (TextView) convertView.findViewById(R.id.title);
        txtParent.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String content = (String) getChild(groupPosition, childPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child_task,null);
        }
        TextView txtChild = (TextView) convertView.findViewById(R.id.content);
        txtChild.setText(content);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private void showDialogMessage(String title, String body) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(body);
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).
                setTextColor(Color.parseColor("#3399ff"));

    }
}
