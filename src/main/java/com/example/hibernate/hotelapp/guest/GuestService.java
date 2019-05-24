package com.example.hibernate.hotelapp.guest;

import com.example.hibernate.hotelapp.utils.CallCounter;
import com.example.hibernate.hotelapp.room.Room;
import com.example.hibernate.hotelapp.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final CallCounter guestCallCounter;

    private final CallCounter roomCallCounter;

    @Autowired
    public GuestService(GuestRepository guestRepository,
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
            id = guestCallCounter.getCounter();
        }
        while(guestRepository.existsById(id));
        return id;
    }

    public int addGuest(String name, String lastName, String email, String phoneNumber){
        int id = generateId();
        guestRepository.save(new Guest(id, name, lastName, email, phoneNumber));
        return id;
    }

    public Guest getById(int id) {
        return guestRepository.findById(id).get();
    }

    public void updateGuest(int id, String name, String lastName, String email, String phoneNumber) {
        Guest g = guestRepository.findById(id).get();
        if (name.length() != 0) {
            g.setName(name);
        }
        if (lastName.length() != 0) {
            g.setLastName(lastName);
        }
        if (email.length() != 0) {
            g.setEmail(email);
        }
        if (phoneNumber.length() != 0) {
            g.setPhoneNumber(phoneNumber);
        }
        guestRepository.save(g);
    }

    public void deleteById(int id) {
        guestRepository.deleteById(id);
    }

    public void reserveRoom(int guestId, int roomId) {
        Guest g = guestRepository.findById(guestId).get();
        List<Room> rooms = g.getReservedRooms();
        rooms.add(roomRepository.findById(roomId).get());
        g.setReservedRooms(rooms);
        guestRepository.save(g);
    }

    public List<Guest> findByPartialSurname(String what){
        return guestRepository.findByLastNameContainingOrderByLastNameAsc(what);
    }

    public List<Guest> findByPartialSurnameAndSort(String what, String sorting) {
        if (sorting != null) {
            if (sorting.equals("desc")) {
                return guestRepository.findByLastNameContainingOrderByLastNameDesc(what);
            }
        }
        return findByPartialSurname(what);
    }

    @RequestMapping(value = "/Room/find/price", method = RequestMethod.GET)
    public String findRoomsWithPriceInRange(@RequestParam(value = "price1") double price1,
                                            @RequestParam(value = "price2") double price2) {

        if(price1 > price2){
            double tmp = price1;
            price1 = price2;
            price2 = tmp;
        }
        return roomRepository.findRoomsWithPriceInRange(price1,price2).toString();
    }
}
