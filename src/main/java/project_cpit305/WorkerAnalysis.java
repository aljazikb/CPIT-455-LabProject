/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_cpit305;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkerAnalysis {
    
    // Efficiency excludes rejected units and is relative to a positive target.
    public double calculateEfficiency(int produced, int rejected, int target) {
        if (produced < 0 || rejected < 0 || rejected > produced || target <= 0)
            throw new IllegalArgumentException("Invalid worker output");
        return (produced - rejected) * 100.0 / target;
    }

    // Pay in cents: 10% bonus at 100% efficiency; 20% at 120% or above.
    public long calculateBonus(long basePayCents, double efficiency) {
        if (basePayCents < 0 || !Double.isFinite(efficiency) || efficiency < 0)
            throw new IllegalArgumentException("Invalid bonus input");
        int rate = efficiency >= 120 ? 20 : efficiency > 100 ? 10 : 0;
        return Math.multiplyExact(basePayCents, rate) / 100;
    }

    // Rank by score descending; alphabetical name breaks ties. Input is not changed.
    public List<String> rankWorkers(Map<String, Double> scores, int limit) {
        if (scores == null || limit <= 0) throw new IllegalArgumentException("Invalid ranking");
        final Map<String, Double> copy = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()
                    || entry.getValue() == null || !Double.isFinite(entry.getValue()) || entry.getValue() < 0)
                throw new IllegalArgumentException("Invalid score");
            copy.put(entry.getKey(), entry.getValue());
        }
        
        List<String> names = new ArrayList<String>(copy.keySet());
        Collections.sort(names, new Comparator<String>() {
            public int compare(String a, String b) {
                int order = Double.compare(copy.get(b), copy.get(a));
                return order == 0 ? a.compareTo(b) : order;
            }
        });
        return new ArrayList<String>(names.subList(0, Math.min(limit, names.size())));
    }
}
