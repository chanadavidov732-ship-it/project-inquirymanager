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
import java.util.ArrayList;
import java.util.Scanner;

public class HandleFilesReflection extends HandleFiles{
    public String getCSVDataRecursive(Object obj) throws IllegalAccessException {
        Class c;
        c=obj.getClass();
        String s="";
        s+=c.getSimpleName()+",";
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

    public Object readCsv(String filePath) throws FileNotFoundException {
        File dataFile = new File(filePath);
        Inquiry newO;

        Scanner scanner =new   Scanner(dataFile);
        scanner.useDelimiter(",");
        String className=scanner.next();

        ArrayList<Object> fieldes = new ArrayList<>();
        while (scanner.hasNext()) {
            fieldes.add(scanner.next());
        }

        if(className.equals("Question"))
            newO = new Question(Integer.parseInt(fieldes.get(0).toString()), fieldes.get(1).toString());
        else
            if(className.equals("Request"))
                newO=new Request(Integer.parseInt(fieldes.get(0).toString()), fieldes.get(1).toString());
            else
            if(className.equals("Complaint"))
                newO=new Complaint(Integer.parseInt(fieldes.get(0).toString()), fieldes.get(1).toString(), fieldes.get(2).toString());
        else
            newO=new Inquiry();


        return newO;
    }

}
