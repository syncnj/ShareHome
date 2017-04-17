package sharehome.com.androidsharehome2.backend;

import java.util.List;

/**
 * Created by yblur on 4/14/2017.
 */
public class Transaction {
    private String transTitle;
    private String content;
    private String requestor;
    private String otherPeople; // Other people
    private String objectId;
    private String ownerId;
    private double transAmount;

    public String getObjectId(){
    	return this.objectId;
    }
    public String getOwnerId(){
    	return this.ownerId;
    }
    public String getTransTitle(){
    	return this.transTitle;
    }
    public String getContent(){
    	return this.content;
    }
    public String getRequestor(){
    	return this.requestor;
    }
    public String getOtherPeople(){
    	return this.otherPeople;
    }
    public double getTransAmount(){
    	return this.transAmount;
    }
    public void setTransTitle(String transTitle){
    	this.transTitle = transTitle;
    }
    public void setContent(String content){
    	this.content = content;
    }
    public void setRequestor(String requestor){
    	this.requestor = requestor;
    }
    public void setOtherPeople(String otherPeople){
    	this.otherPeople = otherPeople;
    }
    public void setTransAmount(double transAmount){
    	this.transAmount = transAmount;
    }
}
