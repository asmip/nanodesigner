/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Oliver
 */
public class NDWriter {
        BufferedWriter writer = null;
        FileWriter fileWriter = null;
        
        
    public void writeFloatListToColumn(String header, ArrayList<Float> vals, String filename, boolean override) {

        writeTextFile(filename, header + "\n", override);
        for (int i = 0; i < vals.size(); i++) {
            String linestring = "" + vals.get(i) + "\n";
            writeTextFile(filename, linestring, override);
        }
        writeTextFile(filename, "\n\n\n\n", override);
    }

    private FileWriter writeTextFile(String filename, String s, boolean append) {

        try {
            fileWriter = new FileWriter(filename, append);
        } catch (IOException e1) {
        }

        try {
            writer = new BufferedWriter(fileWriter);
            writer.write(s);
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                }

            }
        }
        return fileWriter;
    }
}
