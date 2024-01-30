package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import umm3601.Main;

/**
 * @throws IOException
 */
@SuppressWarnings({ "MagicNumber" })
public class TodoControllerSpec {
  private TodoController todoController;
  private static TodoDatabase db;

  @Mock
  private Context ctx;

  @Captor
  private ArgumentCaptor<Todo[]> todoArrayCaptor;

  /**
   * @throws IOException
   */
  @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);

    db = new TodoDatabase(Main.TODO_DATA_FILE);

    todoController = new TodoController(db);
  }

  @Test
  public void canBuildController() throws IOException {
    TodoController controller = TodoController.buildTodoController(Main.TODO_DATA_FILE);
    Javalin mockServer = Mockito.mock(Javalin.class);
    controller.addRoutes(mockServer);
  }

  @Test
  public void buildControllerFailsWithIllegalDbFile() {
    Assertions.assertThrows(IOException.class, () -> {
      TodoController.buildTodoController("this is not a legal file name");
    });
  }

  /**
   * @throws IOException
   */
  @Test
  public void canGetAllTodos() throws IOException {
    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    assertEquals(db.size(), todoArrayCaptor.getValue().length);

  }


  @Test
  public void canGetAllTodosWithStatusTrue() {
    // Add a query param map to the context that maps "status"
    // to "true".
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"true"}));
    // Tell the mock `ctx` object to return our query
    // param map when `queryParamMap()` is called.
    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller with the added
    // query param map to limit the result to just todos with
    // status true.
    todoController.getTodos(ctx);

    // Confirm that all the todos passed to `json` have status true.
    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertTrue(todo.status);
    }
    // Confirm that there are 143 todos with status true
    assertEquals(143, todoArrayCaptor.getValue().length);
  }




  /**
   * Confirm that we get a todo when using a valid user ID.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  @Test
  public void canGetTodoWithSpecifiedId() throws IOException {
    // A specific todo ID known to be in the "database".
    String id = "58895985a22c04e761776d54";
    // Get the user associated with that ID.
    Todo user = db.getTodo(id);

    when(ctx.pathParam("id")).thenReturn(id);

    todoController.getTodo(ctx);

    verify(ctx).json(user);
    verify(ctx).status(HttpStatus.OK);
  }

  /**
   * Confirm that we get a 404 Not Found response when
   * we request a todo ID that doesn't exist.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  @Test
  public void respondsAppropriatelyToRequestForNonexistentId() throws IOException {
    when(ctx.pathParam("id")).thenReturn(null);
    Throwable exception = Assertions.assertThrows(NotFoundResponse.class, () -> {
      todoController.getTodo(ctx);
    });
    assertEquals("No todo with the id " + null + " was found.", exception.getMessage());
  }


  @Test
  public void canGetTodoByOwner() {
  String ownerToFilter = "Fry";
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("owner", Arrays.asList(ownerToFilter));
  when(ctx.queryParamMap()).thenReturn(queryParams);
  todoController.getTodos(ctx);
  verify(ctx).json(todoArrayCaptor.capture());
  for (Todo todo : todoArrayCaptor.getValue()) {
    assertEquals(ownerToFilter, todo.owner);
  }
  assertEquals(61, todoArrayCaptor.getValue().length); }



}
