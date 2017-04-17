package sharehome.com.androidsharehome2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

import sharehome.com.androidsharehome2.backend.Grocery;
import sharehome.com.androidsharehome2.backend.GroceryService;
import sharehome.com.androidsharehome2.backend.Post;

import static sharehome.com.androidsharehome2.R.id.upvotes;

/**
 * Created by zhigong on 4/16/2017.
 */

public class GroceryAdapter extends ArrayAdapter<Grocery> {
    public GroceryAdapter(Context context, List<Grocery> groceries){
        super(context, 0, groceries);
    }
    public View getView(int position, final View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grocery_list_item, parent, false);
        }
        final Grocery currentGrocery = getItem(position);
        // Grocery item
        TextView nameView = (TextView) listItemView.findViewById(R.id.GroceryItem);
        nameView.setText(currentGrocery.getGroceryName());


        // Status button
        final Button buttonView = (Button) listItemView.findViewById(R.id.status_button);
        buttonView.setText(Integer.toString(currentGrocery.getStatus()));
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Put next two lines in Grocery.setStatus()
                GroceryService.getInstance().changeStatusAsync(currentGrocery.getObjectId(), new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        currentGrocery.setStatus(response);
                        buttonView.setText(Integer.toString(currentGrocery.getStatus()));
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }
        });

        return listItemView;
    }
}
