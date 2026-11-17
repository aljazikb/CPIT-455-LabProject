/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import java.util.ArrayList;
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
    
    public MachineOperationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMachineName method, of class MachineOperation.
     */
    @Test
    public void testGetMachineName() {
        System.out.println("getMachineName");
        ArrayList<String> expResult = null;
        ArrayList<String> result = MachineOperation.getMachineName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMachineID method, of class MachineOperation.
     */
    @Test
    public void testGetMachineID() {
        System.out.println("getMachineID");
        String machineName = "";
        int expResult = 0;
        int result = MachineOperation.getMachineID(machineName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMachine method, of class MachineOperation.
     */
    @Test
    public void testAddMachine() throws Exception {
        System.out.println("addMachine");
        String name = "";
        int expResult = 0;
        int result = MachineOperation.addMachine(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of runMachine method, of class MachineOperation.
     */
    @Test
    public void testRunMachine() {
        System.out.println("runMachine");
        int machineID = 0;
        String workerName = "";
        int runKey = 0;
        MachineOperation.runMachine(machineID, workerName, runKey);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMachineBusy method, of class MachineOperation.
     */
    @Test
    public void testIsMachineBusy() {
        System.out.println("isMachineBusy");
        int machineID = 0;
        boolean expResult = false;
        boolean result = MachineOperation.isMachineBusy(machineID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateUsageTime method, of class MachineOperation.
     */
    @Test
    public void testUpdateUsageTime() {
        System.out.println("updateUsageTime");
        int machineID = 0;
        int units = 0;
        MachineOperation.updateUsageTime(machineID, units);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of performanceCalc method, of class MachineOperation.
     */
    @Test
    public void testPerformanceCalc() {
        System.out.println("performanceCalc");
        String machineName = "";
        MachineOperation.MachineResult expResult = null;
        MachineOperation.MachineResult result = MachineOperation.performanceCalc(machineName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateMachineStatus method, of class MachineOperation.
     */
    @Test
    public void testUpdateMachineStatus() {
        System.out.println("updateMachineStatus");
        String machineName = "";
        String newStatus = "";
        MachineOperation.updateMachineStatus(machineName, newStatus);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
