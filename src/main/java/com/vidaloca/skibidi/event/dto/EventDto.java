package com.vidaloca.skibidi.event.dto;

import com.sun.istack.NotNull;
import com.vidaloca.skibidi.model.Address;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

public class EventDto {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private Date startDate;

    @NotNull
    @NotEmpty
    private Address address;

    public EventDto(@NotEmpty String name, @NotEmpty Date startDate, @NotEmpty Address address) {
        this.name = name;
        this.startDate = startDate;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
