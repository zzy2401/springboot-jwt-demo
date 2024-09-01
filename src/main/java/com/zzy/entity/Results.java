package com.zzy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builder")
public class Results {
    private boolean success;
    private int code;
    private String msg;
    private Map<String,Object> data = new HashMap<>();

    public static Results success() {
        return Results.builder()
                .success(true)
                .code(200)
                .msg("success")
                .build();
    }

    public static Results fail() {
        return Results.builder()
                .success(false)
                .code(500)
                .msg("fail")
                .build();
    }

    public Results Data(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}
