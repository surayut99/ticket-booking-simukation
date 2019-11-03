package theatre;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TestClass {
    public static void main(String[] args) {
        String a = "A1,A2,A3,A4,A5";
        String r = "A1,A3";
        String c = a.replaceAll(r, "");
        System.out.println(c);

    }

    public static void write() {
        String username = "u";
        String theatre = "5";
        String startTime = "10:00";
        String newVersion = "E4,E6,E7";
        String oldVersion = "A1,A2,B3,B4,C5,C6";
        boolean edited = false;
        List<String> oldData = new ArrayList<>();

        try {
            FileWriter writer = new FileWriter(new File("data/account_data/BookingData.csv"));
            FileReader reader = new FileReader((new File("data/account_data/BookingData.csv")));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("username," + username)) {
                    oldData.add(line);
                    while ((line = bufferedReader.readLine()) != null) {
                        oldData.add(line);
                        if (line.isEmpty()) break;
                    }
                    for (String s : oldData) System.out.println(s);
                    for (int i = 0; i < oldData.size(); i++) {
                        if (oldData.get(i).contains("theatre," + theatre)) {
                            System.out.println(oldData.get(i));
                            for (int j = i + 1; j < oldData.size(); j++) {
                                if (oldData.get(j).equals("") || oldData.get(j).contains("theatre")) {
                                    oldData.add(j, "\tpositions," + newVersion);
                                    oldData.add(j, "\tschedule," + startTime);
                                    break;
                                }
                                if (oldData.get(j).contains(startTime)) {
                                    System.out.println(oldData.get(j));
                                    String oldString = oldData.get(++j);
                                    String newString = oldString.replaceAll(oldVersion, newVersion);
                                    oldData.remove(j);
                                    oldData.add(j, newString);
                                    System.out.println(oldData.get(j));
                                    edited = true;
                                    break;
                                }
                            }
                        }
                        if (edited) break;
                        if (oldData.get(i).contains("theatre,")) {
                            String[] split = oldData.get(i).split(",");
                            if (Integer.parseInt(split[1]) > Integer.parseInt(theatre)) {
                                oldData.add(i, "\tpositions," + newVersion);
                                oldData.add(i, "\tschedule," + startTime);
                                oldData.add(i, "theatre," + theatre);
                                break;
                            }
                        }
                        if (oldData.get(i).equals("")) {
                            oldData.add(i, "\tpositions," + newVersion);
                            oldData.add(i, "\tschedule," + startTime);
                            oldData.add(i, "theatre," + theatre);
                            break;
                        }
                    }
                    String newData = String.join("\n", oldData);
                    writer.write(newData);
                    writer.flush();
                }
                writer.write(line + "\n");
                writer.flush();
            }
            reader.close();
            writer.close();
            bufferedReader.close();
            File file = new File("data/account_data/BookingData.csv");
            File destFile = new File("data/account_data/BookingData.csv");
            destFile.delete();
            file.renameTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
