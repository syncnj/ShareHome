package backend;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.Map;

/**
 * Created by yblur on 4/13/2017.
 */
public class Helper {
       protected boolean tableQuery (String tableName,String whereClause ){
           BackendlessDataQuery dataQuery = new BackendlessDataQuery();
           dataQuery.setWhereClause( whereClause );

           BackendlessCollection<Map> result = Backendless.Persistence.of( tableName ).find( dataQuery);
            
       }

       protected boolean validateGroupId (String groupId){

       }


}
