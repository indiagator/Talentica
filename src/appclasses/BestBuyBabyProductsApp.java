/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 *
 * @author Prateek
 */
public class BestBuyBabyProductsApp 
{

    /**
     * @param args the command line arguments
     * this variable holds the system path of the csv file to be read
     */
    Path filePath;
    
    /**
     * Set of all shops in the catalogue
     */
    Map<Integer,Shop> shopMap;
    
    /**
     * set of all products in the catalogue
     */
    Map<String,Product> productMap;

    public BestBuyBabyProductsApp(Path filePath) 
    {
        this.filePath = filePath;
        shopMap       = new HashMap<>();
        productMap    = new HashMap<>();
        
    }
    
    /**
     * this method parses the csv file to generate the following objects and associations
     * - shops, products and product offers
     */
    @SuppressWarnings("InfiniteRecursion")
    public void parseCatalogue()
    {
                
        try(BufferedReader in = Files.newBufferedReader(filePath))
        {
            String line;
            boolean firstLineFlag=false;            
            
            //first line in the file should be a general string and not the data record
            //data records come from the second line onwards
            while((line=in.readLine())!=null)
            {
                if(firstLineFlag!=false)
                {
                    this.parseRecord(line);
                }
                else
                {
                    System.out.println(line);
                    firstLineFlag=true;
                }                
            }            
        } 
        catch (IOException ex) 
        {
            System.out.println("Invalid File, Please enter another path");
            System.exit(-1);           
        }
        
        //this invokes the method of the Class Shop to populate its Product Set
        shopMap.forEach((k,v)->shopMap.get(k).populateProductSet());
        
    }
    
    /**
     * this method parses each record or line of the csv file to generate
     * the product offers for each shop
     * @param  
     */
    private void parseRecord(String s)
    {
        // this variable stores the split strings before processing them
        String[] fields;        
        fields = s.split(",");
        
        //condition to check if this is a valid record
        if(fields.length>2)
        {        
        
        Integer shopId=0;
        shopId = Integer.parseInt(fields[0].trim());
        
       if(shopMap.get(shopId)==null)
       {
           shopMap.put((int)shopId, new Shop("shop_"+fields[0], (int) shopId));
       }
        
       
       Set<Product> thisProductSet = new HashSet<>();
       
        for(int i=2;i<fields.length;i++)
        {
            
            if(productMap.get(fields[i])==null)
            {
                productMap.put(fields[i],new Product(fields[i],productMap.size()));
            }
            
            thisProductSet.add(productMap.get(fields[i]));
        }
        
        shopMap.get(shopId).addProductOffer(new ProductOffer(thisProductSet,Double.parseDouble(fields[1]),shopId));
        
       for(String x:fields)
       {
           System.out.print(x+" ");
       }
       
       System.out.println();
        }
    }
    
    public void printEntities()
    {        
        System.out.println(shopMap);
        System.out.println(productMap);
    }
    
    public void printProductOffers()
    {
        shopMap.forEach((k,v)->
        {
            System.out.println(v.getProductOffers());
        }
                );
        
    }    
    
    /**
     *
     * @param products
     * @return BestOffer
     * This method searches for the best offer in the catalogue 
     * to provide the required products. This method also implements
     * the main algorithm for searching and creating the BestOffer.
     * It accepts a variable number of strings as product names and then
     * searches for them in the catalogue.
     */
    public ProductOffer findBestOffer(String... products)
    {
        //set of required products generated from the arguments
        Set<Product> requiredProducts = new HashSet<>();
        
        //list of all possible offers in the catalogue
        //can be generated offline for a heavy traffic application
        List<ProductOffer> allOffers = new ArrayList<>();
        
        //empty object for holding the  bestOffer in the end
        ProductOffer bestOffer = new ProductOffer();        
        
        for(String p: products)
        {
            requiredProducts.add(productMap.get(p));
        }
        
        System.out.println(requiredProducts);  
        
        //do this for each shop one by one to generate all possible offers
        shopMap.forEach((Integer k,Shop v)->
        {
            //grand list of all possible offers
            List<List<ProductOffer>> grandListOffers = new ArrayList<>() ;
            
            ProductOffer tempOffer = new ProductOffer();
            tempOffer.setShopId(v.getShopId());
            
            grandListOffers.add(v.getProductOffers());            
            
            /**
             * all possible combinations of the product offers in each shop
             * are generated and stored for later consumptions. The Algorithm
             * can be inferred from the code
             */
            for(int counter=0;counter<grandListOffers.get(0).size()-1;counter++)
            {
                List<ProductOffer> tempList = new ArrayList<>();
                              
                for(ProductOffer firstElement: grandListOffers.get(0))
                {
                    
                    for(ProductOffer secondElement: grandListOffers.get(counter))
                    {
                        
                        tempOffer.setPrice(firstElement.getPrice()+secondElement.getPrice());
                        Set<Product> union = new HashSet<>(firstElement.getProducts());
                        union.addAll(secondElement.getProducts());
                        tempOffer.setProducts(union);
                        
                        tempList.add(new ProductOffer(tempOffer));
                    }
                    
                }
                
                grandListOffers.add(tempList);
            }
                
            //A single list is generated from the grand list of lists
            grandListOffers.stream().forEach((p)-> allOffers.addAll(p));
        }
                    
                );
        
        
        
        //relevant offers are selected from all possible offers based on whether they contain the 
        //required products or not
       List<ProductOffer> allRelevantOffers = allOffers.stream().filter((p)
                ->p.getProducts().containsAll(requiredProducts)).collect(Collectors.toList());
        
        //find the minimum price amongst all relevant offers
        OptionalDouble bestPrice = allRelevantOffers.stream().mapToDouble(ProductOffer::getPrice).min();
        
       
        //find the relevant offer with the minimum price
        for(ProductOffer tempForBO: allRelevantOffers)
        {
            // The shop with smaller shopId gets selected in case of same price 
            if(tempForBO.getPrice()==bestPrice.getAsDouble())
            {
                bestOffer = tempForBO;
            }
        }        
        
        return bestOffer;
    }
    
    public static void main(String[] args) 
    {
                
        String[] productList= Arrays.copyOfRange(args, 1, args.length);
        
        /**
         * data file should be stored in a folder named product_data
         * within the current working directory from where the project will be run.
         * Specifically where the project jar will be kept OR
         * an appropriate PATH should be given here. Also the Path that application is looking
         * for can be checked in the console as output.
         */
        Path p1 = Paths.get("product_data\\"+args[0]);
        Path p2 = p1.toAbsolutePath();
        
        //can be checked on console to be as intended
        System.out.println(p2.toString());
        
        BestBuyBabyProductsApp testApp = new BestBuyBabyProductsApp(p2);
        testApp.parseCatalogue();             
        
        try
        {          
            //prints the best offer
            System.out.println(testApp.findBestOffer(productList));           
        }
        catch(NullPointerException e)
        {
            System.out.println("No relevant offer found");
        }   
        
    }
    
}
