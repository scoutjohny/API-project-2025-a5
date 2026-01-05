package tests;

import config.Config;
import endpoints.UserEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import listeners.TestListeners;
import model.UserRequest;
import model.UserResponse;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.Constants;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Listeners(TestListeners.class)
public class UserTests extends Config implements UserEndpoints {

    @Test
    public void test1(){
//        Gherkin sintaksa (Cucumber):
//        Given - preduslov za izvršavanje testa
//        When - sam test
//        Then - provera u testu
//        And - menja bilo koji Given, When ili Then
//        Given The User is on the Sauce Demo Login page
//        When The User types in "username" into the username field
//        And The User types in "password" into the password field
//        And The User clicks on the Login button
//        Then The User is on the product page
    }

    @Test
    public void getListTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 2);
        map.put("limit", 5);

        Response response = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("data[0].firstName");

        Assert.assertEquals(actualFirstName, "Valentin");
    }

    @Test
    public void getListUsingJsonPathTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 2);
        map.put("limit", 5);

        JsonPath jsonPath = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS).getBody().jsonPath();

        String actualFirstName = jsonPath.getString("data[0].firstName");
        boolean result = actualFirstName.equals("Valentin");
        Assert.assertTrue(result, "Expected first name is incorrect!");
    }

    @Test
    public void getUserByIDTest(){

        Response response = given()
                .pathParam("id","60d0fe4f5311236168a109d4")
                .when().get(Constants.GET_USER_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("firstName");

        Assert.assertEquals(actualFirstName, "Valentin");
    }

    @Test
    public void getUserByIDWithGetListAPICallTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 2);
        map.put("limit", 5);

        Response response = given()
                .queryParams(map)
                .when().get(GET_ALL_USERS);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String userId = "";
        for(int i = 0; i<5; i++){
            if (response.jsonPath().get("data["+i+"].firstName").equals("Valentin")){
                userId = response.jsonPath().get("data["+i+"].id");
                System.out.println("User ID: " + userId);
            }
        }

        Response response1 = given()
                .pathParam("id",userId)
                .when().get(Constants.GET_USER_BY_ID);

        Assert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response1.jsonPath().get("firstName");

        Assert.assertEquals(actualFirstName, "Valentin");
    }

    @Test
    public void deleteUserByIDTest(){
        String userID = "60d0fe4f5311236168a109d4";
        Response response = given()
                .pathParam("id",userID)
                .when().delete(Constants.DELETE_USER_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String id = response.jsonPath().get("id");

        Assert.assertEquals(id, userID);

        Response response1 = given()
                .pathParam("id",userID)
                .when().delete(Constants.DELETE_USER_BY_ID);
        Assert.assertEquals(response1.getStatusCode(), 404, "Expected 404 but got: " + response.getStatusCode());
    }

    @Test
    public void createUserTest(){

        Response response = given()
                .body("{\n" +
                        "    \"title\": \"miss\",\n" +
                        "    \"firstName\": \"Naomi\",\n" +
                        "    \"lastName\": \"Rodrigues\",\n" +
                        "    \"picture\": \"https://randomuser.me/api/portraits/med/women/25.jpg\",\n" +
                        "    \"gender\": \"female\",\n" +
                        "    \"email\": \"naomi.rodrigues9874984514@example.com\",\n" +
                        "    \"dateOfBirth\": \"1954-10-15T02:26:17.794Z\",\n" +
                        "    \"phone\": \"993-465-335\",\n" +
                        "    \"location\": {\n" +
                        "        \"street\": \"2580, Calle de Ferraz\",\n" +
                        "        \"city\": \"Albacete\",\n" +
                        "        \"state\": \"Islas Baleares\",\n" +
                        "        \"country\": \"Spain\",\n" +
                        "        \"timezone\": \"-3:30\"\n" +
                        "    }\n" +
                        "}")
                .when().post(CREATE_USER);

        Assert.assertEquals(response.getStatusCode(), 200);
        String id = response.jsonPath().get("id");
        String firstName = response.jsonPath().get("firstName");

        Response response1 = given()
                .pathParam("id",id)
                .when().get(Constants.GET_USER_BY_ID);

        Assert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response1.jsonPath().get("firstName");

        Assert.assertEquals(actualFirstName, firstName);
    }

    @Test
    public void createUserUsingJavaObjectTest(){
        UserRequest user = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(user)
                .when().post(Constants.CREATE_USER)
                .getBody().as(UserResponse.class);

        String userId = userResponse.getId();
        Assert.assertEquals(userResponse.getFirstName(),user.getFirst_Name());

        Utils.createJsonFile("userData", userResponse);
    }


}
