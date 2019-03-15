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
@Table(name = "caca")
public class caca implements Serializable {
 
 private static final long serialVersionUID = -3009157732242241606L;
 
 @Id
 @Column(name = "cust")
 private int cust;
 @Id
 @Column(name = "clase")
 private int clase;
 
 @Column(name = "algo")
 private int algo;
 
 @Column(name = "algo2")
 private int algo2;
 
 protected caca() {
 }
 
 public caca(int c1, int c2, int a1, int a2) {
 this.cust = c1;
 this.clase = c2;
 this.algo2 = a2;
 this.algo = a1;
 }
}