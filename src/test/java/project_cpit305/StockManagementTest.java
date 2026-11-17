package project_cpit305;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StockManagementTest {

    private StockManagement stock;
    @Before
    public void setUp() {
        stock = new StockManagement(100);
    }

    // calculateRequiredMaterial
    // (int units, int materialPerUnit, int wastePercent)

    @Test
    public void testAL01_MaterialWithoutWaste() {
        int actual = stock.calculateRequiredMaterial(10, 2, 0);
        
        assertEquals(20, actual);
    }

    @Test
    public void testAL02_WholeNumberWaste() {
        int actual = stock.calculateRequiredMaterial(10, 2, 10);
        assertEquals(22, actual);
    }

    @Test
    public void testAL03_RoundWasteUp() {
        int actual = stock.calculateRequiredMaterial(5, 3, 10);
        
        assertEquals(17, actual);
    }

    @Test
    public void testAL04_WasteLessThanOnePiece() {
        int actual = stock.calculateRequiredMaterial(1, 1, 1);
        assertEquals(2, actual);
    }

    @Test
    public void testAL05_FullWastePercentage() {
        int actual = stock.calculateRequiredMaterial(5, 3, 100);
        assertEquals(30, actual);
    }

    @Test
    public void testAL06_MinimumValidInputs() {
        int actual = stock.calculateRequiredMaterial(1, 1, 0);
        assertEquals(1, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL07_RejectZeroUnits() {
        stock.calculateRequiredMaterial(0, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL08_RejectNegativeUnits() {
        stock.calculateRequiredMaterial(-1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL09_RejectZeroMaterialPerUnit() {
        stock.calculateRequiredMaterial(10, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL10_RejectNegativeMaterialPerUnit() {
        stock.calculateRequiredMaterial(10, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL11_RejectNegativeWastePercentage() {
        stock.calculateRequiredMaterial(10, 2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL12_RejectWasteAbove100() {
        stock.calculateRequiredMaterial(10, 2, 101);
    }

    @Test
    public void testAL13_MaximumIntWithoutWaste() {
        int actual = stock.calculateRequiredMaterial(Integer.MAX_VALUE, 1, 0);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test(expected = ArithmeticException.class)
    public void testAL14_RejectBaseOverflow() {
        stock.calculateRequiredMaterial(Integer.MAX_VALUE, 2, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void testAL15_RejectOverflowAfterAddingWaste() {
        stock.calculateRequiredMaterial(Integer.MAX_VALUE, 1, 1);
    }

    @Test
    public void testAL16_TotalExactlyAtIntLimit() {
        int actual = stock.calculateRequiredMaterial(2126221432, 1, 1);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test
    public void testAL17_CalculationDoesNotChangeStock() {
        assertEquals(100, stock.getQuantity());
        int actual = stock.calculateRequiredMaterial(100, 2, 10);
        assertEquals(220, actual);
        assertEquals(100, stock.getQuantity());
    }
    
    // decreaseStock(int required)

    @Test
    public void testAL18_DecreaseStockSuccessfully() {
        boolean actual = stock.decreaseStock(30);
        assertTrue(actual);
        assertEquals(70, stock.getQuantity());
    }

    @Test
    public void testAL19_DecreaseExactlyAvailableStock() {
        boolean actual = stock.decreaseStock(100);
        assertTrue(actual);
        assertEquals(0, stock.getQuantity());
    }

    @Test
    public void testAL20_RejectInsufficientStock() {
        boolean actual = stock.decreaseStock(101);

        assertFalse(actual);
        assertEquals(100, stock.getQuantity());
    }

    @Test
    public void testAL21_DecreaseOnePiece() {
        boolean actual = stock.decreaseStock(1);
        assertTrue(actual);
        assertEquals(99, stock.getQuantity());
    }

    @Test
    public void testAL22_RejectZeroDemand() {
        try {
            stock.decreaseStock(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException error) {
            assertEquals(100, stock.getQuantity());
        }
    }

    @Test
    public void testAL23_RejectNegativeDemand() {
        try {
            stock.decreaseStock(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException error) {
            assertEquals(100, stock.getQuantity());
        }
    }

    @Test
    public void testAL24_RejectDemandWhenStockIsEmpty() {
        stock = new StockManagement(0);
        boolean actual = stock.decreaseStock(1);
        assertFalse(actual);
        assertEquals(0, stock.getQuantity());
    }

    @Test
    public void testAL25_TwoSuccessfulDecreases() {
        assertTrue(stock.decreaseStock(30));
        assertTrue(stock.decreaseStock(20));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    public void testAL26_RejectedDemandPreservesRemainingStock() {
        assertTrue(stock.decreaseStock(80));
        assertEquals(20, stock.getQuantity());
        assertFalse(stock.decreaseStock(21));
        assertEquals(20, stock.getQuantity());
    }

    @Test
    public void testAL27_SuccessAfterRejectedDemand() {
        assertFalse(stock.decreaseStock(101));
        assertEquals(100, stock.getQuantity());
        assertTrue(stock.decreaseStock(30));
        assertEquals(70, stock.getQuantity());
    }
    
    // checkStatus(int minimum)

    @Test
    public void testAL28_EmptyStock() {
        stock = new StockManagement(0);
        assertEquals("EMPTY", stock.checkStatus(10));
    }

    @Test
    public void testAL29_EmptyStockWithZeroMinimum() {
        stock = new StockManagement(0);
        assertEquals("EMPTY", stock.checkStatus(0));
    }

    @Test
    public void testAL30_StockBelowMinimum() {
        assertEquals("LOW STOCK", stock.checkStatus(101));
    }

    @Test
    public void testAL31_StockEqualsMinimum() {
        assertEquals("STOCK OK", stock.checkStatus(100));
    }

    @Test
    public void testAL32_StockAboveMinimum() {
        assertEquals("STOCK OK", stock.checkStatus(99));
    }

    @Test
    public void testAL33_PositiveStockWithZeroMinimum() {
        assertEquals("STOCK OK", stock.checkStatus(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL34_RejectNegativeMinimum() {
        stock.checkStatus(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAL35_RejectNegativeMinimumWhenEmpty() {
        stock = new StockManagement(0);
        stock.checkStatus(-1);
    }

    @Test
    public void testAL36_CheckStatusDoesNotChangeStock() {
        assertEquals("LOW STOCK", stock.checkStatus(101));
        assertEquals(100, stock.getQuantity());
    }

    @Test
    public void testAL37_StatusChangesAfterDecrease() {
        assertEquals("STOCK OK", stock.checkStatus(50));
        assertTrue(stock.decreaseStock(60));
        assertEquals("LOW STOCK", stock.checkStatus(50));
        assertTrue(stock.decreaseStock(40));
        assertEquals("EMPTY", stock.checkStatus(50));
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAL38_RejectNegativeInitialStock() {
        new StockManagement(-1);
    }
    
}