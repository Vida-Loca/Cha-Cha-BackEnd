package com.vidaloca.skibidi.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vidaloca.skibidi.address.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    @NotNull(message = "Address is obligatory")
    private Address address;

    @JsonBackReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventUser> eventUsers;


    @NotNull(message = "Name is obligatory")
    @Length(min = 2, max = 20, message = "Event name length has to be between 2 and 20")
    private String name;

    @NotNull(message = "Start time is obligatory")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    private String additionalInformation;


    public static final class EventBuilder {
        private Long id;
        private Address address;
        private List<EventUser> eventUsers;
        private String name;
        private LocalDateTime startTime;
        private String additionalInformation;

        private EventBuilder() {
        }

        public static EventBuilder anEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public EventBuilder withEventUsers(List<EventUser> eventUsers) {
            this.eventUsers = eventUsers;
            return this;
        }

        public EventBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventBuilder withAdditionalInformation(String additionalInformation) {
            this.additionalInformation = additionalInformation;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setAddress(address);
            event.setEventUsers(eventUsers);
            event.setName(name);
            event.setStartTime(startTime);
            event.setAdditionalInformation(additionalInformation);
            return event;
        }
    }
}
