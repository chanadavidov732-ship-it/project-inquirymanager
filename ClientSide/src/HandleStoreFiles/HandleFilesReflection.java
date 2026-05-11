package HandleStoreFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HandleFilesReflection extends HandleFiles {

    public String getCSVDataRecursive(Object obj) throws IllegalAccessException {
        Class<?> c = obj.getClass();
        String s = "";
        s += c.getName() + ",";

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getType().isPrimitive() || field.getType() == String.class) {
                Object v = field.get(obj);
                s += v + ",";
            } else {
                Object inn = field.get(obj);
                s += getCSVDataRecursive(inn);
            }
        }
        return s.substring(0, s.length() - 1);
    }

    public void saveCSVObject(Object obj, String filePath) throws IOException, IllegalAccessException {
        File dataFile = new File(filePath + ".txt");

        if (dataFile.getParentFile() != null) {
            dataFile.getParentFile().mkdirs();
        }
        FileWriter writer = new FileWriter(dataFile, false);
        writer.write(getCSVDataRecursive(obj));
        writer.flush();
        writer.close();
        System.out.println("finish saveFile: " + filePath);
    }



    public void deleteCsv(String filePath) throws IOException {
        Path path = Paths.get(filePath + ".txt");
        Files.delete(path);
    }

    public Object readCsv(String filePath) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File dataFile = new File(filePath + ".txt");
        Scanner scanner = new Scanner(dataFile);
        scanner.useDelimiter(",");

        String className = scanner.next().trim();
        Class<?> clazz = Class.forName(className);
        Object newO = clazz.getDeclaredConstructor().newInstance();

        List<Field> allFields = new ArrayList<>();
        Field[] currentFields = clazz.getDeclaredFields();
        Field[] parentFields = clazz.getSuperclass().getDeclaredFields();

        for (Field f : parentFields) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                allFields.add(f);
            }
        }
        for (Field f : currentFields) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                allFields.add(f);
            }
        }

        ArrayList<Object> fieldValuesFromFile = new ArrayList<>();
        while (scanner.hasNext()) {
            fieldValuesFromFile.add(scanner.next());
        }

        for (int i = 0; i < fieldValuesFromFile.size() && i < allFields.size(); i++) {
            Field field = allFields.get(i);
            field.setAccessible(true);
            String value = fieldValuesFromFile.get(i).toString();

            if (field.getType() == Integer.class || field.getType() == int.class) {
                field.set(newO, Integer.parseInt(value));
            } else if(field.getType() == LocalDateTime.class){
                field.set(newO, LocalDateTime.parse(value));
            } else {
                field.set(newO, value);
            }
        }
        scanner.close();
        return newO;
    }
}
