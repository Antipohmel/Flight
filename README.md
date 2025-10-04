[readme.md](https://github.com/user-attachments/files/22701445/readme.md)
# Prettyfier

Prettyfier is a simple Java command-line tool to format and process flight itineraries to make sure they are readable and client friendly. It reads an input file, improves formatting and replaces elements, and writes the results to an output file.

## Features

- Processes text files containing flight itineraries.
- Replaces airport codes with full airport names and municipalities using a CSV lookup file.
- Converts dates and times to a human-readable format.
- Cleans up line breaks and formatting issues.
- Supports both 12-hour and 24-hour time formats.

## Requirements

- Java 17 or higher
- Input text file
- CSV airport lookup file (with headers: `name`, `iso_country`, `municipality`, `icao_code`, `iata_code`, `coordinates`)

## Usage

```bash
java Prettyfier.java <input-file> <output-file> <airport-lookup-file>
