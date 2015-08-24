/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appclasses;

/**
 *
 * @author DEV
 * this class defines the product in this App 
 */
public class Product 
{
    /**
     * name of the product
     */
    private final String label;
    
    /**
     * id of the product - auto generated in this App
     */
    private final int productId;

    public Product(String label, int productId)
    {
        this.label = label;
        this.productId = productId;
    }

    public String getLabel() 
    {
        return label;
    }

    public int getProductId() 
    {
        return productId;
    }
    
    @Override
    public String toString()
    {
        return label;
    }
    
}
