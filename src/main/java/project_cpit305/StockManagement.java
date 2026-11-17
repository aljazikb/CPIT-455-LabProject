/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;


/**
 *
 * @author aljaz
 */


public class StockManagement {
    
    private int quantity;
    
    public StockManagement(int initialQuantity) {
        if (initialQuantity < 0){
            throw new IllegalArgumentException("Negative stock");}
        quantity = initialQuantity;
    }
    
    public int getQuantity() {
        return quantity; 
    }

    
    public int calculateRequiredMaterial(int units, int materialPerUnit, int wastePercent) {
        
        if (units <= 0 || materialPerUnit <= 0 || wastePercent < 0 || wastePercent > 100){
           
            throw new IllegalArgumentException("Invalid material requirement");
        }
        long base = (long) units * materialPerUnit;
        long waste = (base / 100) * wastePercent+ ((base % 100) * wastePercent + 99) / 100;

        if (base > Integer.MAX_VALUE|| waste > Integer.MAX_VALUE - base) {
                throw new ArithmeticException("Required material is too large");
        }

        return (int) (base + waste);
    }

   
    public boolean decreaseStock(int required) {
        
        if (required <= 0){
            throw new IllegalArgumentException("Demand must be positive");
        }
        if (required > quantity){
            return false;}
        
        quantity -= required;
        return true;
    }

    public String checkStatus(int minimum) {
        
        if (minimum < 0){
            throw new IllegalArgumentException("Negative minimum");}
        if (quantity == 0){
            return "EMPTY";}
        if (quantity <= minimum) {
            return "LOW STOCK";
        }
        
        return "STOCK OK";
    }
}
