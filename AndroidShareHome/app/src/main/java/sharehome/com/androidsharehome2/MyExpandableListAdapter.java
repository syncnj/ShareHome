package sharehome.com.androidsharehome2;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Jeremy on 12/10/17.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter
{
    Context context;
    List<String> title;
    Map<String, List<String>> content;

    public MyExpandableListAdapter(Context context, List<String> title, Map<String, List<String>> content) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent,null);
            Button delete = (Button)convertView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add delete method
                    Toast.makeText(getApplicationContext(), "Delete works",
                            Toast.LENGTH_LONG).show();
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
            convertView = inflater.inflate(R.layout.list_child,null);
        }
        TextView txtChild = (TextView) convertView.findViewById(R.id.content);
        txtChild.setText(content);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
