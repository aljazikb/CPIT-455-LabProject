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

    public int estimateProductionTime(int units, int minutesPerUnit, int setupMinutes) {

        if (units <= 0 || minutesPerUnit <= 0 || setupMinutes < 0) {
            throw new IllegalArgumentException("Invalid timing input");
        }

        long totalTime = (long) units * minutesPerUnit + setupMinutes;

        if (totalTime > Integer.MAX_VALUE) {
            throw new ArithmeticException("Production time is too large");
        }
        return (int) totalTime;
    }

    public double performanceCalc(int actualUnits, int plannedUnits) {

        if (actualUnits < 0 || plannedUnits <= 0) {
            throw new IllegalArgumentException("Invalid output input");
        }

        return actualUnits * 100.0 / plannedUnits;
    }

    public boolean needsMaintenance(int usageMinutes, int serviceInterval, int failures) {

        if (usageMinutes < 0 || serviceInterval <= 0 || failures < 0) {
            throw new IllegalArgumentException("Invalid maintenance input");
        }

      if (usageMinutes > serviceInterval) {
            return true;}
      if (failures >= 3) {
        return true;}

        return false;
    }
}