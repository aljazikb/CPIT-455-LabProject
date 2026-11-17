package project_cpit305;

import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.SwingUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StockManagementTest {

    private static final String NAMES_SQL =
            "SELECT ItemName FROM InventoryManagementTable ORDER BY ItemName";

    private static final String MAX_SQL =
            "SELECT MAX(ItemID) FROM InventoryManagementTable";

    private static final String INSERT_SQL =
            "INSERT INTO InventoryManagementTable (ItemID, ItemName, Quantity) VALUES (?, ?, ?)";

    private static final String DECREASE_SQL =
            "UPDATE InventoryManagementTable SET Quantity = Quantity - ? "
            + "WHERE MachineID = ? AND Quantity >= ?";

    private static final String STATUS_SQL =
            "SELECT Quantity FROM InventoryManagementTable WHERE LOWER(ItemName)=LOWER(?)";

    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet result;
    private TestDriver driver;

    private final ArrayList<Driver> savedDrivers = new ArrayList<>();

    private PrintStream originalOut;
    private PrintStream testOut;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        preparedStatement = mock(PreparedStatement.class);
        result = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(NAMES_SQL)).thenReturn(result);
        when(statement.executeQuery(MAX_SQL)).thenReturn(result);

        when(connection.prepareStatement(INSERT_SQL))
                .thenReturn(preparedStatement);

        when(connection.prepareStatement(DECREASE_SQL))
                .thenReturn(preparedStatement);

        when(connection.prepareStatement(STATUS_SQL))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(result);

        // Temporarily remove matching drivers in this test process.
        // This prevents an installed MySQL driver from opening a real connection.
        for (Driver registered : Collections.list(DriverManager.getDrivers())) {
            if (registered.acceptsURL(TestDriver.URL)) {
                savedDrivers.add(registered);
                DriverManager.deregisterDriver(registered);
            }
        }

        driver = new TestDriver(connection);
        DriverManager.registerDriver(driver);

        originalOut = System.out;
        output = new ByteArrayOutputStream();
        testOut = new PrintStream(output);
        System.setOut(testOut);
    }

    @After
    public void tearDown() throws Exception {
        try {
            if (driver != null) {
                DriverManager.deregisterDriver(driver);
            }

            for (Driver saved : savedDrivers) {
                DriverManager.registerDriver(saved);
            }
        } finally {
            if (originalOut != null) {
                System.setOut(originalOut);
            }

            if (testOut != null) {
                testOut.close();
            }

            SwingUtilities.invokeAndWait(() -> {
                for (Window window : Window.getWindows()) {
                    window.dispose();
                }
            });
        }
    }

    private void prepareInsert(int maxID) throws SQLException {
        when(result.next()).thenReturn(true);
        when(result.getInt(1)).thenReturn(maxID);
        when(preparedStatement.executeUpdate()).thenReturn(1);
    }

    private void prepareQuantity(int quantity) throws SQLException {
        when(result.next()).thenReturn(true);
        when(result.getInt("Quantity")).thenReturn(quantity);
    }

    // getItemNames

    @Test
    public void testG01_EmptyTable() throws Exception {
        when(result.next()).thenReturn(false);

        assertTrue(StockManagement.getItemNames().isEmpty());

        verify(result).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    public void testG02_OneItem() throws Exception {
        when(result.next()).thenReturn(true, false);
        when(result.getString("ItemName")).thenReturn("bolts");

        assertEquals(
                Arrays.asList("bolts"),
                StockManagement.getItemNames()
        );
    }

    @Test
    public void testG03_MultipleItems() throws Exception {
        when(result.next()).thenReturn(true, true, true, false);

        when(result.getString("ItemName"))
                .thenReturn("bolts", "fiber cables", "wires");

        assertEquals(
                Arrays.asList("bolts", "fiber cables", "wires"),
                StockManagement.getItemNames()
        );

        verify(statement).executeQuery(NAMES_SQL);
        verify(result).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    public void testG04_ConnectionFailure() throws Exception {
        driver.failConnection();

        assertTrue(StockManagement.getItemNames().isEmpty());

        verify(connection, never()).createStatement();
    }

    @Test
    public void testG05_StatementFailure() throws Exception {
        when(connection.createStatement())
                .thenThrow(new SQLException("Statement failed"));

        assertTrue(StockManagement.getItemNames().isEmpty());

        verify(statement, never()).executeQuery(NAMES_SQL);
    }

    @Test
    public void testG06_QueryFailure() throws Exception {
        when(statement.executeQuery(NAMES_SQL))
                .thenThrow(new SQLException("Query failed"));

        assertTrue(StockManagement.getItemNames().isEmpty());
    }

    @Test
    public void testG07_ReadFailureAfterOneItem() throws Exception {
        when(result.next())
                .thenReturn(true)
                .thenThrow(new SQLException("Read failed"));

        when(result.getString("ItemName")).thenReturn("bolts");

        assertTrue(StockManagement.getItemNames().isEmpty());
    }

    @Test
    public void testG08_CloseConnectionAfterQueryFailure() throws Exception {
        when(statement.executeQuery(NAMES_SQL))
                .thenThrow(new SQLException("Query failed"));

        StockManagement.getItemNames();

        verify(connection).close();
    }

    @Test
    public void testG09_CurrentBehavior_NullItemName() throws Exception {
        when(result.next()).thenReturn(true, false);
        when(result.getString("ItemName")).thenReturn(null);

        ArrayList<String> items = StockManagement.getItemNames();

        assertEquals(1, items.size());
        assertNull(items.get(0));
    }

    // addNewMaterial

    @Test
    public void testA01_AddMaterial() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("bolts", 20);

        verify(preparedStatement).setInt(1, 8);
        verify(preparedStatement).setString(2, "bolts");
        verify(preparedStatement).setInt(3, 20);
        verify(preparedStatement).executeUpdate();

        verify(result).close();
        verify(statement).close();
        verify(preparedStatement).close();
        verify(connection).close();

        assertTrue(
                output.toString().contains(
                        "Material added successfully: bolts"
                )
        );
    }

    @Test
    public void testA02_EmptyTableStartsAtOne() throws Exception {
        // MAX returns one NULL row for an empty table; getInt then returns 0.
        prepareInsert(0);

        StockManagement.addNewMaterial("bolts", 20);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA03_QuantityOne() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("bolts", 1);

        verify(preparedStatement).setInt(3, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA04_QuantityZero() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("bolts", 0);

        verify(preparedStatement).setInt(3, 0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA05_MaximumIntQuantity() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("bolts", Integer.MAX_VALUE);

        verify(preparedStatement).setInt(3, Integer.MAX_VALUE);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA06_NameWithSeveralWords() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("fiber cables", 20);

        verify(preparedStatement).setString(2, "fiber cables");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA07_ConnectionFailure() throws Exception {
        driver.failConnection();

        StockManagement.addNewMaterial("bolts", 20);

        verify(connection, never()).createStatement();

        assertTrue(
                output.toString().contains(
                        "Error adding material: Connection failed"
                )
        );
    }

    @Test
    public void testA08_MaxQueryFailure() throws Exception {
        when(statement.executeQuery(MAX_SQL))
                .thenThrow(new SQLException("MAX failed"));

        StockManagement.addNewMaterial("bolts", 20);

        verify(connection, never()).prepareStatement(INSERT_SQL);
        verify(connection).close();
    }

    @Test
    public void testA09_PrepareInsertFailure() throws Exception {
        prepareInsert(7);

        when(connection.prepareStatement(INSERT_SQL))
                .thenThrow(new SQLException("Prepare failed"));

        StockManagement.addNewMaterial("bolts", 20);

        verify(preparedStatement, never()).executeUpdate();
        verify(connection).close();
    }

    @Test
    public void testA10_InsertFailure() throws Exception {
        prepareInsert(7);

        when(preparedStatement.executeUpdate())
                .thenThrow(new SQLException("Insert failed"));

        StockManagement.addNewMaterial("bolts", 20);

        verify(connection).close();

        assertTrue(
                output.toString().contains(
                        "Error adding material: Insert failed"
                )
        );

        assertFalse(
                output.toString().contains(
                        "Material added successfully"
                )
        );
    }

    @Test
    public void testA11_UnexpectedEmptyMaxResult() throws Exception {
        // Defensive branch: this is NOT the usual SQL MAX result on an empty table.
        when(result.next()).thenReturn(false);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        StockManagement.addNewMaterial("bolts", 20);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA12_CurrentBehavior_EmptyName() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("", 20);

        verify(preparedStatement).setString(2, "");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA13_CurrentBehavior_NullName() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial(null, 20);

        verify(preparedStatement).setString(2, null);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA14_CurrentBehavior_NegativeQuantity() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial("bolts", -1);

        verify(preparedStatement).setInt(3, -1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testA15_ZeroInsertedRowsMustNotReportSuccess() throws Exception {
        prepareInsert(7);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        StockManagement.addNewMaterial("bolts", 20);

        verify(connection).close();

        assertFalse(
                "No row was inserted, so a success message is misleading",
                output.toString().contains("Material added successfully")
        );
    }

    @Test
    public void testA16_CurrentBehavior_NameSpacesArePreserved() throws Exception {
        prepareInsert(7);

        StockManagement.addNewMaterial(" bolts ", 20);

        verify(preparedStatement).setString(2, " bolts ");
        verify(preparedStatement).executeUpdate();
    }

    // decreaseStock

    @Test
    public void testD01_SuccessfulUpdate() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(StockManagement.decreaseStock(1, 5));

        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).setInt(3, 5);

        InOrder order = inOrder(connection, preparedStatement);

        order.verify(connection).setAutoCommit(false);
        order.verify(preparedStatement).executeUpdate();
        order.verify(connection).commit();
        order.verify(preparedStatement).close();
        order.verify(connection).close();

        verify(connection, never()).rollback();
    }

    @Test
    public void testD02_MultipleUpdatedRows() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(2);

        assertTrue(StockManagement.decreaseStock(1, 5));

        verify(connection).commit();
    }

    @Test
    public void testD03_NoUpdatedRows() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection).rollback();
        verify(connection, never()).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void testD04_ConnectionFailure() throws Exception {
        driver.failConnection();

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection, never()).prepareStatement(DECREASE_SQL);
    }

    @Test
    public void testD05_PrepareFailure() throws Exception {
        when(connection.prepareStatement(DECREASE_SQL))
                .thenThrow(new SQLException("Prepare failed"));

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection).close();
        verify(connection, never()).commit();
    }

    @Test
    public void testD06_BeginTransactionFailure() throws Exception {
        doThrow(new SQLException("Begin failed"))
                .when(connection).setAutoCommit(false);

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(preparedStatement, never()).executeUpdate();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void testD07_UpdateFailureReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate())
                .thenThrow(new SQLException("Update failed"));

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection, never()).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void testD08_CommitFailureReturnsFalse() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        doThrow(new SQLException("Commit failed"))
                .when(connection).commit();

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection).close();
    }

    @Test
    public void testD09_RollbackAfterUpdateFailure() throws Exception {
        when(preparedStatement.executeUpdate())
                .thenThrow(new SQLException("Update failed"));

        assertFalse(StockManagement.decreaseStock(1, 5));

        InOrder order = inOrder(connection);

        order.verify(connection).rollback();
        order.verify(connection).close();
    }

    @Test
    public void testD10_RollbackAfterCommitFailure() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        doThrow(new SQLException("Commit failed"))
                .when(connection).commit();

        assertFalse(StockManagement.decreaseStock(1, 5));

        InOrder order = inOrder(connection);

        order.verify(connection).rollback();
        order.verify(connection).close();
    }

    @Test
    public void testD11_RollbackFailureStillClosesResources() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        doThrow(new SQLException("Rollback failed"))
                .when(connection).rollback();

        assertFalse(StockManagement.decreaseStock(1, 5));

        verify(connection, never()).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void testD12_CurrentBehavior_ZeroUnits() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(StockManagement.decreaseStock(1, 0));

        verify(preparedStatement).setInt(1, 0);
        verify(preparedStatement).setInt(3, 0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testD13_CurrentBehavior_NegativeUnits() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(StockManagement.decreaseStock(1, -1));

        verify(preparedStatement).setInt(1, -1);
        verify(preparedStatement).setInt(3, -1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testD14_MaximumIntUnits() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(
                StockManagement.decreaseStock(1, Integer.MAX_VALUE)
        );

        verify(preparedStatement).setInt(1, Integer.MAX_VALUE);
        verify(preparedStatement).setInt(3, Integer.MAX_VALUE);
    }

    @Test
    public void testD15_NegativeIdWithNoMatchingRows() throws Exception {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        assertFalse(StockManagement.decreaseStock(-1, 5));

        verify(preparedStatement).setInt(2, -1);
        verify(connection).rollback();
    }

    // checkStatus

    @Test
    public void testC01_BelowLimit() throws Exception {
        prepareQuantity(5);

        assertEquals(
                "LOW STOCK (Qty: 5)",
                StockManagement.checkStatus("bolts", 10)
        );

        verify(result).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void testC02_JustBelowLimit() throws Exception {
        prepareQuantity(9);

        assertEquals(
                "LOW STOCK (Qty: 9)",
                StockManagement.checkStatus("bolts", 10)
        );
    }

    @Test
    public void testC03_AtLimit() throws Exception {
        prepareQuantity(10);

        assertEquals(
                "STOCK OK (Qty: 10)",
                StockManagement.checkStatus("bolts", 10)
        );
    }

    @Test
    public void testC04_JustAboveLimit() throws Exception {
        prepareQuantity(11);

        assertEquals(
                "STOCK OK (Qty: 11)",
                StockManagement.checkStatus("bolts", 10)
        );
    }

    @Test
    public void testC05_ZeroQuantityBelowPositiveLimit() throws Exception {
        prepareQuantity(0);

        assertEquals(
                "LOW STOCK (Qty: 0)",
                StockManagement.checkStatus("bolts", 1)
        );
    }

    @Test
    public void testC06_ZeroQuantityAndZeroLimit() throws Exception {
        prepareQuantity(0);

        assertEquals(
                "STOCK OK (Qty: 0)",
                StockManagement.checkStatus("bolts", 0)
        );
    }

    @Test
    public void testC07_MaximumIntBoundary() throws Exception {
        prepareQuantity(Integer.MAX_VALUE);

        assertEquals(
                "STOCK OK (Qty: 2147483647)",
                StockManagement.checkStatus("bolts", Integer.MAX_VALUE)
        );
    }

    @Test
    public void testC08_MissingItem() throws Exception {
        when(result.next()).thenReturn(false);

        assertEquals(
                "ITEM NOT FOUND",
                StockManagement.checkStatus("missing", 10)
        );
    }

    @Test
    public void testC09_CloseConnectionForMissingItem() throws Exception {
        when(result.next()).thenReturn(false);

        assertEquals(
                "ITEM NOT FOUND",
                StockManagement.checkStatus("missing", 10)
        );

        verify(connection).close();
    }

    @Test
    public void testC10_TrimName() throws Exception {
        prepareQuantity(5);

        assertEquals(
                "LOW STOCK (Qty: 5)",
                StockManagement.checkStatus(" bolts ", 10)
        );

        verify(preparedStatement).setString(1, "bolts");
    }

    @Test
    public void testC11_CurrentBehavior_WhitespaceName() throws Exception {
        when(result.next()).thenReturn(false);

        assertEquals(
                "ITEM NOT FOUND",
                StockManagement.checkStatus("   ", 10)
        );

        verify(preparedStatement).setString(1, "");
    }

    @Test
    public void testC12_CurrentBehavior_EmptyName() throws Exception {
        when(result.next()).thenReturn(false);

        assertEquals(
                "ITEM NOT FOUND",
                StockManagement.checkStatus("", 10)
        );

        verify(preparedStatement).setString(1, "");
    }

    @Test
    public void testC13_QueryFailure() throws Exception {
        when(preparedStatement.executeQuery())
                .thenThrow(new SQLException("Query failed"));

        assertEquals(
                "DB ERROR: Query failed",
                StockManagement.checkStatus("bolts", 10)
        );
    }

    @Test
    public void testC14_ConnectionFailure() throws Exception {
        driver.failConnection();

        assertEquals(
                "DB ERROR: Connection failed",
                StockManagement.checkStatus("bolts", 10)
        );

        verify(connection, never()).prepareStatement(STATUS_SQL);
    }

    @Test
    public void testC15_CloseConnectionAfterQueryFailure() throws Exception {
        when(preparedStatement.executeQuery())
                .thenThrow(new SQLException("Query failed"));

        StockManagement.checkStatus("bolts", 10);

        verify(connection).close();
    }

    @Test
    public void testC16_PrepareFailure() throws Exception {
        when(connection.prepareStatement(STATUS_SQL))
                .thenThrow(new SQLException("Prepare failed"));

        assertEquals(
                "DB ERROR: Prepare failed",
                StockManagement.checkStatus("bolts", 10)
        );

        verify(preparedStatement, never()).executeQuery();
    }

    @Test
    public void testC17_CurrentBehavior_NullName() throws Exception {
        String actual = StockManagement.checkStatus(null, 10);

        assertTrue(actual.startsWith("DB ERROR: "));

        verify(preparedStatement, never()).executeQuery();
    }

    @Test
    public void testC18_CloseConnectionAfterNullName() throws Exception {
        StockManagement.checkStatus(null, 10);

        verify(connection).close();
    }

    @Test
    public void testC19_CurrentBehavior_NegativeLimit() throws Exception {
        prepareQuantity(0);

        assertEquals(
                "STOCK OK (Qty: 0)",
                StockManagement.checkStatus("bolts", -1)
        );
    }

    @Test
    public void testC20_CurrentBehavior_NegativeStoredQuantity() throws Exception {
        prepareQuantity(-1);

        assertEquals(
                "LOW STOCK (Qty: -1)",
                StockManagement.checkStatus("bolts", 0)
        );
    }

    @Test
    public void testC21_ReadQuantityFailure() throws Exception {
        when(result.next()).thenReturn(true);

        when(result.getInt("Quantity"))
                .thenThrow(new SQLException("Read failed"));

        assertEquals(
                "DB ERROR: Read failed",
                StockManagement.checkStatus("bolts", 10)
        );
    }

    @Test
    public void testC22_ReadNextFailure() throws Exception {
        when(result.next())
                .thenThrow(new SQLException("Next failed"));

        assertEquals(
                "DB ERROR: Next failed",
                StockManagement.checkStatus("bolts", 10)
        );
    }
}