package sharehome.com.androidsharehome2.backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.servercode.IBackendlessService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TaskService implements IBackendlessService{

	public void createNewTask(String taskName, String membersIdList, String groupId, String duration){
		Task task = new Task();
		task.setTaskName(taskName);
		task.setDuration(duration);
		task.setMembersIdList(membersIdList);
		task.setGroupId(groupId);
		Backendless.Persistence.save(task);
	}
	
	
	
}
