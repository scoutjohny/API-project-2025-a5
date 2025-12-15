package tests;

import endpoints.UserEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserTests implements UserEndpoints {

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
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id","6818f1db630bd90adcce8c16")
                .queryParams(map)
                .log().all()
                .when().get(GET_ALL_USERS);

        response.prettyPeek();

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
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id","6818f1db630bd90adcce8c16")
                .queryParams(map)
                .log().all()
                .when().get(Constants.GET_ALL_USERS).getBody().jsonPath();

        String actualFirstName = jsonPath.getString("data[0].firstName");
        boolean result = actualFirstName.equals("Valentin");
        Assert.assertTrue(result, "Expected first name is incorrect!");
    }

    @Test
    public void getUserByIDTest(){

        Response response = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id","6818f1db630bd90adcce8c16")
                .log().all()
                .pathParam("id","60d0fe4f5311236168a109d4")
                .when().get(Constants.GET_USER_BY_ID);

        response.prettyPeek();

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
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id","6818f1db630bd90adcce8c16")
                .queryParams(map)
                .log().all()
                .when().get(GET_ALL_USERS);

        response.prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String userId = "";
        for(int i = 0; i<5; i++){
            if (response.jsonPath().get("data["+i+"].firstName").equals("Valentin")){
                userId = response.jsonPath().get("data["+i+"].id");
                System.out.println("User ID: " + userId);
            }
        }

        Response response1 = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id","6818f1db630bd90adcce8c16")
                .log().all()
                .pathParam("id",userId)
                .when().get(Constants.GET_USER_BY_ID);

        response1.prettyPeek();

        Assert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response1.jsonPath().get("firstName");

        Assert.assertEquals(actualFirstName, "Valentin");
    }
}
