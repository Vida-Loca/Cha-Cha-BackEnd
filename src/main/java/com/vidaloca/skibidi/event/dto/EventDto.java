package com.vidaloca.skibidi.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import com.vidaloca.skibidi.event.utills.SqlTimeDeserializer;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.sql.Time;

public class EventDto {
    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotNull
    private Date startDate;
    @NotNull
    private AddressDto address;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    private Time startTime;

    private String additionalInformation;

    public EventDto(){

    }

    public EventDto(@NotEmpty String name, Date startDate, AddressDto address, Time startTime, String additionalInformation) {
        this.name = name;
        this.startDate = startDate;
        this.address = address;
        this.startTime = startTime;
        this.additionalInformation = additionalInformation;
    }
    public EventDto(@NotEmpty String name, Date startDate, AddressDto address, Time startTime) {
        this.name = name;
        this.startDate = startDate;
        this.address = address;
        this.startTime = startTime;
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
