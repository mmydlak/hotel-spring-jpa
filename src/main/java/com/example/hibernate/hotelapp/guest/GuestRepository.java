package com.example.hibernate.hotelapp.guest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Integer> {

    List<Guest> findByLastNameContainingOrderByLastNameDesc(String toFind);
    List<Guest> findByLastNameContainingOrderByLastNameAsc(String toFind);

}
