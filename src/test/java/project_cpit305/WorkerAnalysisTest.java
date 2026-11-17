package project_cpit305;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkerAnalysisTest {

    private WorkerAnalysis worker;
    private Map<String, Double> scores;

    @Before
    public void setUp() {
        worker = new WorkerAnalysis();
         

        scores = new LinkedHashMap<String, Double>();
        scores.put("Nora", 70.0);
        scores.put("Sara", 90.0);
        scores.put("Mona", 80.0);
    }

    //calculateEfficiency
    //(int produced, int rejected, int target)

    @Test
    public void testN01_TargetAchievedWithoutRejections() {
        double actual = worker.calculateEfficiency(100, 0, 100);
        assertEquals(100.0, actual, 0.001);}

    @Test
    public void testN02_RejectedUnitsReduceEfficiency() {
        double actual = worker.calculateEfficiency(100, 20, 100);
        
        assertEquals(80.0, actual, 0.001);
    }

    @Test
    public void testN03_OutputBelowTarget() {
        double actual = worker.calculateEfficiency(50, 0, 100);
        assertEquals(50.0, actual, 0.001);
    }

    @Test
    public void testN04_OutputAboveTarget() {
        double actual = worker.calculateEfficiency(150, 0, 100);
        assertEquals(150.0, actual, 0.001);}

    @Test
    public void testN05_FractionalEfficiency() {
        double actual = worker.calculateEfficiency(1, 0, 3);
        assertEquals(33.333333, actual, 0.000001);
    }

    @Test
    public void testN06_ZeroProduction() {
        double actual = worker.calculateEfficiency(0, 0, 100);
        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void testN07_AllUnitsRejected() {
        double actual = worker.calculateEfficiency(100, 100, 100);
        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void testN08_MinimumPositiveTarget() {
        double actual = worker.calculateEfficiency(1, 0, 1);
        assertEquals(100.0, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN09_RejectNegativeProduction() {
        worker.calculateEfficiency(-1, 0, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN10_RejectNegativeRejectedUnits() {
        worker.calculateEfficiency(100, -1, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN11_RejectRejectionsAboveProduction() {
        worker.calculateEfficiency(100, 101, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN12_RejectZeroTarget() {
        worker.calculateEfficiency(100, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN13_RejectNegativeTarget() {
        worker.calculateEfficiency(100, 0, -1);
    }

    @Test
    public void testN14_MaximumProductionAndTarget() {
        double actual = worker.calculateEfficiency(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        assertEquals(100.0, actual, 0.001); }

    @Test
    public void testN15_LargeProductionWithSmallTarget() {
        double actual = worker.calculateEfficiency(Integer.MAX_VALUE, 0, 1);
        assertEquals(214748364700.0, actual, 0.001);
    }
    
    // calculateBonus
    //(long basePayCents, double efficiency)

    @Test
    public void testN16_NoBonusBelow100Percent() {
        long actual = worker.calculateBonus(10000L, 99.99);
        assertEquals(0L, actual);}

    @Test
    public void testN17_BonusAtExactly100Percent() {
        long actual = worker.calculateBonus(10000L, 100.0);
        assertEquals(1000L, actual);}

    @Test
    public void testN18_BonusJustAbove100Percent() {
        long actual = worker.calculateBonus(10000L, 100.01);
        assertEquals(1000L, actual);}

    @Test
    public void testN19_BonusJustBelow120Percent() {
        long actual = worker.calculateBonus(10000L, 119.99);
        assertEquals(1000L, actual);}

    @Test
    public void testN20_BonusAtExactly120Percent() {
        long actual = worker.calculateBonus(10000L, 120.0);
        assertEquals(2000L, actual);}

    @Test
    public void testN21_BonusAbove120Percent() {
        long actual = worker.calculateBonus(10000L, 150.0);
        assertEquals(2000L, actual);}

    @Test
    public void testN22_ZeroEfficiency() {
        long actual = worker.calculateBonus(10000L, 0.0);
        assertEquals(0L, actual);}

    @Test
    public void testN23_ZeroBasePay() {
        long actual = worker.calculateBonus(0L, 120.0);
        assertEquals(0L, actual);
    }

    @Test
    public void testN24_DiscardFractionalCentAt10Percent() {
        long actual = worker.calculateBonus(105L, 110.0);
        assertEquals(10L, actual);
    }

    @Test
    public void testN25_DiscardFractionalCentAt20Percent() {
        long actual = worker.calculateBonus(109L, 120.0);
        assertEquals(21L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN26_RejectNegativeBasePay() {
        worker.calculateBonus(-1L, 120.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN27_RejectNegativeEfficiency() {
        worker.calculateBonus(10000L, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN28_RejectNaNEfficiency() {
        worker.calculateBonus(10000L, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN29_RejectPositiveInfinity() {
        worker.calculateBonus(10000L, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN30_RejectNegativeInfinity() {
        worker.calculateBonus(10000L, Double.NEGATIVE_INFINITY);
    }
    
    // rankWorkers
    //(Map<String, Double> scores, int limit)

    @Test
    public void testN31_RankByScoreDescending() {
        List<String> actual = worker.rankWorkers(scores, 3);
        assertEquals(Arrays.asList("Sara", "Mona", "Nora"),actual);}

    @Test
    public void testN32_ReturnOnlyTopTwoWorkers() {
        List<String> actual = worker.rankWorkers(scores, 2);
        assertEquals(Arrays.asList("Sara", "Mona"), actual); }

    @Test
    public void testN33_LimitAboveNumberOfWorkers() {
        List<String> actual = worker.rankWorkers(scores, 5);
        assertEquals(Arrays.asList("Sara", "Mona", "Nora"),actual);
    }

    @Test
    public void testN34_EmptyScores() {
        scores.clear();
        List<String> actual = worker.rankWorkers(scores, 3);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testN35_OneWorker() {
        scores.clear();
        scores.put("Sara", 90.0);
        List<String> actual = worker.rankWorkers(scores, 1);
        assertEquals(Arrays.asList("Sara"), actual);
    }

    @Test
    public void testN36_EqualScoresSortedAlphabetically() {
        scores.put("Nora", 80.0);
        scores.put("Sara", 80.0);
        List<String> actual = worker.rankWorkers(scores, 3);
        assertEquals(Arrays.asList("Mona", "Nora", "Sara"),actual);
    }

    @Test
    public void testN37_DecimalAndZeroScores() {
        scores.put("Nora", 0.0);
        scores.put("Sara", 80.5);
        scores.put("Mona", 80.9);
        List<String> actual = worker.rankWorkers(scores, 3);
        assertEquals(Arrays.asList("Mona", "Sara", "Nora"),actual);
    }

    @Test
    public void testN38_RankingDoesNotChangeInput() {
        Map<String, Double> originalScores =new LinkedHashMap<String, Double>(scores);

        List<String> originalOrder =new ArrayList<String>(scores.keySet());
        worker.rankWorkers(scores, 2);
        assertEquals(originalScores, scores);
        assertEquals(originalOrder,new ArrayList<String>(scores.keySet()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN39_RejectNullScores() {
        worker.rankWorkers(null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN40_RejectZeroLimit() {
        worker.rankWorkers(scores, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN41_RejectNegativeLimit() {
        worker.rankWorkers(scores, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN42_RejectNullName() {
        scores.put(null, 85.0);

        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN43_RejectEmptyName() {
        scores.put("", 85.0);
        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN44_RejectWhitespaceName() {
        scores.put("   ", 85.0);
        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN45_RejectNullScore() {
        scores.put("Sara", null);
        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN46_RejectNegativeScore() {
        scores.put("Sara", -1.0);
        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN47_RejectNaNScore() {
        scores.put("Sara", Double.NaN);

        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN48_RejectPositiveInfinityScore() {
        scores.put("Sara", Double.POSITIVE_INFINITY);
        worker.rankWorkers(scores, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testN49_RejectNegativeInfinityScore() {
        scores.put("Sara", Double.NEGATIVE_INFINITY);
        worker.rankWorkers(scores, 3);
    }

   
    
}