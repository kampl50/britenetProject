package filesUtils;

import exceptions.FileSizeToLargeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    public static List<List<String>> getRecords(String path) {
        List<List<String>> records = new ArrayList<>();
        File file = new File(path);
        if(FileUtils.getSize(file)>1)
            try {
                throw new FileSizeToLargeException("file size too large");
            } catch (FileSizeToLargeException e) {
                e.printStackTrace();
            }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
