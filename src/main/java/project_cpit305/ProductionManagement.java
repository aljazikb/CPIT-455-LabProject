package project_cpit305;


/**
 *
 * @author aljaz
 */

public class ProductionManagement {
    
    private final StockManagement stock;
    private final MachineOperation machine;
    private int totalProduced;

    public ProductionManagement(StockManagement stock, MachineOperation machine) {
        if (stock == null || machine == null){
            throw new IllegalArgumentException("Missing component");
        }
        this.stock = stock;
        this.machine = machine;
    }
    
    public int getTotalProduced() {
        return totalProduced;
    }

    public double calculateProductionCost(int units, double materialCost, double laborCost, double setupCost, int discountPercent) {

        if (units <= 0 || materialCost < 0 || laborCost < 0 || setupCost < 0 || discountPercent < 0|| discountPercent > 100) {
            throw new IllegalArgumentException("Invalid cost input");
        }

        double productionCost = units * (materialCost + laborCost);
        double totalCost = productionCost + setupCost;
        double discount = totalCost * discountPercent / 100.0;

        return totalCost - discount;
    }

   
    public int[] planProduction(int units, int batchCapacity) {
        
        if (units <= 0 || batchCapacity <= 0){
            throw new IllegalArgumentException("Invalid batch input");
        }
        
        int count = (units - 1) / batchCapacity + 1;
        
        if (count > 10000){
            throw new IllegalArgumentException("Too many batches");
        }
        int[] batches = new int[count];
        int remaining = units;
        
        for (int i = 0; i < count; i++) {
            batches[i] = Math.min(remaining, batchCapacity);
            remaining -= batches[i];
        }
        return batches;
    }
    

    public boolean processProduction(int units, int materialPerUnit, boolean workerBusy,boolean machineBusy,int usageMinutes, int serviceInterval, int failures) {
        
        int demand = stock.calculateRequiredMaterial(units, materialPerUnit, 0);
        
        boolean maintenance = machine.needsMaintenance(usageMinutes, serviceInterval, failures);
        
        int nextTotal = Math.addExact(totalProduced, units);
        if (workerBusy || machineBusy || maintenance){
            return false;
        }
        if (!stock.decreaseStock(demand)){
            return false;
        }
        totalProduced = nextTotal;
        return true;
    }
    
}
