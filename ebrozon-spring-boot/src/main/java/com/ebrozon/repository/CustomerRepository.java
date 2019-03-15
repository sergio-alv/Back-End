package com.ebrozon.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
    List<Customer> findByLastName(String lastName);

    @Query("SELECT c FROM Customer c, caca p where p.cust = c.id")
    List<Customer> findByCaca();
}