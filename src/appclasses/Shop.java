/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appclasses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author DEV
 * This class defines the Shop entity in this App 
 */
public class Shop
{
    /**
     * name of the shop
     */
    private final String name;
    
    /**
     * Shop Id
     */
    private final int shopId;
    
    /**
     * A set of all products on offer in this shop
     */
    private final Set<Product> productSet;
    
    /**
     * List of Product Offers with Price
     */
    private final List<ProductOffer> productOffers;

    public Shop(String name, int shopId)
    {
        this.name = name;
        this.shopId = shopId;
        this.productOffers = new ArrayList<>();
        this.productSet = new HashSet<>();
                        
    }

    public String getName()
    {
        return name;
    }

    public int getShopId()
    {
        return shopId;
    }

    public List<ProductOffer> getProductOffers() 
    {
        return productOffers;
    }
    
    /**
     * this method adds a new Product Offer to this Shop
     * @param productOffer 
     */
    public void addProductOffer(ProductOffer productOffer)
    {
        this.productOffers.add(productOffer);
    }
    
    /**
     * Routine to populate the Product Set for this shop from all the Product Offers
     */
    public void populateProductSet()
    {
        productOffers.stream().forEach((p)->productSet.addAll(p.getProducts()));
    }
    
    @Override
    public String toString()
    {
        return "["+shopId+" "+name+"]";
    }

    public Set<Product> getProductSet() {
        return productSet;
    }
    
}
