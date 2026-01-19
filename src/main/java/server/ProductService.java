package server;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    public void 상품등록(String name, int price, int qty) {
        productRepository.insert(name, price, qty);
    }

    public List<Product> 상품목록() {
        return productRepository.findAll();
    }

    public Product 상품상세(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new RuntimeException("id not found");
        }
        return product;
    }

    public void 상품삭제(int id) {
        productRepository.deleteById(id);
    }
}
