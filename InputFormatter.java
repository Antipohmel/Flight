import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// regex patterns for dates and #XXX/#XXXX + *#XXX / *#XXXX
// line break replace pattern + new pattern for Dates and Times + replacememnt for Airport codes whick comes from lookup
// replaceAirportCodes - replaces Codes using lookup
// replacePattern - takes patterns from above and matches to input line, creates a Buffer. Apparently appendReplacement that i want to use 
// does not work witk StringBuilder, so, Stringbuffer is used. dividing strings based on () to group0 which is (#XXX) and group1 - (XXX) format(to find code via lookup)
// lookupMap.getOrDefault - to replace a match if found - if not - then keep the original
// quoteReplacement (Returns a literal replacement String for the specified String)

// replacement = whole datetime so that if time cannot be parsed - it gets no error

public class InputFormatter {

private static final Pattern datePattern = Pattern.compile("D\\(([^)]+)\\)");
private static final Pattern hourPattern12 = Pattern.compile("T12\\(([^)]+)\\)");
private static final Pattern hourPattern24 = Pattern.compile("T24\\(([^)]+)\\)");
private static final Pattern iataNamePattern = Pattern.compile("#([A-Z]{3})");
private static final Pattern icaoNamePattern = Pattern.compile("##([A-Z]{4})");
private static final Pattern iataMunicipatilyPattern = Pattern.compile("\\*#([A-Z]{3})");
private static final Pattern icaoMunicipalityPattern = Pattern.compile("\\*##([A-Z]{4})");


public static String processLine(String oneLine, AirportLookup lookup) {
    if (oneLine == null || oneLine.isEmpty()) 
    return oneLine;
    
        oneLine = oneLine.replace('\u2212', '-');
oneLine = oneLine.replaceAll("[\\v\\f\\r]", "\n");
oneLine = oneLine.replaceAll("(\\n){2,}", "\n");


oneLine = replaceDateTime(oneLine, datePattern, "dd MMM yyyy");
oneLine = replaceDateTime(oneLine, hourPattern12, "hh:mma");
oneLine = replaceDateTime(oneLine, hourPattern24, "HH:mm");
oneLine = replaceAirportCodes(oneLine, lookup);
return oneLine;
}

private static String replaceAirportCodes(String oneLine, AirportLookup lookup) {
    oneLine = replacePattern(oneLine, iataMunicipatilyPattern, lookup.iataMunicipalityMap);
    oneLine = replacePattern(oneLine, icaoMunicipalityPattern, lookup.icaoMunicipalityMap);
    oneLine = replacePattern(oneLine, iataNamePattern, lookup.iataNameMap);
    oneLine = replacePattern(oneLine, icaoNamePattern, lookup.icaoNameMap);

System.out.println(oneLine); 
return oneLine;

}
    
    private static String replacePattern(String oneLine, Pattern pattern, Map<String, String> lookupMap) {
        Matcher matcher = pattern.matcher(oneLine);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String code = matcher.group(1);
            String replacement = lookupMap.getOrDefault(code, matcher.group(0)); 
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
private static String replaceDateTime(String oneLine, Pattern pattern, String format) {
        Matcher matcher = pattern.matcher(oneLine);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String isoString = matcher.group(1);
            String replacement = matcher.group(0);
        
        try {
            OffsetDateTime odt = OffsetDateTime.parse(isoString);
            if (format.equals("dd MMM yyyy")) {
                replacement = odt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            }
            else if (format.equals("hh:mma")) {
                String timePart = odt.format(DateTimeFormatter.ofPattern("hh:mma"));
                String offset =odt.getOffset().toString();
                if (offset.equals("Z")) offset = "+00:00";
                replacement = timePart + " (" + offset + ")";

            }
            else if(format.equals("HH:mm")) {
                String timePart = odt.format(DateTimeFormatter.ofPattern("HH:mm"));
                String offset = odt.getOffset().toString();
                if (offset.equals("Z")) offset = "+00:00";
                replacement = timePart + "( " + offset + ")";
            }
        }
                catch (DateTimeParseException e) {}
            
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
        }
    }



