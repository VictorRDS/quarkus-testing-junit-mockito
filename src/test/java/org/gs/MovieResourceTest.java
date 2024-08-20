package org.gs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
class MovieResourceTest {

  @InjectMock
  MovieRepository movieRepository;

  @Inject
  MovieResource movieResource;

  private Movie movie;

  @Test
  @BeforeEach
  void setUp() {
    movie = new Movie();
    movie.setId(3L);
    movie.setTitle("Ó PAI Ó 2");
    movie.setDescription("BAHIA");
    movie.setDirector("nao sei");
    movie.setCountry("Brasil");
  }

  @Test
  void getAll() {
  
    when(movieRepository.listAll()).thenReturn(Collections.singletonList(movie));

    Response response = movieResource.getAll();

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(Collections.singletonList(movie), response.getEntity());
  }

  @Test
  void getByIdOK() {
  
    Long id = 1L;
    when(movieRepository.findByIdOptional(id)).thenReturn(Optional.of(movie));


    Response response = movieResource.getById(id);

  
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(movie, response.getEntity());
  }

  @Test
  void getByIdKO() {
  
    Long id = 999L;
    when(movieRepository.findByIdOptional(id)).thenReturn(Optional.empty());

   
    Response response = movieResource.getById(id);

  
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void getByTitleOK() {

    String title = "Ó PAI Ó";
    when(movieRepository.find("title", title)).thenReturn((PanacheQuery<Movie>) new MockPanacheQuery<>(movie));

    Response response = movieResource.getByTitle(title);


    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(movie, response.getEntity());
  }

  @Test
  void getByTitleKO() {
  
    String title = "TROPA DE ELITE";
 
    when(movieRepository.find("title", title)).thenReturn(new MockPanacheQuery<>());

    Response response = movieResource.getByTitle(title);

  
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void getByCountry() {
  
    String country = "Brasil";
    when(movieRepository.findByCountry(country)).thenReturn(Collections.singletonList(movie));

    Response response = movieResource.getByCountry(country);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(Collections.singletonList(movie), response.getEntity());
  }

  @Test
  void createOK() {
    Movie newMovie = new Movie();
    newMovie.setTitle("Ó PAI Ó 3");
    when(movieRepository.isPersistent(newMovie)).thenReturn(true);
    doNothing().when(movieRepository).persist(newMovie);

    Response response = movieResource.create(newMovie);

  
    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    assertTrue(response.getLocation().toString().contains("/movies/"));
  }

  @Test
  void createKO() {

    Movie newMovie = new Movie();
    newMovie.setTitle("Ó PAI Ó");
    when(movieRepository.isPersistent(newMovie)).thenReturn(false);

    Response response = movieResource.create(newMovie);


    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  void updateByIdOK() {

    Long id = 1L;
    Movie updatedMovie = new Movie();
    updatedMovie.setId(id);
    when(movieRepository.findByIdOptional(id)).thenReturn(Optional.of(movie));


    Response response = movieResource.updateById(id, updatedMovie);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(updatedMovie.getTitle(), ((Movie) response.getEntity()).getTitle());
  }

  @Test
  void updateByIdKO() {
  
    Long id = 999L;
    Movie updatedMovie = new Movie();
    updatedMovie.setId(id);
    when(movieRepository.findByIdOptional(id)).thenReturn(Optional.empty());

    Response response = movieResource.updateById(id, updatedMovie);


    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void deleteByIdOK() {
    Long id = 1L;
    when(movieRepository.deleteById(id)).thenReturn(true);

    
    Response response = movieResource.deleteById(id);

   
    assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
  }

  @Test
  void deleteByIdKO() {
    
    Long id = 999L;
    when(movieRepository.deleteById(id)).thenReturn(false);

    
    Response response = movieResource.deleteById(id);

    
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

 
  private static class MockPanacheQuery<T> implements PanacheQuery<Movie> {
    private final List<T> results;

    MockPanacheQuery(T... results) {
      this.results = List.of(results);
    }

    
    public List<T> list() {
      return results;
    }

    
    public Optional<T> singleResultOptional() {
      return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

        @Override
        public <T> PanacheQuery<T> project(Class<T> type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> page(Page page) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> page(int pageIndex, int pageSize) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> nextPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> previousPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> firstPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> lastPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean hasNextPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean hasPreviousPage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int pageCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Page page() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> range(int startIndex, int lastIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> withLock(LockModeType lockModeType) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> withHint(String hintName, Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> filter(String filterName, Parameters parameters) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> filter(String filterName, Map<String, Object> parameters) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> PanacheQuery<T> filter(String filterName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long count() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> Stream<T> stream() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> T firstResult() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> Optional<T> firstResultOptional() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Movie> T singleResult() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        

  }
}