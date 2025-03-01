package com.example.senior_project.service.buyer;

import com.example.senior_project.model.Favorite;
import com.example.senior_project.model.Product;
import com.example.senior_project.model.User;
import com.example.senior_project.repository.FavoriteRepository;
import com.example.senior_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Favorite addToFavorites(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Bu ürün zaten favorilerinizde");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFromFavorites(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (!favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Bu ürün favorilerinizde değil");
        }

        favoriteRepository.deleteByUserAndProduct(user, product);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getUserFavorites(User user) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }
} 