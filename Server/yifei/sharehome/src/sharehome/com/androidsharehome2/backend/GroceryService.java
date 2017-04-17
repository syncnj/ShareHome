package sharehome.com.androidsharehome2.backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.servercode.IBackendlessService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroceryService implements IBackendlessService{
	public String createNewGrocery(String groupId, String groceryName, int status){
		if(groupId == null || groceryName == null || status == 0 ||
				groupId.trim() == "" || groceryName.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		// Find so that if error then return

		//Now check for duplicate:
		String whereClause = "groupId = '" + groupId.trim() + "'";
		//if (true)throw new RuntimeException(""+ whereClause);
		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		dataQuery.setWhereClause( whereClause );
		BackendlessCollection<Map> result = Backendless.Persistence.of( "Grocery" ).find( dataQuery);

		if ( !result.getCurrentPage().isEmpty()){
			throw new RuntimeException("Same Grocery already exist" + result.getCurrentPage());

		}
		
		Grocery grocery = new Grocery();
		grocery.setGroceryName(groceryName);
		grocery.setGroupId(groupId);
		grocery.setStatus(status);
		
		Backendless.Persistence.save(grocery);
		
		
		group.addGrocery(grocery.getObjectId());
		Backendless.Persistence.save(group);
		
		return grocery.getObjectId();
		
	}
	
	public boolean deleteGrocery(String groceryId, String groupId) throws Exception{
		if(groceryId == null || groupId == null || groceryId.trim() == "" || groupId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		
		Grocery grocery = Backendless.Persistence.of(Grocery.class).findById(groceryId);
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		String groceryList = group.getGroceryIdList();
		int indexOfFirstChar = groceryList.indexOf(groceryId);
		
		if (indexOfFirstChar + groceryId.length() + 1 == groceryList.length()) {
			// member is last user in group
			if ( indexOfFirstChar != 0 && groceryList.charAt(indexOfFirstChar - 1) != ',' ){
				throw new Exception("Grocery can't be found");
			}

			group.setGroceryIdList(groceryList.substring(0, indexOfFirstChar));
			Backendless.Persistence.save(group);
			
			Backendless.Persistence.of(Grocery.class).remove(grocery);
			
			return true;
		} else {

			if ( groceryList.charAt(indexOfFirstChar - 1) != ',' || groceryList.charAt(groceryList.lastIndexOf(groceryId) + 1) != ','){
				throw new Exception("Grocery can't be found");
			}

			String beforeDeletedGrocery = group.getGroceryIdList().substring(0, indexOfFirstChar);
			String afterDeletedGrocery = group.getGroceryIdList().substring((indexOfFirstChar + groceryId.length() + 1), group.getGroceryIdList().length());
			group.setGroceryIdList(beforeDeletedGrocery + afterDeletedGrocery);
			Backendless.Persistence.save(group);

			Backendless.Persistence.of(Grocery.class).remove(grocery);
			
			return true;
		}
		
	}
	public Grocery getGroceryById(String groceryId){
		return Backendless.Persistence.of(Grocery.class).findById(groceryId);
	}
	
	public int changeStatus(String groceryId){
		Grocery grocery = Backendless.Persistence.of(Grocery.class).findById(groceryId);
		grocery.changeStatus();
		Backendless.Persistence.save(grocery);
		return grocery.getStatus();
	}
	
}
