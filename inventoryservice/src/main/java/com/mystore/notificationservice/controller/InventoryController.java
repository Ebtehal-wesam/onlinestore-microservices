package com.mystore.notificationservice.controller;
import com.mystore.notificationservice.dto.InventoryDto;
import com.mystore.notificationservice.dto.InventoryResponse;
import com.mystore.notificationservice.model.Inventory;
import com.mystore.notificationservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService iService) {
        this.inventoryService = iService;
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryDto> getProductsFromInventory(){
        return inventoryService.getInventoryProductsQty();
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<Inventory> getProductById(@PathVariable long id) {
        try {
            Inventory p = inventoryService.getInventoryById(id);
            return ResponseEntity.ok(p);
        } catch (IllegalStateException e) {

            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewProduct( @RequestBody InventoryDto pr)
    {
        inventoryService.addNewProductInInventory(pr);
    }

    @DeleteMapping(path= "/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id){
        inventoryService.deleteProductFromInventory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path="/update/{id}")
    public ResponseEntity<Inventory> updateProduct(@PathVariable("id") long id, @RequestBody Inventory p  ) {
        Inventory newp = inventoryService.updateProductInInventory(id, p );
        return new ResponseEntity<>(newp , HttpStatus.OK);
    }

    @GetMapping("/isinstock")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        log.info("Received inventory check request for skuCode: {}", skuCode);
        return inventoryService.isInStock(skuCode);
    }
}