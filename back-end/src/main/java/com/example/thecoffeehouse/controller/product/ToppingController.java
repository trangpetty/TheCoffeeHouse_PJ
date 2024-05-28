package com.example.thecoffeehouse.controller.product;

import com.example.thecoffeehouse.entity.product.Topping;
import com.example.thecoffeehouse.service.product.ToppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topping")
public class ToppingController {
    private final ToppingService toppingService;

    public ToppingController(ToppingService toppingService) {
        this.toppingService = toppingService;
    }

    @PostMapping
    public ResponseEntity<Topping> createTopping(@RequestBody Topping topping) {
        return new ResponseEntity<>(toppingService.createTopping(topping), HttpStatus.CREATED);
    }

    @GetMapping ResponseEntity<List<Topping>> getAllTopping() {
        List<Topping> toppings = toppingService.findAll();
        return ResponseEntity.ok(toppings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topping> updateTopping(@PathVariable Long id, @RequestBody Topping topping) {
        return ResponseEntity.ok(toppingService.updateTopping(id, topping));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopping(@PathVariable Long id) {
        toppingService.deleteTopping(id);
        return ResponseEntity.ok("success");
    }
}
