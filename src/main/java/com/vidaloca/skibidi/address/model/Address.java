package com.vidaloca.skibidi.address.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vidaloca.skibidi.event.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
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
    private Long id;

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


    public static final class AddressBuilder {
        private Long id;
        private List<Event> events;
        private String country;
        private String city;
        private String postcode;
        private String street;
        private String number;

        private AddressBuilder() {
        }

        public static AddressBuilder anAddress() {
            return new AddressBuilder();
        }

        public AddressBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AddressBuilder withEvents(List<Event> events) {
            this.events = events;
            return this;
        }

        public AddressBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withPostcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public AddressBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder withNumber(String number) {
            this.number = number;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.setId(id);
            address.setEvents(events);
            address.setCountry(country);
            address.setCity(city);
            address.setPostcode(postcode);
            address.setStreet(street);
            address.setNumber(number);
            return address;
        }
    }
}
