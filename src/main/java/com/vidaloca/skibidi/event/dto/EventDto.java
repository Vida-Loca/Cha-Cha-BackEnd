package com.vidaloca.skibidi.event.dto;

import com.vidaloca.skibidi.address.dto.AddressDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventDto {
    private String name;
    private LocalDateTime startTime;
    private AddressDto address;
    private String additionalInformation;
}
