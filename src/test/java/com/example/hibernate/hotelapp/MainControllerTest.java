package com.example.hibernate.hotelapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.port = 8081;
    }

    @Test
    public void shouldReturnOkStatusCode()  {
        given().port(port).when().request("GET", "/Guest/get?id=4").then().statusCode(200);
    }

    @Test
    public void shouldReturnGuestWithRightName() {
        given().port(port).when().request("GET", "/Guest/get?id=4").then().body("name", equalTo("Kona"));
    }

    @Test
    public void shouldReturnStatus405()  {
        given().port(port).when().request("PUT", "/Guest/get?id=-1").then().statusCode(405);
    }

    @Test
    public void shouldReturnStatus404()  {
        given().port(port).when().request("GET", "/Guest/get?id=-1").then().statusCode(404);
    }

    @Test
    public void shouldReturnStatus400WhenDeletingNonExistingObject()  {
        given().port(port).when().request("DELETE", "/Guest/delete?id=a").then().statusCode(400);
    }

    @Test
    public void shouldReturnJSONWhenGettingGuest()  {
        given().port(port).when().request("GET", "/Guest/get?id=2").then().contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnOkStatusCodeWhenGettingRoom()  {
        given().port(port).when().request("GET", "/Room/get?id=1").then().statusCode(200);
    }

    @Test
    public void shouldReturnRoomWithRightNumber() {
        given().port(port).when().request("GET", "/Room/get?id=2").then().body("roomNumber", equalTo(6));
    }

    @Test
    public void shouldReturnStatus400WhenAddingRoomWithToLowPricePerNight()  {
        given().port(port).when().request("POST", "/Room/add?number=12&floor=414&price=3.9&occupied=TRue").then().statusCode(400);
    }

    @Test
    public void shouldReturnStatus404WhenAddringReservationWithNonExistingRoomId()  {
        given().port(port).when().request("PUT", "/Guest/reserve?guestID=2&roomID=10000").then().statusCode(404);
    }
//
//    @Test
//    public void shouldReturnStatus400WhenDeletingNonExistingObject()  {
//        given().port(port).when().request("DELETE", "/Guest/delete?id=a").then().statusCode(400);
//    }

}