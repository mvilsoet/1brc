package onebrc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeasurementsLoader {
    private final List<String> lines = new ArrayList<>();

    public MeasurementsLoader(String path) throws IOException, MalformedMeasurementException {
        try (BufferedReader r = new BufferedReader(new FileReader(path))) {
            String l;
            int count = 0;
            while ((l = r.readLine()) != null && count < 10) {
                String[] parts = l.split(";", -1);
                if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    throw new MalformedMeasurementException("Bad line " + (count + 1) + ": " + l);
                }
                lines.add(l);
                count++;
            }
        }
    }

    public List<String> getFirstTen() {
        return new ArrayList<>(lines);
    }

    public void printFirstTen() {
        for (String l : lines) {
            System.out.println(l);
        }
    }
}
