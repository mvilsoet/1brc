# 1brc
This project parses (min, max, average) a billion rows but really fast. 
- Row format of measurements.txt: String;Decimal\n
- https://github.com/gunnarmorling/1brc/tree/main

## Class Diagram
![uml.png](uml.png)

## TODO:
- DefaultReportGenerator creates logfiles
- Telemetry interface has .report(message, type) 
  - NoOpTelemeter as DefaultTelemeter.
  - JFRTelemeter using JFR. maybe pass --verbose to enable 
- Unit tests / testing logic accuracy with known measurement_test.txt
- How-to use this repo

## Notes
- `mvn test` will run JFR profiling by default as seen in `pom.xml`.