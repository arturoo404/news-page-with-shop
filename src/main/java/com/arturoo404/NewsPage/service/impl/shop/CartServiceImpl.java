package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.CartDetailRepository;
import com.arturoo404.NewsPage.repository.shop.CartRepository;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.AvailableProductService;
import com.arturoo404.NewsPage.service.shop.CartService;
import com.arturoo404.NewsPage.service.shop.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CartDetailRepository cartDetailRepository;

    @Autowired
    private AvailableProductService availableProductService;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartDetailRepository cartDetailRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    //TODO Remove all product from cart when promotion start
    @Override
    public Cart addProductToCart(String email, Long id, Integer quantity) throws ExistInDatabaseException {
        Optional<Cart> byUserEmail = cartRepository.findByUserEmail(email);
        if (byUserEmail.isEmpty()){
            throw new ExistInDatabaseException("We did not find user with this email:\u00A0" + email);
        }

        Product product = availableProductService.availableStatus(id, quantity);

        Cart cart = byUserEmail.get();
        List<CartDetail> cartDetail = cart.getCartDetail();
        final Optional<CartDetail> first = cartDetail.stream()
                .filter(d -> d.getProduct().getId().equals(id))
                .findFirst();

        if (first.isEmpty()){
            cartDetail.add(CartDetail.builder()
                            .cart(cart)
                            .quantity(quantity)
                            .product(product)
                    .build());
            cart.setCartDetail(cartDetail);
            cart.setProductQuantity(cart.getProductQuantity() + quantity);
            cart.setAmount(cart.getAmount() + productPrice(id) * quantity);

            return cartRepository.save(cart);
        }
        cartDetailRepository.updateProductQuantity(first.get().getQuantity() + quantity, first.get().getId());

        cart.setCartDetail(cartDetail);
        cart.setProductQuantity(cart.getProductQuantity() + quantity);
        cart.setAmount(cart.getAmount() + productPrice(id) * quantity);

        return cartRepository.save(cart);
    }

    private Double productPrice(Long productId){
        Product product = productRepository.findById(productId).get();

        if (product.getProductPrice().isDiscount()){
            return product.getProductPrice().getDiscountPrice();
        }
        return product.getProductPrice().getPrice();
    }
}
