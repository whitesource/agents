package org.whitesource.agent.api;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.whitesource.agent.api.model.DependencyInfo;

import java.io.*;
import java.util.Arrays;

/**
 * Created by anna.rozin
 */
public class FileHash {

    /* --- Static members --- */
    public static final int FILE_MIN_SIZE= 1024 * 2;
    public static final int FILE_SMALL_SIZE = 1024 * 3;
    public static final double FILE_SMALL_BUCKET_SIZE = 1024 * 1.25;

    /* --- Static methods --- */
    // remove -just for test
    public static void main(String[] args) throws IOException {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir("C:\\Users\\Anna\\Downloads\\signature-test-files\\signature-test-files\\");
        scanner.scan();
        String[] fileNames = scanner.getIncludedFiles();
//        for (String fileName : fileNames) {
//            File file = new File("C:\\Users\\Anna\\Downloads\\signature-test-files\\signature-test-files\\" + fileName);
            File file = new File("C:\\Users\\Anna\\Downloads\\signature-test-files\\signature-test-files\\" + "SMACKW32.DLL");
            DependencyInfo dependencyInfo = getFileHash(file, new DependencyInfo());
//        }

//        InputStream stream = new ByteArrayInputStream(exampleString.getBytes());
//        File file = new File("C:\\Users\\Anna\\Downloads\\signature-test-files\\signature-test-files\\SMACKW32.DLL");
//        System.out.println(ChecksumUtils.calculateSHA1(file));
    }

    public static DependencyInfo getFileHash(File file, DependencyInfo dependencyInfo) throws IOException {
        // Ignore files smaller than 2kb
        if (file.length() <= FILE_MIN_SIZE) {
            return null;
        }
        // Get file bytes
        byte[] fileToByte = FileUtils.readFileToByteArray(file);

        //Remove white spaces
        byte[] bytesWithoutSpaces = ChecksumUtils.stripWhiteSpaces(fileToByte);

        long fileSize = bytesWithoutSpaces.length;
        // Ignore files smaller than 2kb
        if (fileSize <= FILE_MIN_SIZE) {
            return null;
        }
        // Handle 2kb->3kb files
        else if (fileSize <= FILE_SMALL_SIZE) {
            return hashBuckets(bytesWithoutSpaces , FILE_SMALL_BUCKET_SIZE, dependencyInfo);
        }

        else if (file != null && fileSize > FILE_SMALL_SIZE) {
            int baseLowNumber = 1;
            int digits = (int) Math.log10(fileSize);
            int i = 0;
            while (i < digits) {
                baseLowNumber  = baseLowNumber * 10;
                i++;
            }
            System.out.println("File Name: "+ file.getName());
            System.out.println("-----------------------------------------------------------------");
            double highNumber = Math.ceil((fileSize + 1) / (float) baseLowNumber) * baseLowNumber;
            double lowNumber = highNumber - baseLowNumber;
            double bucketSize = (highNumber + lowNumber) / 4;
            System.out.println("High Number:  " +highNumber);
            System.out.println("Low Number:  " +lowNumber);
            System.out.println("bucket Size:  " +bucketSize);

            return hashBuckets(bytesWithoutSpaces, bucketSize, dependencyInfo);
        }
        return null;
    }

    private static DependencyInfo hashBuckets(byte[] fileWithoutSpaces, double bucketSize, DependencyInfo dependencyInfo) throws IOException {
        // int(bucket_size) will round down the bucket_size: IE: 1.2 -> 1.0
        int bucketIntSize = (int) bucketSize ;

        // Get bytes and calculate sha1
        byte [] mostSifBytes = Arrays.copyOfRange(fileWithoutSpaces, 0, bucketIntSize);
        int length = fileWithoutSpaces.length;
        byte [] listSigBytes = Arrays.copyOfRange(fileWithoutSpaces, length - bucketIntSize, length);
        dependencyInfo.setMostSigBitSha1(ChecksumUtils.calculateSHA1TEST(mostSifBytes));
        System.out.println("Most Sig Bit Sha1:  " +dependencyInfo.getMostSigBitSha1());
        dependencyInfo.setLeastSigBitSha1(ChecksumUtils.calculateSHA1TEST(listSigBytes));
        System.out.println("Least Sig Bit Sha1:  " +dependencyInfo.getLeastSigBitSha1());
        return dependencyInfo;
    }

    private static String getFileString(File file) {
        String fileToString = null;
        try {
           fileToString = FileUtils.readFileToString(file);
        } catch (IOException e) {
            //TODO add logs.
            e.printStackTrace();
        }
        return fileToString;
    }

}
