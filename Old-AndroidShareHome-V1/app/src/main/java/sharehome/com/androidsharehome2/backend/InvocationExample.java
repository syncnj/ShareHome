
package sharehome.com.androidsharehome2.backend;

public class InvocationExample
{
    public static void main( String[] args )
    {
        GroupService.initApplication();

        GroupService groupService = GroupService.getInstance();
        // invoke methods of you service
        //Object result = groupService.yourMethod();
        //System.out.println( result );
    }
}
