package HandleStoreFiles;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HandleFilesReflection extends HandleFiles{

    public String getCSVDataRecursive(Object obj) throws IllegalAccessException {
        Class<?> c = obj.getClass();
        String s="";
        s+=c.getName()+",";
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);

            if(field.getType().isPrimitive()|| field.getType() == String.class){
                Object v=field.get(obj);
                s+= v+"," ;
            }
            else{
                Object inn = field.get(obj);
                s+=getCSVDataRecursive(inn);
            }
        }
        return s.substring(0,s.length()-1);
    }

    public void saveCSVObject(Object obj, String filePath) throws IOException, IllegalAccessException {
//        File folder = new File(filePath);
//        if (!folder.exists())
//            folder.mkdirs();
//        File dataFile = new File(folder, obj.getClass().getName() + ".txt");
        File dataFile = new File( filePath + ".txt");

        FileWriter writer = new FileWriter(dataFile, false);
        writer.write(getCSVDataRecursive(obj));
        writer.flush();
        writer.close();
        System.out.println("finish saveFile");
    }

    public Object readCsv(String filePath) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File dataFile = new File(filePath + ".txt");
        Scanner scanner =new Scanner(dataFile);
        scanner.useDelimiter(",");

        String className=scanner.next().trim();

        Class<?> clazz = Class.forName(className);
        Object newO = clazz.getDeclaredConstructor().newInstance();

        List<Field> allFields = new ArrayList<>();
        Field[] currentFields = clazz.getDeclaredFields();

        Field[] parentFields = clazz.getSuperclass().getDeclaredFields();

        allFields.addAll(Arrays.asList(parentFields));
        allFields.addAll(Arrays.asList(currentFields));

        ArrayList<Object> fieldValuesFromFile = new ArrayList<>();
        while (scanner.hasNext()) {
            fieldValuesFromFile.add(scanner.next());
        }

        System.out.println("DEBUG: מספר השדות שנמצאו במחלקה: " + fieldValuesFromFile.size());
        System.out.println("DEBUG: מספר הערכים שנקראו מהקובץ: " + allFields.size());
        for (int i = 0; i < fieldValuesFromFile.size() && i < allFields.size(); i++) {
            Field field = allFields.get(i);

            String value = fieldValuesFromFile.get(i).toString();
            System.out.println("DEBUG: מציב בשדה [" + field.getName() + "] את הערך: " + value);            field.setAccessible(true);

            if (field.getType() == Integer.class || field.getType() == int.class) {
                field.set(newO, Integer.parseInt(value));
            } else {
                field.set(newO, value);
            }
        }
        return newO;
    }

}
