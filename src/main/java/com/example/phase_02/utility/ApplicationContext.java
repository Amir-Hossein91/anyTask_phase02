package com.example.phase_02.utility;

import com.github.mfathi91.time.PersianDate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationContext {
    public static final Path outputPath;
    public static final Path inputPath;
    public static final String sourceAddress;
    public static final String imageName;
    public static final String imageExtension;
    public static final PersianDate currentPersianDate;
    public static final LocalDate currentDate;
    public static final Printer printer;
    public static final Scanner input;

    static{
        sourceAddress = "image_input";
        imageName = "technician_01";
        imageExtension = "js";
        inputPath = Paths.get(sourceAddress,imageName+"."+imageExtension);
        outputPath = Paths.get("image_output/" + imageName + ".jpg");
        currentPersianDate = PersianDate.now();
        currentDate = currentPersianDate.toGregorian();
        printer = new Printer();
        input = new Scanner(System.in);
    }
}
