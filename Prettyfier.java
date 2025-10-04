import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.*;
import java.nio.file.Paths;
import java.nio.file.Path;


// command line 
// promt if less then 3 arguments are inserted
// checks that input and lookup files exist
// checks header of lookup file matching "name", "iso_country", "municipality", "icao_code", "iata_code", "coordinates" by checking csvHeaderChecker.java 
// and validateHeaders method returns false - prints error and stops
// AirportLookup - loads CSV into memory with try block in case of errors
// creating bufferedreader and writer with try() - which is try with resources - buffered reader needed to pass lines to Inputformatter processLine
// 

public class Prettyfier {
public static void main(String [] args) {
    if (args.length == 0 || args[0].equals("-h") || args[0].equals("-help") || args.length < 3 ){
        printHelp();
        return;
    }
    String inputFile = args[0];
    String outputFile = args[1];
    String lookupFile = args[2];
    
    Path inputPath = Paths.get(inputFile);
    Path lookupPath = Paths.get(lookupFile);

    if (!Files.exists(inputPath) || !Files.isRegularFile(inputPath)) {
        System.out.println("Input not found");
        return;
    }
    if (!Files.exists(lookupPath) || !Files.isRegularFile(lookupPath)) {
        System.out.println("Airport lookup not found");
        return;
    }
    if (!CsvHeaderChecker.validateHeaders(lookupFile)) {
        System.out.println("CSV file missing required headers");
        return;
    }
    try {
        AirportLookup lookup = new AirportLookup(lookupFile);
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
        String line;
        while ((line = br.readLine()) !=null) {
                    
        String formattedLine = InputFormatter.processLine(line, lookup);
        bw.write(formattedLine);
        bw.newLine();
    }
System.out.println("Output written to " + outputFile);
}
    }
catch (IOException e) {
    System.out.println("An error has occured");
    e.printStackTrace();
}
}
private static void printHelp() {
    System.out.println(" itinerary usage:");
    System.out.println(";$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv");
}
}


