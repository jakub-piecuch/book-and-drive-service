package org.redcode.bookanddriveservice.cars.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Car {
    private UUID id;
    private String make;
    private String model;
    private String registrationNumber;
}
