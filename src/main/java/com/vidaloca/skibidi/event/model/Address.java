package com.vidaloca.skibidi.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int address_id;

    @JsonBackReference
    @OneToMany(mappedBy = "address", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Event> events;

    @NotNull(message = "Country is obligatory")
    @Length(min=3,max=50, message = "Length of country name has to be between 3 and 50")
    private String country;
    @NotNull(message = "City is obligatory")
    @Length(min=3,max=50, message = "Length of city name has to be between 3 and 50")
    private String city;
    @Length(max = 10, message = "Length of postcode has to be less than 11")
    private String postcode;
    private String street;
    @NotNull(message = "Number is obligatory")
    @NotEmpty(message = "Number cannot be empty")
    private String number;
}
