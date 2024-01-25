package umm3601.user;

import java.io.IOException;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import umm3601.Controller;
import umm3601.user.UserController;

public class TodoController implements Controller {

    private TodoDatabase todoDatabase;

    /**
    * @param database
    */
public TodoController(TodoDatabase todoDatabase) {
    this.todoDatabase = todoDatabase;
}


/**
 * @throws IOException
*/
public static TodoController buildTodoController(String todoDataFile) throws IOException {
  TodoController todoController = null;

  TodoDatabase todoDatabase = new TodoDatabase(todoDataFile);
  todoController = new TodoController(todoDatabase);
}




/**
 *Get the single todos specified by the `id` parameter in the request.
 *
 * @param ctx a Javalin HTTP context
 */
public void getUser
