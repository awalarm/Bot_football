package com.myApp;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class VotingResults  {

    public void createdVotingList() throws Exception    {
        String csv = "data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        //Create record
        String [] record = "Итого, 0".split(",");
        //Write the record to file
        writer.writeNext(record);
        //close the writer
        writer.close();
    }

    public void addVotingResults(String voting) throws Exception {
        String csv = "data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
        String [] record = voting.split(",");
        writer.writeNext(record);
        writer.close();
    }

    @SneakyThrows
    public void deletePlayer(int lineIndex) {
        String csv = "data.csv";
        //Build reader instance
        CSVReader reader2 = new CSVReader(new FileReader(csv));
        List<String[]> allElements = reader2.readAll();
        allElements.remove(lineIndex);
        FileWriter sw = new FileWriter(csv);
        CSVWriter writer = new CSVWriter(sw);
        writer.writeAll(allElements);
        writer.close();
        //Read CSV line by line and use the string array as you want
        for (String[] row : allElements) {
            System.out.println(Arrays.toString(row));
        }
    }

    @SneakyThrows
    public int searchRejectedPlayer (String idPlayer) {
        int line = 0;
        CSVReader reader = new CSVReader(new FileReader("data.csv"), ',' , '"' , line);
        //Read CSV line by line and use the string array as you want
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                //Verifying the read data here

                if (nextLine[0].equals(idPlayer)){
                    return line;
                }
                line++;
            }
        }
        return -1;
    }
}
