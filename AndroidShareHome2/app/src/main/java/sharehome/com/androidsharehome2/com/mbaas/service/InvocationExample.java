
package sharehome.com.androidsharehome2.com.mbaas.service;

import sharehome.com.androidsharehome2.com.mbaas.service.ShoppingCartService;

public class InvocationExample
{
    public static void main( String[] args )
    {
        ShoppingCartService.initApplication();

        ShoppingCartService shoppingCartService = ShoppingCartService.getInstance();
        // invoke methods of you service
        //Object result = shoppingCartService.yourMethod();
        //System.out.println( result );
    }
}
