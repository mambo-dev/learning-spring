package com.example.demo.bean;

import lombok.Data;

@Data
public class Smp {
    private Type type;
    private long datetime;
    private double value;

    public enum Type {
        LAND(1),
        JEJU(9);

        int value;
        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
