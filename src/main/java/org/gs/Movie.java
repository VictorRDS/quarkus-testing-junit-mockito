package org.gs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 200)
    private String description;

    private String director;

    private String country;

}