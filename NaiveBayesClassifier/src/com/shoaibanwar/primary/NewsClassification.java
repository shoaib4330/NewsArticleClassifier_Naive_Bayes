/*
 * Copyright (C) 2017 shoaibanwar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.shoaibanwar.primary;

import com.datumbox.opensource.classifiers.NaiveBayes;
import com.datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;
import com.datumbox.opensource.examples.NaiveBayesExample;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shoaibanwar
 */
public class NewsClassification {

    public static String[] getLinesThoroughly(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }

 

    public static void main(String[] args) throws IOException {

        //A Map to contain URL's of training files against name of each category...
        Map<String, URL> trainingFiles = new HashMap<>();
        trainingFiles.put("business", NewsClassification.class.getResource("/merged_docs/business.txt"));
        trainingFiles.put("entertainment", NewsClassification.class.getResource("/merged_docs/entertainment.txt"));
        trainingFiles.put("politics", NewsClassification.class.getResource("/merged_docs/politics.txt"));
        trainingFiles.put("sports", NewsClassification.class.getResource("/merged_docs/sport.txt"));
        trainingFiles.put("tech", NewsClassification.class.getResource("/merged_docs/tech.txt"));

        //A Map to read the content of the training files against name of each category...
        Map<String, String[]> trainingExamples;
        trainingExamples = new HashMap<>();
        for (Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), getLinesThoroughly(entry.getValue()));
        }

        //train classifier
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        nb.train(trainingExamples);

        //get trained classifier knowledgeBase
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();

        nb = null;
        trainingExamples = null;

        //Classifier created to classify objects
        nb = new NaiveBayes(knowledgeBase);
        // Get files from each class to be tested
        Utility dataStore = new Utility();
        Map<String,String> mapWithPaths = dataStore.getClasses_With_PathTo_IndividualFiles_TrainingTestData();
        
  
        double totalExamplesClassified =0;
        double totalCorrectClassifications=0;
        double totalWronClassifications=0;
        
        for (Map.Entry<String, String> entry : mapWithPaths.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            List<CategoryRecord> listOfFilesForCurrentClass = dataStore.getFilesInDirectory(value);
            totalExamplesClassified = totalExamplesClassified + listOfFilesForCurrentClass.size();
            
            for (int i = 0; i < listOfFilesForCurrentClass.size(); i++) {
                CategoryRecord record = listOfFilesForCurrentClass.get(i);
                String pathString = record.fileObjectToHold.getAbsolutePath();
                Path pathToFile = Paths.get(pathString);
                byte[] encoded = Files.readAllBytes(pathToFile);
                String test_Text_From_ByteArray = new String(encoded, StandardCharsets.UTF_8);
                
                String label = nb.predict(test_Text_From_ByteArray);
                record.Category_predicted = label;
                
                if (record.Category_Original.equals(record.Category_predicted))
                {
                    totalCorrectClassifications++;
                }
                else{
                    totalWronClassifications++;
                }
                //System.out.print("\n The given article's category is: " + label + "\n");
            }
        }
        
        double training_accracy = totalCorrectClassifications/totalExamplesClassified *100;
        System.out.println("\n Training accuracy: "+training_accracy+" (Whole training set)");
        
        double subset_training_totalExamplesClassified =0;
        double subset_training_totalCorrectClassifications=0;
        double subset_training_totalWronClassifications=0;
        
        Map<String,String> subset_training_mapWithPaths = dataStore.getClasses_With_PathTo_IndividualFiles_Subset_TrainingData();

        
        for (Map.Entry<String, String> entry : subset_training_mapWithPaths.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            List<CategoryRecord> listOfFilesForCurrentClass = dataStore.getFilesInDirectory(value);
            subset_training_totalExamplesClassified = subset_training_totalExamplesClassified + listOfFilesForCurrentClass.size();
            
            for (int i = 0; i < listOfFilesForCurrentClass.size(); i++) {
                CategoryRecord record = listOfFilesForCurrentClass.get(i);
                String pathString = record.fileObjectToHold.getAbsolutePath();
                Path pathToFile = Paths.get(pathString);
                byte[] encoded = Files.readAllBytes(pathToFile);
                String test_Text_From_ByteArray = new String(encoded, StandardCharsets.UTF_8);
                
                String label = nb.predict(test_Text_From_ByteArray);
                record.Category_predicted = label;
                
                if (record.Category_Original.equals(record.Category_predicted))
                {
                    subset_training_totalCorrectClassifications++;
                }
                else{
                    subset_training_totalWronClassifications++;
                }
                //System.out.print("\n The given article's category is: " + label + "\n");
            }
        }
        
        double subset_training_accuracy = subset_training_totalCorrectClassifications/subset_training_totalExamplesClassified *100;
        System.out.println("\n Training Subset accuracy: "+subset_training_accuracy+" (30 articles from each class)");
        
        
        double test_totalExamplesClassified =0;
        double test_totalCorrectClassifications=0;
        double test_totalWronClassifications=0;
        
        Map<String,String> test_mapWithPaths = dataStore.getClasses_With_PathTo_IndividualFiles_UnSeenTestData();

        
        for (Map.Entry<String, String> entry : test_mapWithPaths.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            List<CategoryRecord> listOfFilesForCurrentClass = dataStore.getFilesInDirectory(value);
            test_totalExamplesClassified = test_totalExamplesClassified + listOfFilesForCurrentClass.size();
            
            for (int i = 0; i < listOfFilesForCurrentClass.size(); i++) {
                CategoryRecord record = listOfFilesForCurrentClass.get(i);
                String pathString = record.fileObjectToHold.getAbsolutePath();
                Path pathToFile = Paths.get(pathString);
                byte[] encoded = Files.readAllBytes(pathToFile);
                String test_Text_From_ByteArray = new String(encoded, StandardCharsets.UTF_8);
                
                String label = nb.predict(test_Text_From_ByteArray);
                record.Category_predicted = label;
                
                if (record.Category_Original.equals(record.Category_predicted))
                {
                    test_totalCorrectClassifications++;
                }
                else{
                    test_totalWronClassifications++;
                }
                //System.out.print("\n The given article's category is: " + label + "\n");
            }
        }
        
        double test_accuracy = test_totalCorrectClassifications/test_totalExamplesClassified *100;
        System.out.println("\n Test accuracy: "+test_accuracy+" (30 articles from each class)");

    }
}
