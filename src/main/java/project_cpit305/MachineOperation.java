/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;


/**
 *
 * @author aljaz
 *
 */

public class MachineOperation {
    
    // Time is expressed in minutes, including one setup period per batch.
    public int estimateProductionTime(int units, int minutesPerUnit, int setupMinutes) {
        if (units <= 0 || minutesPerUnit <= 0 || setupMinutes < 0)
            throw new IllegalArgumentException("Invalid timing input");
        return Math.addExact(Math.multiplyExact(units, minutesPerUnit), setupMinutes);
    }

    // Percentage of planned output achieved; values above 100 are allowed.
    public double performanceCalc(int actualUnits, int plannedUnits) {
        if (actualUnits < 0 || plannedUnits <= 0)
            throw new IllegalArgumentException("Invalid output input");
        return actualUnits * 100.0 / plannedUnits;
    }

    // Maintenance is required when either operating time or failures reach a limit.
    public boolean needsMaintenance(int usageMinutes, int serviceInterval, int failures) {
        if (usageMinutes < 0 || serviceInterval <= 0 || failures < 0)
            throw new IllegalArgumentException("Invalid maintenance input");
        return usageMinutes > serviceInterval || failures >= 3;
    }
}
