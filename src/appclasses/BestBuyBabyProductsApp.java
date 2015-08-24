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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Prateek
 */
public class BestBuyBabyProductsApp 
{

    /**
     * @param args the command line arguments
     */
    Path filePath;
    //BufferedReader in;
    
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
    
    @SuppressWarnings("InfiniteRecursion")
    public void parseCatalogue()
    {
        //this.filePath = p;
        
        try(BufferedReader in = Files.newBufferedReader(filePath))
        {
            String line;
            boolean firstLineFlag=false;
            
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
            Logger.getLogger(BestBuyBabyProductsApp.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Invalid File, Please enter another path");
            System.exit(-1);
           // this.parseCatalogue(filePath);
        }
        
        shopMap.forEach((k,v)->shopMap.get(k).populateProductSet());
        
    }
    
    private void parseRecord(String s)
    {
        
        String[] fields;        
        fields = s.split(",");
        
        //Integer.parseInt(fields[0].trim());
       // Integer.parseInt(fields[1].trim());
        
        
        if(fields.length>1)
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
        Set<Product> requiredProducts = new HashSet<>();
        List<ProductOffer> allOffers = new ArrayList<>();
        ProductOffer bestOffer = new ProductOffer();        
        
        for(String p: products)
        {
            requiredProducts.add(productMap.get(p));
        }
        
        System.out.println(requiredProducts);        
        
        shopMap.forEach((Integer k,Shop v)->
        {
            List<List<ProductOffer>> grandListOffers = new ArrayList<>() ;
            
            ProductOffer tempOffer = new ProductOffer();
            tempOffer.setShopId(v.getShopId());
            
            grandListOffers.add(v.getProductOffers());
            
            for(int counter=0;counter<grandListOffers.get(0).size()-1;counter++)
            {
                List<ProductOffer> tempList = new ArrayList<>();
                              
                for(ProductOffer firstElement: grandListOffers.get(0))
                {
                    
                    for(ProductOffer secondElement: grandListOffers.get(counter))
                    {
                        //tempOffer = null;
                        tempOffer.setPrice(firstElement.getPrice()+secondElement.getPrice());
                        Set<Product> union = new HashSet<>(firstElement.getProducts());
                        union.addAll(secondElement.getProducts());
                        tempOffer.setProducts(union);
                        
                        tempList.add(new ProductOffer(tempOffer));
                    }
                    
                }
                
                grandListOffers.add(tempList);
            }
                
            grandListOffers.stream().forEach((p)-> allOffers.addAll(p));
            
            
            
            /*
           
            List<ProductOffer> tempListOffers = v.getProductOffers();
            //check if this shop has all the products and only then proceed
           if(v.getProductSet().containsAll(requiredProducts))
           {
               System.out.println(v.getName()+" has all the products");                
               ProductOffer temp = new ProductOffer();
               
               
                for(ProductOffer firstElement: tempListOffers)
                {
                    temp.setShopId(firstElement.getShopId());
                    temp.setPrice(firstElement.getPrice());
                    temp.setProducts(firstElement.getProducts());
                    
                    allOffers.add(new ProductOffer(temp));                    
                    
                    for(ProductOffer secondElement : tempListOffers)
                    {
                       temp.setPrice(temp.getPrice()+secondElement.getPrice());
                       Set<Product> union = new HashSet<Product>(temp.getProducts());
                       union.addAll(secondElement.getProducts());
                       temp.setProducts(union);
                       //temp.getProducts().addAll(secondElement.getProducts());
                        
                       allOffers.add(new ProductOffer(temp));
                    }               
                }           
           }
                    
                    */
       
        }
                    
                );
        
        
        
      System.out.println(allOffers);
        
       List<ProductOffer> allRelevantOffers = allOffers.stream().filter((p)
                ->p.getProducts().containsAll(requiredProducts)).collect(Collectors.toList());
        
                
        OptionalDouble bestPrice = allRelevantOffers.stream().mapToDouble(ProductOffer::getPrice).min();
        
        System.out.println(allRelevantOffers);
        
        for(ProductOffer tempForBO: allRelevantOffers)
        {
            // The shop with smaller shopId gets selected in case of same price offers
            if(tempForBO.getPrice()==bestPrice.getAsDouble())
            {
                bestOffer = tempForBO;
            }
        }        
        
        //System.out.println(allOffers);
        return bestOffer;
    }
    
    public static void main(String[] args) 
    {
        // TODO code application logic here        
        // Path p = Paths.get("C:\\Users\\DEV\\Documents\\NetBeansProjects\\BestBuyBabyProductsApp\\product_data\\data_1.csv");
        
        
        String[] productList= Arrays.copyOfRange(args, 1, args.length);
        
        Path p1 = Paths.get("product_data\\"+args[0]);
        Path p2 = p1.toAbsolutePath();
        System.out.println(p2.toString());
        
        BestBuyBabyProductsApp testApp = new BestBuyBabyProductsApp(p2);
        testApp.parseCatalogue();
       // testApp.printEntities();
       // testApp.printProductOffers();
        
        
        try
        {
            //if(testApp.findBestOffer(productList).getProducts().size()!=0)  
            //{
                System.out.println(testApp.findBestOffer(productList));
            //}
        }
        catch(NullPointerException e)
        {
            System.out.println("No relevant offer found");
        }   
        
    }
    
}
