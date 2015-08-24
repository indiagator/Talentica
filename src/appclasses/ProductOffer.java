/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appclasses;

//import java.util.List;
import java.util.Set;

/**
 *
 * @author DEV
 * this class defines a product offer in this App
 */
public class ProductOffer 
{
    /**
     * list of all products in this offer
     * number of products maybe more than one
     */
    private Set<Product> products;
     /**
     * price of this offer
     */
    private double price;
    
    /**
     * ShopId where this offer is available
     */
    private int shopId;
    
    public ProductOffer() 
    {
    }
    
    public ProductOffer(Set<Product> products, double price,int shopId) 
    {
        this.products = products;
        this.price = price;
        this.shopId=shopId;
    }
    
   
    
    public String getType()
    {
        
        if(products.size()>1)
        {
            return "combo";
        }
        else if(products.size()==1) 
        {
            return "single";
        }
        else if(products.isEmpty())
        {
            return "empty";
        }
        
        return null;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public void addProduct(Product product)
    {
        products.add(product);
    }

    public ProductOffer(ProductOffer po) 
    {
        this.price = po.getPrice();
        this.products = po.getProducts();
        this.shopId = po.getShopId();
    }

    
    
    public void removeProduct(String productName)
    {
        /**
         * this method takes the productName and retrieves the product
         * reference and then removes it from the set
         */
        
    }
    
    @Override
    public String toString()
    {
        return "["+"@Shop "+shopId+" Price: "+price+" for Products: "+products+"]";
    }

    public int getShopId() {
        return shopId;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
    
}
