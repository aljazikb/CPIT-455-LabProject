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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author aljaz
 */
public class ProductionManagementTest {
    
    private ProductionManagement production;
    private StockManagement stock;
    private MachineOperation machine;

    
    public ProductionManagementTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        stock = mock(StockManagement.class);
        machine = mock(MachineOperation.class);

        production = new ProductionManagement(stock, machine);
    }
    
    @After
    public void tearDown() {
    }
    
    
    
    //calculateProductionCost
    // (int units, double materialCost, double laborCost, double setupCost, int discountPercent) 

    @Test
    public void testAJ01_CostWithoutDiscount() {
        double actual =production.calculateProductionCost(10, 2, 3, 20, 0);
        assertEquals(70.0, actual, 0.001);
    }

    
    @Test
    public void testAJ02_CostWithDecimalAmounts() {
        double actual =production.calculateProductionCost( 3, 2.50, 1.25, 4.50, 0);

        assertEquals(15.75, actual, 0.001);
    }

  
    @Test
    public void testAJ03_DiscountWithoutSetupCost() {
        double actual =production.calculateProductionCost(10, 2, 3, 0, 10);
        assertEquals(45.0, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ04_RejectZeroUnits() {
        production.calculateProductionCost(0, 2, 3, 20, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ05_RejectNegativeMaterialCost() {
        production.calculateProductionCost(10, -2, 3, 20, 0);
    }

    @Test
    public void testAJ06_DiscountExcludesSetupCost() {
         assertEquals(65.0,production.calculateProductionCost(10, 2, 3, 20, 10),0.001);
     }

    @Test
    public void testAJ07_FullDiscountReturnsZero() {
        
        assertEquals(0.0, production.calculateProductionCost(10, 2, 3, 20, 100), 0.001);
    }

    @Test
    public void testAJ08_MinimumOneUnit() {
        
        assertEquals(25.0, production.calculateProductionCost(1, 2, 3, 20, 0), 0.001);
    }

    @Test
    public void testAJ09_ZeroCostsAccepted() {
        
        assertEquals(0.0, production.calculateProductionCost(5, 0, 0, 0, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ10_RejectNegativeUnits() {
        production.calculateProductionCost(-1, 2, 3, 20, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ11_RejectNegativeLaborCost() {
        production.calculateProductionCost(10, 2, -3, 20, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ12_RejectNegativeSetupCost() {
        production.calculateProductionCost(10, 2, 3, -20, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ13_RejectNegativeDiscount() {
        
        production.calculateProductionCost(10, 2, 3, 20, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ14_RejectDiscountAbove100() {
        
        production.calculateProductionCost(10, 2, 3, 20, 101);
    }
    

   
    
    // planProduction
   // (int units, int batchCapacity)

    @Test
    public void testAJ15_ExactBatches() {
        int[] actual = production.planProduction(20, 10);

        assertArrayEquals(new int[]{10, 10}, actual);
    }

    @Test
    public void testAJ16_BatchesWithRemainder() {
        int[] actual = production.planProduction(23, 10);

        assertArrayEquals(new int[]{10, 10, 3}, actual);
    }

    @Test
    public void testAJ17_UnitsLessThanCapacity() {
        int[] actual = production.planProduction(5, 10);

        assertArrayEquals(new int[]{5}, actual);
    }

    @Test
    public void testAJ18_UnitsEqualCapacity() {
        int[] actual = production.planProduction(10, 10);
        assertArrayEquals(new int[]{10}, actual);
    }

    @Test
    public void testAJ19_UnitsJustAboveCapacity() {
        int[] actual = production.planProduction(11, 10);
        assertArrayEquals(new int[]{10, 1}, actual);
    }

    @Test
    public void testAJ20_MinimumOneUnit() {
        int[] actual = production.planProduction(1, 10);
        assertArrayEquals(new int[]{1}, actual);
    }

    @Test
    public void testAJ21_CapacityOfOne() {
        int[] actual = production.planProduction(3, 1);

        assertArrayEquals(new int[]{1, 1, 1}, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ22_RejectZeroUnits() {
        production.planProduction(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ23_RejectNegativeUnits() {
        production.planProduction(-1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ24_RejectZeroCapacity() {
        production.planProduction(10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ25_RejectNegativeCapacity() {
        production.planProduction(10, -1);
    }

    @Test
    public void testAJ26_MaximumAllowedBatches() {
        int[] actual = production.planProduction(10000, 1);
        assertEquals(10000, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(1, actual[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ27_RejectTooManyBatches() {
        production.planProduction(10001, 1);
    }
    
    
    // processProduction
    // (int units, int materialPerUnit, boolean workerBusy, boolean machineBusy, int usageMinutes,int serviceInterval, int failures)

    @Test
    public void testAJ28_SuccessfulProduction() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);
        when(stock.decreaseStock(20)).thenReturn(true);

        boolean actual = production.processProduction(10, 2, false, false, 30, 120, 0);
        assertTrue(actual);
        assertEquals(10, production.getTotalProduced());
        verify(stock).calculateRequiredMaterial(10, 2, 0);
        verify(machine).needsMaintenance(30, 120, 0);
        verify(stock).decreaseStock(20);
    }

    @Test
    public void testAJ29_RejectBusyWorker() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);

        boolean actual = production.processProduction(10, 2, true, false, 30, 120, 0);
        assertFalse(actual);
        assertEquals(0, production.getTotalProduced());
        verify(stock, never()).decreaseStock(anyInt());
    }

    @Test
    public void testAJ30_RejectBusyMachine() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);
        boolean actual = production.processProduction(10, 2, false, true, 30, 120, 0);
        assertFalse(actual);
        assertEquals(0, production.getTotalProduced());
        verify(stock, never()).decreaseStock(anyInt());
    }

    @Test
    public void testAJ31_RejectMachineNeedingMaintenance() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(120, 120, 0)).thenReturn(true);

        boolean actual = production.processProduction(10, 2, false, false, 120, 120, 0);
        assertFalse(actual);
        assertEquals(0, production.getTotalProduced());
        verify(stock, never()).decreaseStock(anyInt());
    }

    @Test
    public void testAJ32_RejectInsufficientStock() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);
        when(stock.decreaseStock(20)).thenReturn(false);

        boolean actual = production.processProduction(10, 2, false, false, 30, 120, 0);
        assertFalse(actual);
        assertEquals(0, production.getTotalProduced());
        verify(stock).decreaseStock(20);
    }

    @Test
    public void testAJ33_UsesCalculatedMaterialDemand() {

        when(stock.calculateRequiredMaterial(5, 3, 0)).thenReturn(15);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);
        when(stock.decreaseStock(15)).thenReturn(true);

        boolean actual = production.processProduction(5, 3, false, false, 30, 120, 0);

        assertTrue(actual);
        assertEquals(5, production.getTotalProduced());

        verify(stock).decreaseStock(15);
    }

    @Test
    public void testAJ34_TwoSuccessfulOrders() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(stock.calculateRequiredMaterial(5, 2, 0)).thenReturn(10);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);

        when(stock.decreaseStock(20)).thenReturn(true);
        when(stock.decreaseStock(10)).thenReturn(true);

        assertTrue(production.processProduction(10, 2, false, false, 30, 120, 0));
        assertTrue(production.processProduction(5, 2, false, false, 30, 120, 0));
        assertEquals(15, production.getTotalProduced());

        verify(stock).decreaseStock(20);
        verify(stock).decreaseStock(10);
    }

    @Test
    public void testAJ35_RejectedOrderPreservesPreviousTotal() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(stock.calculateRequiredMaterial(5, 2, 0)).thenReturn(10);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);

        when(stock.decreaseStock(20)).thenReturn(true);
        when(stock.decreaseStock(10)).thenReturn(false);

        assertTrue(production.processProduction(10, 2, false, false, 30, 120, 0 ));
        assertFalse(production.processProduction( 5, 2, false, false, 30, 120, 0));
        assertEquals(10, production.getTotalProduced());
    }

    @Test
    public void testAJ36_SuccessAfterRejectedOrder() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);

        when(stock.decreaseStock(20)).thenReturn(false, true);

        assertFalse(production.processProduction(10, 2, false, false, 30, 120, 0));
        assertEquals(0, production.getTotalProduced());
        assertTrue(production.processProduction(10, 2, false, false, 30, 120, 0));
        assertEquals(10, production.getTotalProduced());

        verify(stock,times(2)).decreaseStock(20);
    }

    @Test
    public void testAJ37_MaterialCalculationException() {
        when(stock.calculateRequiredMaterial(0, 2, 0)).thenThrow(new IllegalArgumentException("Invalid units"));

        try {
            production.processProduction(0, 2, false, false, 30, 120, 0);
            fail("Expected IllegalArgumentException");

        } catch (IllegalArgumentException error) {
            assertEquals(0, production.getTotalProduced());
            verify(machine, never()).needsMaintenance(anyInt(), anyInt(), anyInt());
            verify(stock, never()).decreaseStock(anyInt());
        }
    }

    @Test
    public void testAJ38_MaintenanceCheckException() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(-1, 120, 0)).thenThrow(new IllegalArgumentException("Invalid usage"));
        try {
            production.processProduction(10, 2, false, false, -1, 120, 0);
            fail("Expected IllegalArgumentException");

        } catch (IllegalArgumentException error) {
            assertEquals(0, production.getTotalProduced());
            verify(stock, never()).decreaseStock(anyInt());
        }
    }

    @Test
    public void testAJ39_StockExceptionDoesNotRecordProduction() {
        when(stock.calculateRequiredMaterial(10, 2, 0)).thenReturn(20);
        when(machine.needsMaintenance(30, 120, 0)).thenReturn(false);
        when(stock.decreaseStock(20)).thenThrow(new IllegalStateException("Stock operation failed"));

        try {
            production.processProduction(10, 2, false, false, 30, 120, 0);
            fail("Expected IllegalStateException");

        } catch (IllegalStateException error) {
            assertEquals(0, production.getTotalProduced());
            verify(stock).decreaseStock(20);
        }
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAJ40_RejectNullStock() {
        new ProductionManagement(null, machine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAJ41_RejectNullMaؤحخchine() {
        new ProductionManagement(stock, null);
    }

    
}

    
    

