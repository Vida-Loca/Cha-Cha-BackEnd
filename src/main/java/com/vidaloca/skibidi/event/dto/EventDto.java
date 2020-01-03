package com.vidaloca.skibidi.event.dto;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

public class EventDto {
    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotNull
    private Date startDate;
    @NotNull
    private AddressDto address;

    public EventDto(@NotEmpty String name,  Date startDate, AddressDto address) {
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
