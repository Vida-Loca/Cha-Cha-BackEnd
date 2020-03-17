package com.vidaloca.skibidi.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vidaloca.skibidi.event.utills.SqlTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
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
    private int event_id;

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

}
