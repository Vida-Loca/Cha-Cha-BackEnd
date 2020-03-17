package com.vidaloca.skibidi.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import com.vidaloca.skibidi.event.utills.SqlTimeDeserializer;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

public class EventDto {
    private String name;
    private LocalDateTime startTime;
    private AddressDto address;
    private String additionalInformation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
