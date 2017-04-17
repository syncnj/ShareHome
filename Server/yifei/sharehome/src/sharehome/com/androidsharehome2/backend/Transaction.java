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

    private double transAmount;

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
    
    
    
    
}
