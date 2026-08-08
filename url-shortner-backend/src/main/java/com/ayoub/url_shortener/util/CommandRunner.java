package com.ayoub.url_shortener.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandRunner {
    private static final String SCRIPT_PATH = "src/main/resources/python/generate_qr_code.py";


    public static Optional<String> runPythonScript(String url){
        String[] command = {"py", SCRIPT_PATH, url};

        ProcessBuilder pb = new ProcessBuilder(command);

        pb.redirectErrorStream(true);

        List<String> outputLines = new ArrayList<>();




        try{
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;

            while((line = reader.readLine()) != null){
                System.out.println("Python Output: " + line);
                outputLines.add(line);
            }

            int exitCode = process.waitFor();

            if(exitCode != 0  || outputLines.isEmpty()){
                System.err.println("Python script execution failed with exit code: " + exitCode);
                return Optional.empty();
            }


            String generatedPath = outputLines.getLast();
            return Optional.of(generatedPath);

        } catch (IOException e) {
            System.err.println("Command execution failed: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return Optional.empty();
    }




}
