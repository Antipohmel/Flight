import java.io.*;
import java.util.*;


//using Arrays.asList to define the expected header to be matched to the csv file header
// using boolean that will be used by Prettyfier to check whether expected header matches the CSV file
//using BufferedReader with the help of FileReader on the lookup file that comes from Prettyfier(user input parameters)
// splitting by ,
// declaring ints store matches for expected header names - name, municipality, icao and iata codes
// using name/municipality index variables to match expected headers by name and link them to integers
// Index later used in AirportLookup name/municipality/icao and iata to fill them with corresponsing names (String variables)



public class CsvHeaderChecker {

public static int nameIndex;
public static int municipalityIndex;
public static int icaoIndex;
public static int iataIndex;

    private static final List <String> expectedHeaders = Arrays.asList(
        "name", "iso_country", "municipality", "icao_code", "iata_code", "coordinates");

public static boolean validateHeaders(String lookupFile) {
    try (BufferedReader br = new BufferedReader(new FileReader(lookupFile))) {
        String headerLine = br.readLine(); 

        if (headerLine == null) {
            return false;
        }

        String[] headers = headerLine.split(",");
        Map<String, Integer> headerIndexMap = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            headerIndexMap.put(headers[i].trim().toLowerCase(), i);
        }

        for (String expected : expectedHeaders) {
            if(!headerIndexMap.containsKey(expected)) {
                return false;
            }
        }
        nameIndex = headerIndexMap.get("name");
        municipalityIndex = headerIndexMap.get("municipality");
        icaoIndex = headerIndexMap.get("icao_code");
        iataIndex = headerIndexMap.get("iata_code");
        return true;
    }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }
}

    
