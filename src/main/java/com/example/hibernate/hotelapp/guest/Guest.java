package com.example.hibernate.hotelapp.guest;

import com.example.hibernate.hotelapp.room.Room;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GUEST1")
public class Guest {

    @Id
    //@SequenceGenerator(name="guest1_seq", sequenceName="guest1_seq", allocationSize=1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="guest1_seq")
    @Column(name="GUEST_ID")
    private Integer guestId;

    @Column(name="NAME")
    @NotNull(message="Name cannot be null")
    private String name;

    @Column(name="LAST_NAME")
    @NotNull(message="Last name cannot be null")
    private String lastName;

    @Column(name="EMAIL")
    @Email
    private String email;

    @Column(name="PHONE_NUMBER")
    @NotNull(message="Phone number cannot be null")
    private String phoneNumber;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "RESERVATION1",
            joinColumns = @JoinColumn(name = "GUEST_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROOM_ID")
    )
    private List<Room> reservedRooms;

    public Guest(int guestId, String name, String lastName, String email, String phoneNumber) {
        this.guestId = guestId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservedRooms = new ArrayList<>();
    }

    public Guest() {
            this.reservedRooms = new ArrayList<>();
    }

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Room> getReservedRooms() {
        return reservedRooms;
    }

    public void setReservedRooms(List<Room> reservedRooms) {
        this.reservedRooms = reservedRooms;
    }

    @Override
    public String toString() {
        String list = "";
        for (Room r : reservedRooms) {
            list += r.getRoomId() + ",";
        }

        return "GUEST{" +
                "\nname: " + name +
                ",\nsurname: " + lastName +
                ",\nemail: " + email +
                ",\nphone: " + phoneNumber +
                ",\nreserved rooms: " + list +
                "\n}";
    }
}

