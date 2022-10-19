package com.example.demospringboots3.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Asset {
    ///almacena los bytes
    private byte[]content;
    private String contentType;

}
