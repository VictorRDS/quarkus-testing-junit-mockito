package org.gs;

import java.util.List;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MovieRepositoryTest {

  @Inject
  MovieRepository movieRepository;

  @Test
  void findByCountryOK() {
    
    List<Movie> movies = movieRepository.findByCountry("Planet");

    
    assertEquals(2, movies.size());
    assertEquals("Planet", movies.get(1).getCountry());
    assertEquals(1L, movies.get(1).getId());
    assertEquals("FirstMovie", movies.get(1).getTitle());
    assertEquals("SecondMovie", movies.get(0).getTitle());
  }

  @Test
  void findByCountryKO() {
   
    String country = "NonExistentCountry";
    
    List<Movie> movies = movieRepository.findByCountry(country);

    assertTrue(movies.isEmpty());
  }
}

