package com.company;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        boolean isMode = false;
        boolean isKey = false;
        boolean isData = false;
        boolean isOut = false;
        boolean isIn = false;
        String mode = null;
        String data = null;
        Integer key = null;
        String exportFileName = null;
        String fileName = null;

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "-mode":
                    isMode = true;
                    mode = args[i + 1];
                    break;
                case "-key":
                    isKey = true;
                    key = Integer.valueOf(args[i + 1]);
                    break;
                case "-data":
                    isData = true;
                    data = args[i + 1];
                    break;
                case "-out":
                    isOut = true;
                    exportFileName = args[i + 1];
                    break;
                case "-in":
                    isIn = true;
                    fileName = args[i + 1];
                    break;
            }

        }
        Data codeData;
        if (isOut && isIn && !isData) {
            codeData = new Data(fileName, exportFileName, true);
        } else if (isOut && isIn && isData) {
            codeData = new Data(data, fileName, exportFileName);
        } else if (isData && isOut && !isIn) {
            codeData = new Data(data, exportFileName);
        } else {
            codeData = new Data(data);
        }


        Decoder decoder = new Decoder(isMode ? mode : "enc", isKey ? key : 0, codeData);

        if (decoder.data.onlyData && !decoder.data.toExport) {
            //System.out.println("Odczyt z argumentu i wywietlenie");
            // System.out.println(codeData.data);
            System.out.println(decoder.data.choseAction(decoder.mode, decoder.key, codeData.data));
        } else if (decoder.data.onlyData && decoder.data.toExport) {
            //System.out.println("Odczyt z argumentu i zapis do niego");
            decoder.writeToFile(decoder.data.choseAction(decoder.mode, decoder.key, decoder.data.data));
        } else if (decoder.data.readFromFile && decoder.data.toExport) {
            //System.out.println("Odczyt z pliku i zapis do niego");
            decoder.writeToFile(decoder.data.choseAction(decoder.mode, decoder.key, decoder.readFromFile()));
        } else if (decoder.data.readFromFile && !decoder.data.toExport) {
            // System.out.println("Odczyt z pliku i wyświetlenie");
            System.out.print(decoder.data.choseAction(decoder.mode, decoder.key, decoder.readFromFile()));
        }
    }
}

class Decoder {
    String mode;
    Integer key;
    Data data;
    String dataToShow;
    String dataFromFile;

    Decoder(String mode, Integer key, Data data) {
        this.mode = mode;
        this.key = key;
        this.data = data;
    }


    public void writeToFile(String data) throws IOException {

        File file = new File(this.data.exportFileName);
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.close();
    }

    public String readFromFile() {
        File file = new File(this.data.fileName);
        try (Scanner scanner = new Scanner(file)) {
            return scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + this.data.fileName);
        }

        return null;
    }


}


class Data {


    String data = "";
    String fileName;
    String exportFileName;
    boolean onlyData = false;
    boolean toExport = false;
    boolean readFromFile = false;

    Data(String data) {
        this.data = data;
        this.onlyData = true;
    }

    Data(String data, String exportFileName) {
        new Data(data);
        this.exportFileName = exportFileName;
        this.toExport = true;
    }

    Data(String fileName, String exportFileName, boolean onlyFiles) {
        this.fileName = fileName;
        this.exportFileName = exportFileName;
        this.toExport = onlyFiles;
        this.readFromFile = true;
    }


    Data(String data, String fileName, String exportFileName) {
        this.data = data;
        this.fileName = fileName;
        this.exportFileName = exportFileName;
        onlyData = true;
    }

    public String encode(Integer key, String data) {
        if (data.length() > 0) {
            char[] chars = data.toCharArray();
            StringBuilder str = new StringBuilder("");
            int count = 0;

            for (char item : chars) {
                char shiftItem = (char) ((item + key));
                str.append(shiftItem);


            }

            return str.toString();
        }
        return "";

    }


    public String decode(Integer key, String data) {
        if (data.length() > 0) {
            char[] chars = data.toCharArray();
            StringBuilder str = new StringBuilder("");
            int count = 0;

            for (char item : chars) {
                char shiftItem = (char) ((item - key));
                str.append(shiftItem);


            }

            return str.toString();
        }
        return "";
    }

    public void setData(String data) {
        this.data = data;

    }

    public String getData() {
        return this.data;
    }


    public String choseAction(String mode, Integer key, String data) {
        return switch (mode) {
            case "enc" -> this.encode(key, data);
            case "dec" -> this.decode(key, data);
            default -> this.encode(key, data);
        };
    }

}




