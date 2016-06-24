/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

import com.jme3.math.Vector3f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Oliver
 */
public class NDAngleReader {

   private ArrayList<Vector3f> centerDATArray   = null; 
   private ArrayList<Vector3f> posDATArray      = null; 
   
    public NDAngleReader() {
    }

    public void readNDAFile(String filename) {
    }

    public void readDATFile(String filename) {

        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            
            centerDATArray = new ArrayList<Vector3f>();
            posDATArray = new ArrayList<Vector3f>();
            
            int l = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (l > 1) {
                    String x = line;
                    Pattern logEntry = Pattern.compile("\\{(.*?)\\}");
                    Matcher matchPattern = logEntry.matcher(x);
                    int m = 0;
                    while (matchPattern.find()) {
                        
                        Vector3f centre = null;
                        Vector3f pos = null;
                        
                        System.out.println(matchPattern.group(1));
                        //split up coordinates
                        if (m == 0){
                           //centre 
                           String[] centreStrings =  matchPattern.group(1).split("\\s+");
                           float cx = Float.parseFloat(centreStrings[0]);
                           float cy = Float.parseFloat(centreStrings[1]);
                           float cz = Float.parseFloat(centreStrings[2]);
                           centre = new Vector3f(cx,cy,cz);
                           centerDATArray.add(centre);

                        }
                        else if (m == 1){
                           String[] minStrings =  matchPattern.group(1).split("\\s+");
                           float cx = Float.parseFloat(minStrings[0]);
                           float cy = Float.parseFloat(minStrings[1]);
                           float cz = Float.parseFloat(minStrings[2]);
                           pos = new Vector3f(cx,cy,cz);
                           posDATArray.add(pos);

                        }
                        
                        m++;
                    }
                }
                l++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    float total = 0;
    
  public ArrayList<Float> calculateDeltaAngles(){
        ArrayList<Float> deltaAngles = new ArrayList<Float>();
        System.out.println("d");
        
        for (int i = 1; i < centerDATArray.size() - 1; i++){
            Vector3f v1 = posDATArray.get(i-1).subtract(centerDATArray.get(i-1));
            Vector3f v2 = posDATArray.get(i).subtract(centerDATArray.get(i));
            
            double ang1 = Math.acos(v1.dot(v2) / (v1.length() * v2.length()))* (180/Math.PI);
            deltaAngles.add(new Float(ang1));
            total += ang1;
        }
        
        System.out.println("Total DAng: " + total + "\n\n\n");
        return deltaAngles;
    }
    
    public ArrayList<Float> calculateTotalAngles(){
        ArrayList<Float> totalAngles = new ArrayList<Float>();
        
        for (int i = 0; i < centerDATArray.size(); i++){
            Vector3f cen1 = centerDATArray.get(0);
            Vector3f pos1 = posDATArray.get(0);
            Vector3f cen2 = centerDATArray.get(i);
            Vector3f pos2 = posDATArray.get(i);
            
            cen1 = pos1.subtract(cen1);
            cen2 = pos2.subtract(cen2);
            
            
            float dot = (cen1.x * cen2.x) + (cen1.y * cen2.y) + (cen1.z * cen2.z);
            float m1  = (float)Math.sqrt((cen1.x*cen1.x) + (cen1.y*cen1.y) + (cen1.z*cen1.z));
            float m2  = (float)Math.sqrt((cen2.x*cen2.x) + (cen2.y*cen2.y) + (cen2.z*cen2.z));

            double ang = Math.acos(dot / (m1*m2)) * (180/Math.PI);
         
            totalAngles.add(new Float(ang));
        }
        
        System.out.println("Total Start: " + totalAngles.get(0));
        System.out.println("Total End  : " + totalAngles.get(totalAngles.size()-1));
        
        return totalAngles;
    }

    public ArrayList<Vector3f> getCenterDATArray() {
        return centerDATArray;
    }

    public void setCenterDATArray(ArrayList<Vector3f> centerDATArray) {
        this.centerDATArray = centerDATArray;
    }
}
