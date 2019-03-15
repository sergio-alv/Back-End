package com.ebrozon.model;
 
import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
 
@Entity
@Table(name = "customer")
public class Customer implements Serializable {
 
 private static final long serialVersionUID = -3009157732242241606L;
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private long id;
 
 @Column(name = "firstname")
 @Size(min = 3, max = 8, message="El nombre tiene que tener entre 3 y 8 caracteres")
 @Pattern(regexp = "[A-z]+", message="El nombre solo puede tener letras mayúsculas o minúsculas")
 private String firstName;
 
 @Column(name = "lastname")
 private String lastName;
 
 
 protected Customer() {
 }
 
 public Customer(String firstName, String lastName) {
 this.firstName = firstName;
 this.lastName = lastName;
 }
 
 @Override
 public String toString() {
 return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
 }
}