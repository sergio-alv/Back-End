package com.ebrozon.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.ebrozon.model.Customer;
import com.ebrozon.repository.CustomerRepository;
 
@RestController
public class WebController {
    @Autowired
    CustomerRepository repository;
      
    @RequestMapping("/save")
    public String process(){
    	try {
	        repository.save(new Customer("Jack", "Smith"));
	        repository.save(new Customer("Adam", "Johnson"));
	        repository.save(new Customer("Kim", "Smith"));
	        repository.save(new Customer("David", "Williams"));
	        repository.save(new Customer("Peter", "Davis"));
	        return "Done";
    	}
    	catch(Exception e) {
    		return e.getMessage();
    	}
    }
      
      
    @RequestMapping("/findall")
    public String findAll(){
        String result = "<html>";
          
        for(Customer cust : repository.findAll()){
            result += "<div>" + cust.toString() + "</div>";
        }
          
        return result + "</html>";
    }
    
    /*@RequestMapping("/findbyid")
    public String findById(@RequestParam("id") long id){
        String result = "";
        result = repository.findById(id).toString();
        Customer aux = repository.findById(id).get();
        return result;
    }*/
    
    @RequestMapping("/findbyid/{id}/{p}")
    public String findById(@PathVariable long id, @PathVariable String p){
        String result = "";
        result = repository.findById(id).toString();
        Customer aux = repository.findById(id).get();
        return result + p;
    }
      
    @RequestMapping("/findbycaca")
    public String findByCaca(){
        String result = "";
        for(Customer cust : repository.findByCaca()){
            result += "<div>" + cust.toString() + "</div>";
        }
        return result;
    }
    
    @RequestMapping("/findbylastname")
    public String fetchDataByLastName(@RequestParam("lastname") String lastName){
        String result = "<html>";
          
        for(Customer cust: repository.findByLastName(lastName)){
            result += "<div>" + cust.toString() + "</div>"; 
        }
          
        return result + "</html>";
    }
}