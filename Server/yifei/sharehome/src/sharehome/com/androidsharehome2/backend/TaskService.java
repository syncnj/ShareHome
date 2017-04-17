package sharehome.com.androidsharehome2.backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.servercode.IBackendlessService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class TaskService implements IBackendlessService{

	public String
	createNewTask(String taskName, String membersIdList, String groupId, int duration, Date startTime){
		if(groupId == null || taskName == null || duration == 0 ||
				groupId.trim() == "" || taskName.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		Group group =  Backendless.Persistence.of(Group.class).findById(groupId);
		
		String whereClause = "groupId = '" + groupId.trim() + "'";
		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		dataQuery.setWhereClause( whereClause );
		BackendlessCollection<Map> result = Backendless.Persistence.of( "Task" ).find( dataQuery);

		if ( !result.getCurrentPage().isEmpty()){
			throw new RuntimeException("Same Task already exist" + result.getCurrentPage());

		}
		
		
	    Task task = new Task();
		task.setTaskName(taskName);
		task.setDuration(duration);
		task.setMembersIdList(membersIdList+ ",");
		task.setGroupId(groupId);
		task.setStartTime(startTime);
		Backendless.Persistence.save(task);
		
		
		group.addTask(task.getObjectId());
		Backendless.Persistence.save(group);
		
		return task.getObjectId();
	}
	
	public boolean deleteTask(String  taskId, String groupId) throws Exception{
		if(taskId == null || groupId == null || taskId.trim() == "" || groupId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		
		Task task = Backendless.Persistence.of(Task.class).findById(taskId);
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		String taskList = group.getTaskIdList();
		int indexOfFirstChar = taskList.indexOf(taskId);
		
		if (indexOfFirstChar + taskId.length() + 1 == taskList.length()) {
			// member is last user in group
			if ( indexOfFirstChar != 0 && taskList.charAt(indexOfFirstChar - 1) != ',' ){
				throw new Exception("Task can't be found");
			}

			group.setTaskIdList(taskList.substring(0, indexOfFirstChar));
			Backendless.Persistence.save(group);
			
			Backendless.Persistence.of(Task.class).remove(task);
			
			return true;
		} else {

			if ( taskList.charAt(indexOfFirstChar - 1) != ',' || taskList.charAt(taskList.lastIndexOf(taskId) + 1) != ','){
				throw new Exception("Task can't be found");
			}

			String beforeDeletedTask = group.getTaskIdList().substring(0, indexOfFirstChar);
			String afterDeletedTask = group.getTaskIdList().substring((indexOfFirstChar + taskId.length() + 1), group.getTaskIdList().length());
			group.setTeamMembersList(beforeDeletedTask + afterDeletedTask);
			Backendless.Persistence.save(group);

			Backendless.Persistence.of(Task.class).remove(task);
			
			return true;
		}
		
		
		
		
	}
	public Task getTaskById(String taskId){
		return Backendless.Persistence.of(Task.class).findById(taskId);
	}
	
	public void rotate(String taskId){
		Task task = getTaskById(taskId);
		int indexOfFirstMember = task.getMembersIdList().indexOf(",") + 1;
		String movedMember =  task.getMembersIdList().substring(0,indexOfFirstMember);
		task.setMembersIdList(task.getMembersIdList().substring(indexOfFirstMember) + movedMember);
		Backendless.Persistence.save(task);
	}
	
}
