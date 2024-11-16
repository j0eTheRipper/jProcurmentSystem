package procurmentsystem;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.function.Function;

public class Table {
    private final File file;
    private final int numberOfColumns;
    private final List<String> columnNames;
    private final HashMap<String, List<String>> columns = new HashMap<>();

    public Table(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        Scanner reader = new Scanner(file);
        String row = reader.nextLine();
        String[] cls = row.split(",");
        columnNames = Arrays.stream(cls).toList();
        numberOfColumns = columnNames.size();

        for (String cell : columnNames) {
            columns.put(cell, new ArrayList<>());
        }

        while (reader.hasNextLine()) {
            row = reader.nextLine();
            String[] rowValues = row.split(",");

            addValues(rowValues);
        }
    }

    public void addRow(String[] values) throws IncorrectNumberOfValues {
        if (values.length > numberOfColumns || values.length < numberOfColumns)
            throw new IncorrectNumberOfValues();

        try (FileWriter writer = new FileWriter(file, true)) {
            addValues(values);
            writer.write(String.join(",", values) + "\n");
        } catch (IOException e) {
            System.out.println("please correct the input :)");
        }
    }

    public List<String> getRow(String column, Function<String, Boolean> checkerFunction) {
        if (!columnNames.contains(column))
            return null;

        try {
            int rowNumber = getRowIndex(column, checkerFunction);
            ArrayList<String> result = new ArrayList<>();
            for (String colName : columnNames) {
                result.add(columns.get(colName).get(rowNumber));
            }
            return result;
        } catch (ValueNotFound e) {
            return null;
        }
    }

    public List<String> getLastRow() throws FileNotFoundException {
        String lastLine = "";
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            lastLine = reader.nextLine();
        }

        return List.of(lastLine.split(","));
    }

    public List<List<String>> getRows(String column, Function<String, Boolean> checkerFunction) {
        if (!columnNames.contains(column))
            return null;

        List<String> columnValues = columns.get(column);
        ArrayList<List<String>> allMatchingRows = new ArrayList<>();

        for (int i = 0; i < columnValues.size(); i++) {
            String cell = columnValues.get(i);
            if (checkerFunction.apply(cell)) {
                ArrayList<String> result = new ArrayList<>();
                for(String columnName : columnNames) {
                    result.add(columns.get(columnName).get(i));
                }
                allMatchingRows.add(result);
            }
        }
        return allMatchingRows;
    }

    public int getRowIndex(String lookUpColumn, Function<String, Boolean> checkerFunction) throws ValueNotFound {
        if (!columnNames.contains(lookUpColumn))
            return 0;

        List<String> ColumnValues = columns.get(lookUpColumn);
        for (int i = 0; i < ColumnValues.size(); i++) {
            String cell = ColumnValues.get(i);
            if (checkerFunction.apply(cell))
                return i;
        }
        throw new ValueNotFound();
    }

    public List<Integer> getRowIndexes(String lookUpColumn, Function<String, Boolean> checkerFunction) throws ValueNotFound {
        if (!columnNames.contains(lookUpColumn))
            return null;

        List<String> ColumnValues = columns.get(lookUpColumn);
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < ColumnValues.size(); i++) {
            String cell = ColumnValues.get(i);
            if (checkerFunction.apply(cell))
                result.add(i);
        }
        return result;
    }

    public void updateRow(int rowIndex, String columnToBeEdited, String newValue) {
        if (!columnNames.contains(columnToBeEdited))
            return;

        columns.get(columnToBeEdited).set(rowIndex, newValue);

        writeNewContentsTofile();
    }

    public void updateRows(List<Integer> rowIndexes, String columnToBeEdited, String newValue) {
        if (!columnNames.contains(columnToBeEdited))
            return;

        for (int rowIndex : rowIndexes) {
            columns.get(columnToBeEdited).set(rowIndex, newValue);
        }

        writeNewContentsTofile();
    }

    private void writeNewContentsTofile() {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(String.join(",", columnNames) + "\n");
            int rowNumber = columns.entrySet().iterator().next().getValue().size();
            for (int i = 0; i < rowNumber; i++) {
                ArrayList<String> row = new ArrayList<>();
                for (String col : columnNames) row.add(columns.get(col).get(i));
                writer.write(String.join(",", row) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    public void deleteRow(int rowIndex) {
        for (String colName : columnNames) {
            columns.get(colName).remove(rowIndex);
        }

        writeNewContentsTofile();
    }

    public void deleteRows(List<Integer> rowIndexes) {
        for (int rowIndex : rowIndexes) {
            for (String colName : columnNames) {
                columns.get(colName).remove(rowIndex);
            }
        }

        writeNewContentsTofile();
    }

    private void addValues(String[] rowValues) {
        for (int i = 0; i < rowValues.length; i++) {
            String cell = rowValues[i];
            String column = columnNames.get(i);
            columns.get(column).add(cell);
        }
    }

}