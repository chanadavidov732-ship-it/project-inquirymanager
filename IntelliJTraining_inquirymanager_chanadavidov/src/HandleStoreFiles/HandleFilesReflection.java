package HandleStoreFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class HandleFilesReflection extends HandleFiles{
    public String getCSVDataRecursive(Object obj) throws IllegalAccessException {
        Class c;
        c=obj.getClass();
        String s="";
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
        return s;
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



}
