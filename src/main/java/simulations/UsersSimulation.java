package simulations;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class UsersSimulation extends Simulation {

    //Header-i i URL setup

    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("https://dummyapi.io")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .header("app-id","6818f1db630bd90adcce8c16");

    private static ChainBuilder createNewUser =
            exec(http("Create New User")
                    .post("/data/v1/user/create")
                    .body(StringBody(
                            "{\n" +
                                    "    \"title\": \"miss\",\n" +
                                    "    \"firstName\": \"Naomi\",\n" +
                                    "    \"lastName\": \"Rodrigues\",\n" +
                                    "    \"picture\": \"https://randomuser.me/api/portraits/med/women/25.jpg\",\n" +
                                    "    \"gender\": \"female\",\n" +
                                    "    \"email\": \"naomi.rodrigues98749845146958@example.com\",\n" +
                                    "    \"dateOfBirth\": \"1954-10-15T02:26:17.794Z\",\n" +
                                    "    \"phone\": \"993-465-335\",\n" +
                                    "    \"location\": {\n" +
                                    "        \"street\": \"2580, Calle de Ferraz\",\n" +
                                    "        \"city\": \"Albacete\",\n" +
                                    "        \"state\": \"Islas Baleares\",\n" +
                                    "        \"country\": \"Spain\",\n" +
                                    "        \"timezone\": \"-3:30\"\n" +
                                    "    }\n" +
                                    "}"

                    )));

    private static ChainBuilder getAllUsers =
            exec(http("Get ALl Users")
                    .get("/data/v1/user"));

    //Definisanje scenarija

    private ScenarioBuilder scn = scenario("Users - full simulation")
            .forever().on(
                    exec(getAllUsers)
                            .pause(2)
                            .exec(createNewUser)
            );

    //Učitavanje simulacije
    {
        setUp(
                scn.injectOpen(
                        nothingFor(3),
                        rampUsers(5).during(10),
                        constantUsersPerSec(10).during(10)
                ).protocols(httpProtocolBuilder)
        ).maxDuration(30);
    }
}
