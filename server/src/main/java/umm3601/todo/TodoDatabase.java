package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.CheckReturnValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.BadRequestResponse;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {

    InputStream resourceAsStream = getClass().getResourceAsStream(todoDataFile);
    if (resourceAsStream == null) {
      throw new IOException("Could not find " + todoDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourceAsStream);
    ObjectMapper objectMapper = new ObjectMapper();
    allTodos = objectMapper.readValue(reader, Todo[].class);
  }

  public int size(){
    return allTodos.length;
  }
/**
  @param id
  @return
*/
public Todo getTodo(String id) {
  return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
}
/**
  @param queryParams
  @return
*/
  public Todo[] listTodos(Map<String,List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

    // Filter owner is defined
    if (queryParams.containsKey("owner")) {
      String targetOwner = queryParams.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, targetOwner);
    }
    // Filter status is defined
    if (queryParams.containsKey("status")) {
      String statusParam = queryParams.get("status").get(0);
      // try {
      Boolean targetStatus = Boolean.parseBoolean(statusParam);
      filteredTodos = filterTodosByStatus(filteredTodos, targetStatus);
      // } catch ()
    }

    // Filter body is defined
    if (queryParams.containsKey("body")) {
      String targetBody = queryParams.get("body").get(0);
      filteredTodos = filterTodosByBody(filteredTodos, targetBody);
    }
    // Filter category is defined
    if (queryParams.containsKey("category")) {
      String targetCategory = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos,targetCategory);
    }

    return filteredTodos;
  }
  /**
   * Get an array of all the Todos having the target Owner
    @param todos          the list of todos to filter by status
    @param targetOwner    the target Owner to look for
    @return               an array of all the todos from the given list that have the target status
  */

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  /** Get an array of all the todos having the target Status.
    @param todos          the list of todos to filter by status
    @param targetStatus   the target status to look for
    @return               an array of all the todos from the given list that have the target status
  */

  public Todo [] filterTodosByStatus(Todo[] todos, Boolean targetStatus){
    return Arrays.stream(todos).filter(x -> x.status.equals(targetStatus)).toArray(Todo[]::new);
  }

  /**
   * Get an array of all the Todos with target in Body.
    @param todos          the list of todos to filter by body
    @param targetBody     the target status to look for
    @return               an array of all the todos from the given list that have the target body
  */

  public Todo[] filterTodosByBody(Todo[] todos, String targetBody) {
      return Arrays.stream(todos).filter(x -> x.body.equals(targetBody)).toArray(Todo[]::new);
    }

  /**
   * Get an array of all the Todos with the target Category
    @param todos          the list of todos to filter by status
    @param targetCategory the target category to look for
    @return               an array of all the todos from the given list that have the target status
  */

  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

  }
