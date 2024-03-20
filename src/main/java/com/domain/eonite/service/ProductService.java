package com.domain.eonite.service;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.eonite.dto.*;
import com.domain.eonite.entity.*;
import com.domain.eonite.repository.*;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepo ProductRepo;

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductReviewRepo productReviewRepo;

    @Autowired
    private TransactionDetailRepo TransactionDetailRepo;

    public ProductRes addProduct(ProductRes request){
        ProductRes resp =  new ProductRes();
        vendorRepo.findById(request.getVendorId()).ifPresentOrElse((Vendor)->{
            Product product = new Product();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setDescription(request.getDescription());
            product.setCapacity(request.getCapacity());
            product.setRating((float) 0);
            product.setVendor(Vendor);
            ProductRepo.save(product);
            List<Product> products = new ArrayList<Product>();
            products.add(product);
            try{
                resp.setProducts(products);
                resp.setMessage("Success Add Product");
                resp.setStatusCode(200);
    
            }catch(Exception e){
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
        }, ()->{
            resp.setStatusCode(500);
            resp.setError("Vendor Not Found");
        });
        return resp;
    }

    public ProductRes updateProduct(ProductRes request){
        ProductRes resp =  new ProductRes();
        ProductRepo.findById(request.getId()).ifPresentOrElse((product)->{
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setDescription(request.getDescription());
            product.setCapacity(request.getCapacity());
            ProductRepo.save(product);     
            try{
                resp.setMessage("Success Update Product");
                resp.setStatusCode(200);
    
            }catch(Exception e){
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
        }, ()->{
            resp.setStatusCode(500);
            resp.setError("Vendor Not Found");
        });
        return resp;
    }

    public ProductRes getProduct(Integer id){
        ProductRes resp =  new ProductRes();
        List<Product> products = ProductRepo.findAllById(id);
        try{
            resp.setProducts(products);
            resp.setMessage("Success Get Product with Id "+id);
            resp.setStatusCode(200);

        }catch(Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ProductRes getProductbyVendor(Integer id){
        ProductRes resp =  new ProductRes();
        List<Product> products = ProductRepo.findAllByVendorId(id);
        try{
            resp.setProducts(products); 
            resp.setMessage("Success Get All Product with Vendor Id "+id);
            resp.setStatusCode(200);

        }catch(Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    // public ProductRes deleteProduct(Integer id){
    //     ProductRes resp =  new ProductRes();
    //     bankAccountRepo.findById(id).ifPresentOrElse((bankacc)->{
    //         bankAccountRepo.deleteBankAccount(id);
    //         try{
    //             resp.setMessage("Success Delete Bank Account for Id " + id);
    //             resp.setStatusCode(200);
    
    //         }catch(Exception e){
    //             resp.setStatusCode(500);
    //             resp.setError(e.getMessage());
    //         }
    //     }, ()->{
    //         resp.setStatusCode(500);
    //         resp.setError("Bank Account Not Found");
    //     });
    //     return resp;
    // }

    public ProductReviewRes addProductReview(ProductReviewRes request){
        ProductReviewRes resp =  new ProductReviewRes();
        ProductReview productReview = new ProductReview();
            productReview.setTransactionDetail(TransactionDetailRepo.findById(request.getTransaction_detail_id()).get());
            productReview.setRating(request.getRating());
            productReview.setReview(request.getReview());
            productReview.setUser(userRepo.findById(request.getUser_id()).get());
            productReview.setProduct(ProductRepo.findById(request.getProduct_id()).get());
            productReviewRepo.save(productReview);
                ProductRepo.findById(request.getProduct_id()).ifPresentOrElse((product)->{
                    product.setRating(ProductRepo.updateRatingProduct(product.getId()));
                    vendorRepo.findById(product.getVendor().getId()).ifPresentOrElse((vendor)->{
                        vendor.setRating(ProductRepo.updateRatingVendor(vendor.getId()));
                    },null);
                    ProductRepo.save(product);
                }, null);
            List<ProductReview> productrws = new ArrayList<ProductReview>();
            productrws.add(productReview);
            try{
                resp.setProductReview(productrws);
                resp.setMessage("Success Add Review Product");
                resp.setStatusCode(200);
    
            }catch(Exception e){
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
        return resp;
    }

    public ProductReviewRes updateProductReview(ProductReviewRes request){
        ProductReviewRes resp =  new ProductReviewRes();
        productReviewRepo.findById(request.getId()).ifPresentOrElse((productRev)->{
            productRev.setRating(request.getRating());
            productRev.setReview(request.getReview());
            productReviewRepo.save(productRev);
                ProductRepo.findById(request.getProduct_id()).ifPresentOrElse((product)->{
                    product.setRating(ProductRepo.updateRatingProduct(product.getId()));
                    vendorRepo.findById(product.getVendor().getId()).ifPresentOrElse((vendor)->{
                        vendor.setRating(ProductRepo.updateRatingVendor(vendor.getId()));
                    },null);
                    ProductRepo.save(product);
                }, null);
            try{
                resp.setMessage("Success Update Product Review");
                resp.setStatusCode(200);
    
            }catch(Exception e){
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
        },()->{
            resp.setStatusCode(500);
            resp.setError("Product Review Not Found");
        });

        return resp;
    }

    public ProductReviewRes getProductReviewbyProduct(Integer id){
        ProductReviewRes resp =  new ProductReviewRes();
        List<ProductReview> products = productReviewRepo.findAllByProductId(id);
        try{
            resp.setProductReview(products);
            resp.setMessage("Success Get All Review Product with Product Id "+id);
            resp.setStatusCode(200);

        }catch(Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}
