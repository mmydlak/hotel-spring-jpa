package com.example.hibernate.hotelapp.utils;

import org.springframework.stereotype.Component;

@Component
public class CallCounter {

    private Integer counter = 0;

    public Integer getCounter() {
        counter++;
        return counter;
    }

    public void setCounter(int i) {
        counter = i;
    }
}
