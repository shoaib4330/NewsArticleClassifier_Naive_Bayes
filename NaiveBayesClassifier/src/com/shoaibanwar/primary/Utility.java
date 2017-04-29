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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shoaibanwar
 */
public class Utility {
    private static final String[] categoryList = {"business","entertainment","politics","sport","tech"};
    
    public Map<String, String> getClasses_With_PathTo_IndividualFiles_TrainingTestData() {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("business", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_test_data/business");
        dataMap.put("entertainment", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_test_data/entertainment");
        dataMap.put("politics", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_test_data/politics");
        dataMap.put("sports", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_test_data/sport");
        dataMap.put("tech", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_test_data/tech");
        return dataMap;
    }
    
    public Map<String, String> getClasses_With_PathTo_IndividualFiles_UnSeenTestData() {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("business", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/unseen_test_data/business");
        dataMap.put("entertainment", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/unseen_test_data/entertainment");
        dataMap.put("politics", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/unseen_test_data/politics");
        dataMap.put("sports", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/unseen_test_data/sport");
        dataMap.put("tech", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/unseen_test_data/tech");
        return dataMap;
    }
    
    public Map<String, String> getClasses_With_PathTo_IndividualFiles_Subset_TrainingData() {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("business", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_subset_data/business");
        dataMap.put("entertainment", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_subset_data/entertainment");
        dataMap.put("politics", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_subset_data/politics");
        dataMap.put("sports", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_subset_data/sport");
        dataMap.put("tech", "/Users/shoaibanwar/Desktop/AI Project/NaiveBayesClassifier/training_subset_data/tech");
        return dataMap;
    }

    public List<CategoryRecord> getFilesInDirectory(String path) {
        String categoryToExist="";
        for (int i = 0; i < categoryList.length; i++) {
            if (path.contains(categoryList[i]))
            {
                categoryToExist = categoryList[i];
            }
        }
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        
        List<CategoryRecord> listToReturn = new ArrayList();
        
        
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                CategoryRecord mCategoryRecord = new CategoryRecord();
                mCategoryRecord.total_category_files_count = listOfFiles.length;
                mCategoryRecord.fileObjectToHold = listOfFiles[i];
                mCategoryRecord.Category_Original = categoryToExist;
                listToReturn.add(mCategoryRecord);
            }
        }
        return listToReturn;
    }
}
