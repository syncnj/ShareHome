package sharehome.com.androidsharehome2;

import android.content.Context;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * Created by Zirui Tao on 12/11/2017.
 */

public class SlidableExpandableListView extends ExpandableListView {
    Context context;
    SlidableExpandableListView(Context context){
        super(context);
    }
    @Override
    public boolean performClick() {
        // do what you want
        Toast.makeText(this.context, "In Slidable PerformClick!", Toast.LENGTH_LONG);
        return true;
    }
}
