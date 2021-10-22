package com.myApp;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class VotingResults  {

    private String csv = "data.csv";

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public void addVotingResults(String voting) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(getCsv(), true));
        String [] record = voting.split(",");
        writer.writeNext(record);
        writer.close();
    }

    @SneakyThrows
    public void deletePlayer(int lineIndex) {
        //Build reader instance
        CSVReader reader2 = new CSVReader(new FileReader(getCsv()));
        List<String[]> allElements = reader2.readAll();
        allElements.remove(lineIndex);
        FileWriter sw = new FileWriter(getCsv());
        CSVWriter writer = new CSVWriter(sw);
        writer.writeAll(allElements);
        writer.close();
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
