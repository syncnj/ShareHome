package sharehome.com.androidsharehome2.backend;

import com.backendless.*;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.servercode.IBackendlessService;

import java.util.Map;

/**
 * Created by yblur on 4/14/2017.
 */
public class TransactionService implements IBackendlessService {
    public String createTransaction (String requestorId, String otherPeople, String groupId, double amount,
                                     String title, String content){
    	if(groupId == null || otherPeople== null || title== null|| content == null || requestorId == null 
    			|| amount == 0.0 ||groupId.trim() == "" || otherPeople.trim() == ""|| title.trim() ==""
    			|| content.trim()=="" || requestorId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
    	
    	BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		String whereClause = "objectId = '" + requestorId.trim() + "'";
		dataQuery.setWhereClause( whereClause );

		BackendlessCollection<BackendlessUser> userResult = Backendless.Data.of( BackendlessUser.class).find(dataQuery);

		if (userResult.getCurrentPage().isEmpty()){
			throw new RuntimeException(" ID is invalid or could not be found in the database");
		}
    	
		
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		
		whereClause = "groupId = '" + groupId.trim() + "' AND transactionTitle = '" + title.trim() + "'";
		//if (true)throw new RuntimeException(""+ whereClause);
		dataQuery.setWhereClause( whereClause );
		BackendlessCollection<Map> result = Backendless.Persistence.of( "Transaction" ).find( dataQuery);

		if ( !result.getCurrentPage().isEmpty()){
			throw new RuntimeException("Same Transaction already exist" + result.getCurrentPage());

		}
    	
		
		Transaction transaction = new Transaction();
		transaction.setTransTitle(title);
		transaction.setContent(content);
		transaction.setOtherPeople(otherPeople);
		transaction.setRequestor(requestorId);
		Backendless.Persistence.save(transaction);
		
		group.addTransaction(transaction.getObjectId());
		Backendless.Persistence.save(group);
		return transaction.getObjectId();
		
    }
}
