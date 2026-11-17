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
    
  
    public double calculateEfficiency(int produced, int rejected, int target) {
        
        if (produced < 0 || rejected < 0 || rejected > produced || target <= 0){
            throw new IllegalArgumentException("Invalid worker output");
        }
        return (int)((produced - rejected) * 100.0 / target);
    }

  
    public long calculateBonus(long basePayCents, double efficiency) {

      if (basePayCents < 0|| !Double.isFinite(efficiency)|| efficiency < 0) {
          throw new IllegalArgumentException("Invalid bonus input");
      }

      int rate = 0;

      if (efficiency >= 120) {
          rate = 20;
      } else if (efficiency > 100) {
          rate = 10; }
      
      return (basePayCents / 100) * rate + ((basePayCents % 100) * rate) / 100;
  }

    public List<String> rankWorkers(Map<String, Double> scores, int limit) {

     if (scores == null || limit <= 0) {
         throw new IllegalArgumentException("Invalid ranking");
     }
     
     // Validate names and scores.
    List<String> names = new ArrayList<String>(scores.keySet());
    for (int i = 0; i < names.size(); i++) {

        String name = names.get(i);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid score");}

        Double score = scores.get(name);
        if (score == null || !Double.isFinite(score) || score < 0) {
            throw new IllegalArgumentException("Invalid score");}
    }

     // Sort by score from highest to lowest
     for (int i = 0; i < names.size() - 1; i++) {
         for (int j = i + 1; j < names.size(); j++) {

             String firstName = names.get(i);
             String secondName = names.get(j);

             int comparison = Double.compare(scores.get(firstName),scores.get(secondName));

             boolean swap = false;
             if (comparison < 0) {
                 swap = true;
             } else if (comparison == 0) {
                 if (firstName.compareTo(secondName) > 0) {
                     swap = true;
                 }
             }

             if (swap) {
                 names.set(i, secondName);
                 names.set(j, firstName);
             }
         }
     }

     List<String> result = new ArrayList<String>();

     int count = limit;
     if (count > names.size()) {
         count = names.size();}
     
     for (int i = 0; i < count; i++) {
         result.add(names.get(i));
     }

     return result;
 }
    
}

