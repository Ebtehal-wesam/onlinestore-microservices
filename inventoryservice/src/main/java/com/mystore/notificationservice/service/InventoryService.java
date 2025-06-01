package com.mystore.notificationservice.service;
import com.mystore.notificationservice.dto.InventoryDto;
import com.mystore.notificationservice.dto.InventoryResponse;
import com.mystore.notificationservice.model.Inventory;
import com.mystore.notificationservice.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Autowired
    public InventoryService(InventoryRepository r) {
        this.inventoryRepository = r;
    }
    public List<InventoryDto> getInventoryProductsQty(){
        List<Inventory> inventoryproductsqty = inventoryRepository.findAll();
        return inventoryproductsqty.stream().map(this::mapToInventoryQtyResponse).toList();
    }

    private InventoryDto mapToInventoryQtyResponse(Inventory inventory ){
        return InventoryDto.builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }
    public Inventory getInventoryById(long id) {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);

        if (optionalInventory.isPresent()) {
            return optionalInventory.get();
        } else {
            throw new IllegalStateException("product with ID " +id+" is not found");
        }
    }
    public void addNewProductInInventory(InventoryDto newproduct ) {
        //  Product s = new Product();
        Inventory inven = Inventory.builder()
                .skuCode(newproduct.getSkuCode())
                .quantity(newproduct.getQuantity())
                .build();
        inventoryRepository.save(inven);
        log.info("product is saved");
    }

    public void deleteProductFromInventory(long pid) {
        boolean b = inventoryRepository.existsById(pid);
        if (!b){
            throw new IllegalStateException("product doesn't exist : "+pid);
        }else {
            inventoryRepository.deleteById(pid);
        }
    }
    @Transactional
    public Inventory updateProductInInventory(long id, Inventory prod ) {
        Optional<Inventory> optionalproduct = inventoryRepository.findById(id);
        if (optionalproduct.isPresent()) {
            Inventory existing = optionalproduct.get();
            if (prod.getQuantity() != null ){
                existing.setQuantity(prod.getQuantity());
            }

            return inventoryRepository.save(existing);
        } else {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory");
        Thread.sleep(10000);
        log.info("Checking Inventory Is Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}