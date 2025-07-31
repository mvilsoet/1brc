# 1brc
This project parses (min, max, average) a billion rows but really fast. 
- https://github.com/gunnarmorling/1brc/tree/main

## Class Diagram
![uml.png](uml.png)

## TODO:
- DefaultReportGenerator creates logfiles
- Telemetry interface has .report(message) 
  - NoOpTelemeter as DefaultTelemeter.
  - JFRTelemeter using JFR. maybe pass --verbose to enable 
- Unit tests / testing logic accuracy with known measurement_test.txt
- How-to use this repo

