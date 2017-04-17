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
    
    public boolean deleteTransaction(String transactionId, String groupId) throws Exception{
    	if(transactionId == null || groupId == null || transactionId.trim() == "" || groupId.trim() == ""){
			throw new IllegalArgumentException("Invalid input argument");
		}
		
		Transaction transaction = Backendless.Persistence.of(Transaction.class).findById(transactionId);
		Group group = Backendless.Persistence.of(Group.class).findById(groupId);
		String transactionList = group.getTransactionIdList();
		int indexOfFirstChar = transactionList.indexOf(transactionId);
		
		if (indexOfFirstChar + transactionId.length() + 1 == transactionList.length()) {
			// member is last user in group
			if ( indexOfFirstChar != 0 && transactionList.charAt(indexOfFirstChar - 1) != ',' ){
				throw new Exception("Grocery can't be found");
			}

			group.setTransactionIdList(transactionList.substring(0, indexOfFirstChar));
			Backendless.Persistence.save(group);
			
			Backendless.Persistence.of(Transaction.class).remove(transaction);
			
			return true;
		} else {

			if ( transactionList.charAt(indexOfFirstChar - 1) != ',' || transactionList.charAt(transactionList.lastIndexOf(transactionId) + 1) != ','){
				throw new Exception("Transaction can't be found");
			}

			String beforeDeletedTransaction = group.getTransactionIdList().substring(0, indexOfFirstChar);
			String afterDeletedTransaction = group.getTransactionIdList().substring((indexOfFirstChar + transactionId.length() + 1), group.getTransactionIdList().length());
			group.setTeamMembersList(beforeDeletedTransaction + afterDeletedTransaction);
			Backendless.Persistence.save(group);

			Backendless.Persistence.of(Transaction.class).remove(transaction);
			
			return true;
		}
    	
    	
    	
    	
    }
    
    
    
    
}
