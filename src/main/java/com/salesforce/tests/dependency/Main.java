package com.salesforce.tests.dependency;

import com.salesforce.tests.dependency.parsing.CommandLineParser;
import com.salesforce.tests.dependency.service.ComponentManagementService;
import com.salesforce.tests.dependency.service.DefaultComponentManagementService;

import java.util.Scanner;

/**
 * The entry point for the Test program
 */
public class Main {

    public static void main(String[] args) {
        final ComponentManagementService service = new DefaultComponentManagementService();
        //read input from stdin
        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }

            //the END commandType to stop the program
            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }

            //Please provide your implementation here
           service.executeCommand(new CommandLineParser().parse(line));
        }

    }
}