import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;

// creating a hashmap to store iata/icao maps with String values - this to make sure that i can return iata / icao if it is not found in CSV
// using CsvHeaderChecker indexes for finding the matching colums with correct data, then populating new Map with name/municipality and iata / icao
public class AirportLookup {

    public final Map<String, String> iataNameMap = new HashMap<>();
    public final Map<String, String> icaoNameMap = new HashMap<>();
    public final Map<String, String> iataMunicipalityMap = new HashMap<>();
    public final Map<String, String> icaoMunicipalityMap = new HashMap<>();

    public AirportLookup(String lookupFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(lookupFile))) {
            String ignoreLine = br.readLine();
            String oneLine;

    while ((oneLine = br.readLine()) !=null)
    {
        String[] csvColumns = oneLine.split(",");

        String name = csvColumns[CsvHeaderChecker.nameIndex].trim();
        String municipality = csvColumns[CsvHeaderChecker.municipalityIndex].trim();
        String icao = csvColumns[CsvHeaderChecker.icaoIndex].trim();
        String iata = csvColumns[CsvHeaderChecker.iataIndex].trim();

        if(!iata.isEmpty()) {
            iataNameMap.put(iata, name); 

        if(!municipality.isEmpty()) {
            iataMunicipalityMap.put(iata, municipality); 
        } 
    }
        if(!icao.isEmpty()) {
            icaoNameMap.put(icao, name);

        if(!municipality.isEmpty()) {
            icaoMunicipalityMap.put(icao, municipality);
        }
        }
        }
    }
        }

}
