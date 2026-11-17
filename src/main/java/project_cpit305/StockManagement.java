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
        if (initialQuantity < 0)
            throw new IllegalArgumentException("Negative stock");
        quantity = initialQuantity;
    }
    
    public int getQuantity() {
        return quantity; 
    }

    // Each unit consumes material; round waste allowance up to whole pieces.
    public int calculateRequiredMaterial(int units, int materialPerUnit, int wastePercent) {
        
        if (units <= 0 || materialPerUnit <= 0 || wastePercent < 0 || wastePercent > 100)
            throw new IllegalArgumentException("Invalid material requirement");
        
        long base = Math.multiplyExact((long) units, materialPerUnit);
        
        long total = Math.addExact(base, (Math.multiplyExact(base, wastePercent) + 99) / 100);
        
        return Math.toIntExact(total);
    }

    // Reject invalid demand and preserve stock when the order cannot be filled.
    public boolean decreaseStock(int required) {
        
        if (required <= 0)
            throw new IllegalArgumentException("Demand must be positive");
        
        if (required > quantity)
            return false;
        quantity -= required;
        
        return true;
    }

    // Zero stock is empty; stock below the minimum requires replenishment.
    public String checkStatus(int minimum) {
        
        if (minimum < 0)
            throw new IllegalArgumentException("Negative minimum");
        if (quantity == 0)
            return "EMPTY";
        if (quantity <= minimum) 
            return "LOW STOCK";
        return "STOCK OK";
    }
}
