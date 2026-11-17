/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aljaz
 */
public class MachineOperationTest {
    

    private MachineOperation machine;

    @Before
    public void setUp() {
        machine = new MachineOperation();
    }

    // estimateProductionTime
    // (int units, int minutesPerUnit, int setupMinutes)

    @Test
    public void testJ01_TimeWithSetup() {
        
        int actual = machine.estimateProductionTime(10, 5, 20);
        
        assertEquals(70, actual);
    }

    @Test
    public void testJ02_TimeWithoutSetup() {
        
        int actual = machine.estimateProductionTime(10, 5, 0);
        
        
        assertEquals(50, actual);
    }

    @Test
    public void testJ03_OneUnit() {
        
        
        int actual = machine.estimateProductionTime(1, 5, 20);
        assertEquals(25, actual);
    }

    @Test
    public void testJ04_OneMinutePerUnit() {
        
        int actual = machine.estimateProductionTime(10, 1, 20);
        
        
        assertEquals(30, actual);
    }

    @Test
    public void testJ05_MinimumValidInputs() {
        
        int actual = machine.estimateProductionTime(1, 1, 0);
        
        
        assertEquals(1, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ06_RejectZeroUnits() {
        
        machine.estimateProductionTime(0, 5, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ07_RejectNegativeUnits() {
        
        machine.estimateProductionTime(-1, 5, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ08_RejectZeroMinutesPerUnit() {
        machine.estimateProductionTime(10, 0, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ09_RejectNegativeMinutesPerUnit() {
        machine.estimateProductionTime(10, -1, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ10_RejectNegativeSetupMinutes() {
        machine.estimateProductionTime(10, 5, -1);
    }

    @Test
    public void testJ11_MaximumTimeWithoutSetup() {
        int actual = machine.estimateProductionTime(Integer.MAX_VALUE, 1, 0);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test
    public void testJ12_TotalExactlyAtIntLimit() {
        int actual = machine.estimateProductionTime(Integer.MAX_VALUE - 10, 1, 10);

        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test(expected = ArithmeticException.class)
    public void testJ13_RejectMultiplicationOverflow() {
        machine.estimateProductionTime(Integer.MAX_VALUE, 2, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void testJ14_RejectOverflowAfterAddingSetup() {
        machine.estimateProductionTime(Integer.MAX_VALUE, 1, 1);
    }
    
    
    
    // performanceCalc
    // (int actualUnits, int plannedUnits)

    @Test
    public void testJ15_PerformanceAtTarget() {
        double actual = machine.performanceCalc(100, 100);
        assertEquals(100.0, actual, 0.001);
    }

    @Test
    public void testJ16_PerformanceBelowTarget() {
        
        double actual = machine.performanceCalc(50, 100);
        
        assertEquals(50.0, actual, 0.001);
    }

    @Test
    public void testJ17_PerformanceAboveTarget() {
        double actual = machine.performanceCalc(150, 100);
        
        assertEquals(150.0, actual, 0.001);
    }

    @Test
    public void testJ18_FractionalPerformance() {
        
        double actual = machine.performanceCalc(1, 3);
        assertEquals(33.333333, actual, 0.000001);
    }

    @Test
    public void testJ19_ZeroActualUnits() {
        double actual = machine.performanceCalc(0, 100);
        
        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void testJ20_MinimumPositiveInputs() {
        double actual = machine.performanceCalc(1, 1);
        
        assertEquals(100.0, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ21_RejectNegativeActualUnits() {
        
        machine.performanceCalc(-1, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ22_RejectZeroPlannedUnits() {
        machine.performanceCalc(100, 0);}

    @Test(expected = IllegalArgumentException.class)
    public void testJ23_RejectNegativePlannedUnits() {
        machine.performanceCalc(100, -1);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ24_RejectBothInputsZero() {
        machine.performanceCalc(0, 0);
        
    }

    @Test
    public void testJ25_MaximumActualAndPlannedUnits() {
        double actual = machine.performanceCalc(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(100.0, actual, 0.001);
    }

    @Test
    public void testJ26_LargeActualWithSmallTarget() {
        double actual = machine.performanceCalc(Integer.MAX_VALUE, 1);
        assertEquals(214748364700.0, actual, 0.001);
    }
    
    // needsMaintenance
    // (int usageMinutes, int serviceInterval, int failures)

    @Test
    public void testJ27_NoMaintenanceNeeded() {
        assertFalse(machine.needsMaintenance(30, 120, 0));
        
    }

    @Test
    public void testJ28_JustBeforeServiceInterval() {
        
        assertFalse(machine.needsMaintenance(119, 120, 0));
    }

    @Test
    public void testJ29_MaintenanceAtServiceInterval() {
        assertTrue(machine.needsMaintenance(120, 120, 0));
        
    }

    @Test
    public void testJ30_AfterServiceInterval() {
        assertTrue(machine.needsMaintenance(121, 120, 0));
    }

    @Test
    public void testJ31_FailuresBelowThreshold() {
        assertFalse(machine.needsMaintenance(30, 120, 2));
    }

    @Test
    public void testJ32_FailuresAtThreshold() {
        assertTrue(machine.needsMaintenance(30, 120, 3));
    }

    @Test
    public void testJ33_FailuresAboveThreshold() {
        assertTrue(machine.needsMaintenance(30, 120, 4));
    }

    @Test
    public void testJ34_BothConditionsRequireMaintenance() {
        assertTrue(machine.needsMaintenance(121, 120, 3));
    }

    @Test
    public void testJ35_ZeroUsageAndNoFailures() {
        assertFalse(machine.needsMaintenance(0, 120, 0));
    }

    @Test
    public void testJ36_FailuresRequireMaintenanceWithoutUsage() {
        
        assertTrue(machine.needsMaintenance(0, 120, 3));
    }

    @Test
    public void testJ37_MinimumPositiveServiceInterval() {
        assertFalse(machine.needsMaintenance(0, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ38_RejectNegativeUsageMinutes() {
        machine.needsMaintenance(-1, 120, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ39_RejectZeroServiceInterval() {
        
        
        machine.needsMaintenance(30, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ40_RejectNegativeServiceInterval() {
        machine.needsMaintenance(30, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ41_RejectNegativeFailures() {
        machine.needsMaintenance(30, 120, -1);
        
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ42_RejectNegativeUsageEvenWithFailures() {
        
        machine.needsMaintenance(-1, 120, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJ43_RejectNegativeFailuresEvenWhenServiceOverdue() {
        
        machine.needsMaintenance(121, 120, -1);
    }

}