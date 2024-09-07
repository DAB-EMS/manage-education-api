package com.example.manageeducation.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForListOfCustomer {
    private String keyword;
    private int page;
    private int size;
    private String sortBy;
    private String sortType;
}
