package endpoints;

public interface UserEndpoints {

    String GET_ALL_USERS = "user";
    String GET_USER_BY_ID = "user/{id}";
    String DELETE_USER_BY_ID = "user/{id}";
    String CREATE_USER = "user/create";
}
