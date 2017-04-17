package sharehome.com.androidsharehome2.backend;

import com.backendless.Backendless;

/**
 * Created by yblur on 4/14/2017.
 */
public class Grocery {
	private String groceryName;
	private int status;
	private String groupId;
	private String objectId;
	private String ownerId;
	
	public String getGroceryName(){
		return this.groceryName;
	}
	public int getStatus(){
		return this.status;
	}
	public String getGroupId(){
		return this.groupId;
	}
	public String getObjectId(){
		return this.objectId;
	}
	public String getOwnerId(){
		return this.ownerId;
	}
	
	public void setGroceryName(String groceryName){
		this.groceryName = groceryName;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public void changeStatus(){
		if(status == 0){
			status = 1;
		}
		else if(status == 1){
			status = 3;
		}
		else if(status == 2){
			status = 1;
		}
		else if(status == 3){
			status = 2;
		}
		Backendless.Persistence.save(this);
	}
}

