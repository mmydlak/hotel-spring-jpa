package com.example.hibernate.hotelapp.controller;

import com.example.hibernate.hotelapp.error.MyError;
import com.example.hibernate.hotelapp.guest.Guest;
import com.example.hibernate.hotelapp.guest.GuestRepository;
import com.example.hibernate.hotelapp.guest.GuestService;
import com.example.hibernate.hotelapp.room.Room;
import com.example.hibernate.hotelapp.room.RoomRepository;
import com.example.hibernate.hotelapp.room.RoomService;
import com.example.hibernate.hotelapp.utils.CallCounter;
import javassist.NotFoundException;
import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class MainController {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final CallCounter guestCallCounter;

    private final CallCounter roomCallCounter;

    private final GuestService guestService;

    private final RoomService roomService;

    @Autowired
    public MainController(GuestRepository guestRepository,
                          RoomRepository roomRepository,
                          CallCounter guestCallCounter,
                          CallCounter roomCallCounter, GuestService guestService, RoomService roomService) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.guestCallCounter = guestCallCounter;
        this.roomCallCounter = roomCallCounter;
        this.guestService = guestService;
        this.roomService = roomService;
    }

    @RequestMapping(value = "/Guest/add", method = RequestMethod.POST)
    public String addGuest(@RequestParam(value = "name") String name,
                           @RequestParam(value = "lastname") String lastName,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "phone") String phoneNumber) {
        guestService.addGuest(name, lastName, email, phoneNumber);
        return "Guest was added successfully.";
    }

    @RequestMapping(value = "/Guest/get", method = RequestMethod.GET)
    public Guest getGuest(@RequestParam(value = "id") int id) {
        return guestService.getById(id);
    }

    @RequestMapping(value = "/Guest/update", method = RequestMethod.PUT)
    public String updateGuest(@RequestParam(value = "id") int id,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "lastname") String lastName,
                          @RequestParam(value = "email") String email,
                          @RequestParam(value = "phone") String phoneNumber) {
        guestService.updateGuest(id, name, lastName, email, phoneNumber);
        return "Guest was updated successfully.";
    }

    @RequestMapping(value = "/Guest/delete", method = RequestMethod.DELETE)
    public String deleteGuest(@RequestParam(value = "id") int id) throws NotFoundException {
        if(!guestRepository.existsById(id))
            throw new NotFoundException("There is no guest with such id.");
        guestService.deleteById(id);
        return "Guest was deleted successfully.";
    }


    @RequestMapping(value = "/Room/add", method = RequestMethod.POST)
    public String addRoom(@RequestParam(value = "number") int number,
                           @RequestParam(value = "floor") int floor,
                           @RequestParam(value = "price") double price,
                           @RequestParam(value = "occupied") boolean isOccupied) {
        roomService.addRoom(number, floor, price, isOccupied);
        return "Room was added successfully.";
    }

    @RequestMapping(value = "/Room/get", method = RequestMethod.GET)
    public Room getRoom(@RequestParam(value = "id") int id) {
        return roomService.getById(id);
    }

    @RequestMapping(value = "/Room/update", method = RequestMethod.PUT)
    public String updateRoom(@RequestParam(value = "id") int id,
                             @RequestParam(value = "number") String number,
                             @RequestParam(value = "floor") String floor,
                             @RequestParam(value = "price") String price,
                             @RequestParam(value = "occupied") String isOccupied) {
        roomService.updateRoom(id, number, floor, price, isOccupied);
        return "Room was updated successfully.";
    }

    @RequestMapping(value = "/Room/delete", method = RequestMethod.DELETE)
    public String deleteRoom(@RequestParam(value = "id") int id) {
        roomService.deleteById(id);
        return "Room was deleted successfully.";
    }

    @RequestMapping(value = "/Guest/reserve", method = RequestMethod.PUT)
    public String reserveRoom(@RequestParam(value = "guestID") int guestId,
                             @RequestParam(value = "roomID") int roomId) {
        guestService.reserveRoom(guestId, roomId);
        return "Guest " + guestId + " reserved room " + roomId + ".";
    }

    @RequestMapping(value = "/Guest/find/surname", method = RequestMethod.GET)
    public List<Guest> findByPartialSurname(@RequestParam(value = "what") String what,
                                       @RequestParam(value = "sorting", required = false) String sorting) {

        return guestService.findByPartialSurnameAndSort(what, sorting);
    }

    @RequestMapping(value = "/Room/find/price", method = RequestMethod.GET)
    public String findRoomsWithPriceInRange(@RequestParam(value = "price1") double price1,
                                            @RequestParam(value = "price2") double price2) {

        return roomService.findRoomsWithPriceInRange(price1, price2).toString();
    }

    @ExceptionHandler
    void handleException(Exception e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        MyError myError = new MyError(HttpStatus.BAD_REQUEST, "Validation Error!", errors);
        return new ResponseEntity<Object>(myError, new HttpHeaders(), myError.getStatus());
    }



    @ExceptionHandler
    void noResultException(NoResultException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),e.toString());
    }

    @ExceptionHandler
    void noSuchElementException(NoSuchElementException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),e.toString());
    }

    @ExceptionHandler
    void notFoundException(NotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),e.toString());
    }

    @ExceptionHandler
    void queryException(QueryException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),e.toString());
    }

    @ExceptionHandler
    void numberFormatException(NumberFormatException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),e.toString() );
    }

    @ExceptionHandler
    void conversionFailedException(ConversionFailedException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
    }

    @ExceptionHandler
    void handleNullPointerException(NullPointerException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),e.toString());
    }

}
