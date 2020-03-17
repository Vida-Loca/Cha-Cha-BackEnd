package com.vidaloca.skibidi.event.dto;

import com.vidaloca.skibidi.address.dto.AddressDto;

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
