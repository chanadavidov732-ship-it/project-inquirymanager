package testSaveFile;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.HandleFilesReflection;

import java.io.IOException;
import java.util.Arrays;

public class TestingHandleFiles {
    public static void main(String[] args) throws IOException, IllegalAccessException {
//        PersonForTestSaving p1 = new PersonForTestSaving("1234","aaa");
//        PersonForTestSaving p2 = new PersonForTestSaving("5432","bbb");
//        PersonForTestSaving p3 = new PersonForTestSaving("9999","ccc");
//        PersonForTestSaving p4 = new PersonForTestSaving("0090","ccdc");
//
//        HandleFiles handleFiles = new HandleFiles();
//        handleFiles.saveFile(p3);
//        handleFiles.saveFiles(Arrays.asList(p1,p2,p3,p4));
//
//        handleFiles.deleteFile(p2);

        HandleFilesReflection handleFiles2 =new HandleFilesReflection();
        PersonForTestSaving p5 = new PersonForTestSaving("123456789","sucess BH!");
        handleFiles2.saveCSVObject(p5, p5.getId() + ".p");




    }
}
