package com.mystore.productservice.controller;
import com.mystore.productservice.dto.ProductResponse;
import com.mystore.productservice.model.Product;
import com.mystore.productservice.service.ProductService;
import com.mystore.productservice.dto.ProductRequest;

//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@RequestMapping(path = "api/products")
@EnableWebMvc
public class ProductController {
    private final ProductService prodService ;
    @Autowired
    public ProductController(ProductService pService) {
        this.prodService = pService;
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts(){
        return prodService.getProducts();
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        try {
            Product p = prodService.getProductById(id);
            return ResponseEntity.ok(p);
        } catch (IllegalStateException e) {

            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewProduct( @RequestBody ProductRequest pr)
    {

        prodService.addNewProduct(pr);
    }

    @DeleteMapping(path= "/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id){
        prodService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    ///  String name, String model, String company

    @PutMapping(path="/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product p  ) {
        Product newp = prodService.updateProduct(id, p );
        return new ResponseEntity<>(newp , HttpStatus.OK);
    }



}

