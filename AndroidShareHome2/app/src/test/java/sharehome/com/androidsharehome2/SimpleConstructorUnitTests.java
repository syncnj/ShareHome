package sharehome.com.androidsharehome2;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SimpleConstructorUnitTests {
    @Test
    public void UserConstructorPasses() throws Exception {
        User user = new User("Hello", "World");
        assertTrue(!user.equals(null));
    }
    @Test
    public void PostConstructorPasses() throws Exception {
        Post p = new Post();
        assertTrue(!p.equals(null));
    }
    @Test
    public void TransactionConstructorPasses() throws Exception {
        Transaction t = new Transaction();
        assertTrue(!t.equals(null));
    }
    @Test
    public void GroceryListConstructorPasses() throws Exception {
        GroceryList g = new GroceryList();
        assertTrue(!g.equals(null));
    }
    @Test
    public void TaskConstructorPasses() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("hello", "world"));
        Task t = new Task("Do Dishes", "Week", users);
        assertTrue(!t.equals(null));
    }
}