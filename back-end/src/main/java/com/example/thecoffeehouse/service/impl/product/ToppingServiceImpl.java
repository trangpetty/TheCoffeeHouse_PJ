package com.example.thecoffeehouse.service.impl.product;

import com.example.thecoffeehouse.entity.product.Topping;
import com.example.thecoffeehouse.repository.product.ToppingRepository;
import com.example.thecoffeehouse.service.product.ToppingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToppingServiceImpl implements ToppingService {
    private final ToppingRepository toppingRepository;

    public ToppingServiceImpl(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @Override
    public List<Topping> findAll() {
        return toppingRepository.findAll();
    }

    @Override
    public Topping createTopping(Topping topping) {
        return toppingRepository.save(topping);
    }

    @Override
    public Topping updateTopping(Long id, Topping savedTopping) {
        Topping topping = toppingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Topping not found"));

        topping.setName(topping.getName());
        topping.setImage(topping.getImage());
        topping.setPrice(topping.getPrice());

        return toppingRepository.save(topping);
    }

    @Override
    public void deleteTopping(Long id) {
        Topping topping = toppingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Topping not found"));
        toppingRepository.deleteById(id);
    }
}
