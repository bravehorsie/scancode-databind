package com.oracle.scancodedatabind;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("java -jar scancode-databind.jar absolute-path-to-json absolute-path-to-out-file");
            System.out.println("There are two out files generated for \"with license\" and \"without license\" files)");
            return;
        }

        Runner runner = new Runner(args[0], args[1]);
        runner.run();
    }


}
