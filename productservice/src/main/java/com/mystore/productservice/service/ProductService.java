package com.mystore.productservice.service;
import com.mystore.productservice.dto.ProductRequest;
import com.mystore.productservice.dto.ProductResponse;
import com.mystore.productservice.event.ProductPlacedEvent;
import com.mystore.productservice.model.Product;
import com.mystore.productservice.repository.ProductRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository prodrepo ;
    private final KafkaTemplate<String, ProductPlacedEvent> kafkatemplate;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public ProductService(ProductRepository r, KafkaTemplate<String, ProductPlacedEvent> kafkatemplate, EntityManagerFactory entityManagerFactory) {
        this.prodrepo = r;
        this.kafkatemplate = kafkatemplate;
        this.entityManagerFactory = entityManagerFactory;
    }


    public List<ProductResponse> getProducts(){
        List<Product> products = prodrepo.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product ){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
    public Product getProductById(int id) {
        Optional<Product> optionalproduct = prodrepo.findById(id);

        if (optionalproduct.isPresent()) {
            return optionalproduct.get();
        } else {
            throw new IllegalStateException("product with ID " +id+" is not found");
        }
    }


    public void addNewProduct(ProductRequest prodreq ) {
      //  Product s = new Product();
        Product p = Product.builder().name(prodreq.getName())
                        .description(prodreq.getDescription())
                        .price(prodreq.getPrice())
                        .build();
        prodrepo.save(p);
        kafkatemplate.send("notifTopic",new ProductPlacedEvent(prodreq.getName()));
        log.info("product is saved");
    }

    public void deleteProduct(int pid) {
        boolean b = prodrepo.existsById(pid);
        if (!b){
            throw new IllegalStateException("product doesn't exist : "+pid);
        }else {
            prodrepo.deleteById(pid);
        }
    }
  @Transactional
    public Product updateProduct(int id, Product prod ) {
        Optional<Product> optionalproduct = prodrepo.findById(id);
        if (optionalproduct.isPresent()) {
            Product existing = optionalproduct.get();
            if (prod.getName() != null ){
                existing.setName(prod.getName());
            }
            if (prod.getDescription() != null){
                existing.setDescription(prod.getDescription());
            }
            if (prod.getPrice() != null ){
                existing.setPrice(prod.getPrice());

            }
            return prodrepo.save(existing);
        } else {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }
}
