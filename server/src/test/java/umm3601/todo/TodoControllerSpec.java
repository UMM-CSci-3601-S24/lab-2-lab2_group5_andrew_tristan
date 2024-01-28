package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import io.javalin.http.BadRequestResponse;
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
  public void buildControllerFailsWithIllegalDbFile(){
    Assertions.assertThrows(IOException.class, ()-> {
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



  /**
   * @throws IOException
   */
  @Test
  public void canGetAllTodosWithStatusTrue(){
    //
  }


}
