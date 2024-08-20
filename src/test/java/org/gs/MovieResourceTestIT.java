package org.gs;

import static java.util.Optional.empty;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieResourceTestIT {

  @Test
  @Order(1)
  void getAll() {
    given()
      .when().get("/movies")
      .then()
      .statusCode(200)
      .body(is(not(empty())));
  }

  @Test
  @Order(1)
  void getById() {
    
    given()
      .when().get("/movies/1")
      .then()
      .statusCode(200)
      .body("id", equalTo(1));
  }

  @Test
  @Order(1)
  void getByIdKO() {
    given()
      .when().get("/movies/9999") 
      .then()
      .statusCode(404);
  }

  @Test
  @Order(1)
  void getByTitle() {
    
    given()
      .when().get("/movies/title/Planet")
      .then()
      .statusCode(200)
      .body("title", equalTo("Planet"));
  }

  @Test
  @Order(1)
  void getByTitleKO() {
    given()
      .when().get("/movies/title/tropadeeleite")
      .then()
      .statusCode(404);
  }

  @Test
  @Order(2)
  void getByCountry() {
    
    given()
      .when().get("/movies/country/Brasil")
      .then()
      .statusCode(200)
      .body("[0].country", equalTo("Brasil"));
  }

  @Test
  @Order(2)
  void getByCountryKO() {
    given()
      .when().get("/movies/country/")
      .then()
      .statusCode(200)
      .body(equalTo("[]"));
  }

  @Test
  @Order(3)
  void create() {
    JsonObject movie = Json.createObjectBuilder()
      .add("title", "Ó pai Ó")
      .add("description", "COMÉDIA")
      .add("director", "BAHIA")
      .add("country", "Brasil")
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(movie.toString())
      .when().post("/movies")
      .then()
      .statusCode(201)
      .header("Location", containsString("/movies/"));
  }

  @Test
  @Order(4)
  void updateById() {
    JsonObject updatedMovie = Json.createObjectBuilder()
      .add("title", "Ó PAI Ó 2")
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(updatedMovie.toString())
      .when().put("/movies/1")
      .then()
      .statusCode(200)
      .body("title", equalTo("Ó PAI Ó 2"));
  }

  @Test
  @Order(4)
  void updateByIdKO() {
    JsonObject updatedMovie = Json.createObjectBuilder()
      .add("title", "Ó PAI Ó 2")
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(updatedMovie.toString())
      .when().put("/movies/9999")  
      .then()
      .statusCode(404);
  }

  @Test
  @Order(5)
  void deleteById() {
    given()
      .when().delete("/movies/1")
      .then()
      .statusCode(204);
  }

  @Test
  @Order(5)
  void deleteByIdKO() {
    given()
      .when().delete("/movies/9999") 
      .then()
      .statusCode(404);
  }
}