package com.example.hibernate.hotelapp.room;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

    @Query(value = "SELECT * FROM ROOM1 where ROOM1.PRICE_PER_NIGHT BETWEEN :price1 AND :price2", nativeQuery = true)
    List<Room> findRoomsWithPriceInRange(@Param("price1") double price1, @Param("price2") double price2) ;

}
