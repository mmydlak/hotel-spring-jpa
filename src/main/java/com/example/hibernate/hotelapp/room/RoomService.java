package com.example.hibernate.hotelapp.room;

import com.example.hibernate.hotelapp.utils.CallCounter;
import com.example.hibernate.hotelapp.guest.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RoomService {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final CallCounter guestCallCounter;

    private final CallCounter roomCallCounter;

    @Autowired
    public RoomService(GuestRepository guestRepository,
                        RoomRepository roomRepository,
                        CallCounter guestCallCounter,
                        CallCounter roomCallCounter) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.guestCallCounter = guestCallCounter;
        this.roomCallCounter = roomCallCounter;
    }

    private int generateId() {
        int id;
        do {
            id = roomCallCounter.getCounter();
        }
        while(roomRepository.existsById(id));
        return id;
    }

    public int addRoom(int number, int floor, double price, boolean isOccupied) {
        int id = generateId();
        roomRepository.save(new Room(id, number, floor, price, isOccupied));
        return id;
    }

    public Room getById(int id) {
        return roomRepository.findById(id).get();
    }

    public void updateRoom(int id, String number, String floor, String price, String isOccupied) {
        Room r = roomRepository.findById(id).get();
        if(number != null) {
            r.setRoomNumber(Integer.parseInt(number));
        }
        if(floor.length() != 0) {
            r.setFloorNumber(Integer.parseInt(floor));
        }
        if(price.length() != 0) {
            r.setPricePerNight(BigDecimal.valueOf(Double.parseDouble(price)).setScale(3, RoundingMode.HALF_UP).doubleValue());
        }
        if(isOccupied.length() != 0) {
            if(isOccupied.equals("1") || isOccupied.toLowerCase().equals("true")) {
                r.setOccupied(true);
            }
            else if (isOccupied.equals("0") || isOccupied.toLowerCase().equals("false")) {
                r.setOccupied(false);
            }
        }
        roomRepository.save(r);
    }

    public void deleteById(int id) {
        roomRepository.deleteById(id);
    }


    public List<Room> findRoomsWithPriceInRange(double price1, double price2) {
        if(price1 > price2){
            double tmp = price1;
            price1 = price2;
            price2 = tmp;
        }
        return roomRepository.findRoomsWithPriceInRange(price1,price2);
    }

}
