package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartDetailDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartProductDetailDto;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.CartDetailRepository;
import com.arturoo404.NewsPage.repository.shop.CartRepository;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.AvailableProductService;
import com.arturoo404.NewsPage.service.shop.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Object addProductToCart(String email, Long id, Integer quantity) throws ExistInDatabaseException {
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
            cartRepository.save(cart);

            return "Product has added";
        }

        cart.setCartDetail(cartDetail);
        cart.setProductQuantity(cart.getProductQuantity() + quantity);
        cart.setAmount(cart.getAmount() + productPrice(id) * quantity);
        cartRepository.save(cart);

        cartDetailRepository.updateProductQuantity(first.get().getQuantity() + quantity, first.get().getId());
        return "Product has added";
    }

    @Override
    public CartNavInfoDto findCartNavInfo(String email) throws ExistInDatabaseException {
        Optional<Cart> cart = cartRepository.findByUserEmail(email);
        userExist(cart);
        return new CartNavInfoDto(cart.get().getAmount(), cart.get().getProductQuantity());
    }

    @Override
    public Object findCartDetail(String email) throws ExistInDatabaseException {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(email);
        userExist(cartOptional);
        Cart cart = cartOptional.get();

        return new CartDetailDto(cart.getAmount(),
                cart.getProductQuantity(),
                cart.getCartDetail().stream()
                        .map(c -> new CartProductDetailDto(
                                c.getQuantity(),
                                c.getProduct().getId(),
                                c.getProduct().getName(),
                                productPrice(c.getProduct().getId())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Object deleteProductFromCart(String email, Long id, Integer quantity) throws ExistInDatabaseException {
        Optional<CartDetail> cartDetailOptional = cartDetailRepository.findByIdAndUserEmail(email, id);
        if (cartDetailOptional.isEmpty()){
            throw new ExistInDatabaseException("Entity does not exist.");
        }

        CartDetail cartDetail = cartDetailOptional.get();
        if (cartDetail.getQuantity() < quantity){
            throw new IndexOutOfBoundsException("Item quantity is too large.");
        }

        if (cartDetail.getQuantity().equals(quantity)){
            cartDetail.getCart().setProductQuantity(cartDetail.getCart().getProductQuantity() - quantity);
            cartDetail.getCart().setAmount(cartDetail.getCart().getAmount() - productPrice(id) * quantity);
            cartDetailRepository.delete(cartDetail);
            return "Successfully deleted product from cart.";
        }

        cartDetail.setQuantity(cartDetail.getQuantity() - quantity);
        cartDetail.getCart().setProductQuantity(cartDetail.getCart().getProductQuantity() - quantity);
        cartDetail.getCart().setAmount(cartDetail.getCart().getAmount() - productPrice(id) * quantity);
        cartDetailRepository.save(cartDetail);
        return "Successfully deleted product from cart.";
    }

    @Override
    public void deleteCartDetail(Long cartId) {
        cartDetailRepository.deleteAllCartDetails(cartId);
    }

    @Override
    public void restartStatistic(Long id) {
        cartRepository.restartCartStatistic(id);
    }

    private void userExist(Optional<?> o) throws ExistInDatabaseException {
        if (o.isEmpty()){
            throw new ExistInDatabaseException("User does not exist");
        }
    }

    private Double productPrice(Long productId){
        Product product = productRepository.findById(productId).get();

        if (product.getProductPrice().isDiscount()){
            return product.getProductPrice().getDiscountPrice();
        }
        return product.getProductPrice().getPrice();
    }
}
