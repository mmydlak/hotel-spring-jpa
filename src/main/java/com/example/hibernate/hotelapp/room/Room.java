package com.example.hibernate.hotelapp.room;

import com.example.hibernate.hotelapp.guest.Guest;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "ROOM1")
@Table(name = "ROOM1")
public class Room implements Serializable {

    @Id
    @Column(name="ROOM_ID")
    private Integer roomId;

    @Column(name="ROOM_NUMBER")
    @NotNull(message="Room number cannot be null")
    private int roomNumber;

    @Column(name="FLOOR_NUMBER")
    @NotNull(message="Floor number cannot be null")
    private int floorNumber;

    @Column(name="PRICE_PER_NIGHT")
    @NotNull(message="Price per night cannot be null")
    @Min(value=10)
    private double pricePerNight;

    @Column(name="IS_OCCUPIED")
    private boolean isOccupied;

    @ManyToMany(mappedBy = "reservedRooms", cascade = {CascadeType.ALL})
    private List<Guest> guestList;

    public Room() {
        this.guestList = new ArrayList<>();
    }

    public Room(int roomId, int roomNumber, int floorNumber, double pricePerNight, boolean isOccupied) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.pricePerNight = pricePerNight;
        this.isOccupied = isOccupied;
        this.guestList = new ArrayList<>();
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

//    public List<Guest> getGuestList() {
//        return guestList;
//    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }

    @Override
    public String toString() {
        String list = "";
        for (Guest g : guestList) {
            list += g.getGuestId() + ",";
        }

        return "ROOM{" +
                "\nnumber: " + roomNumber +
                ",\nfloor: " + floorNumber +
                ",\nprice/night: " + pricePerNight +
                ",\noccupied: " + isOccupied +
                ",\nfuture guests: " + list +
                "\n}";
    }

}
