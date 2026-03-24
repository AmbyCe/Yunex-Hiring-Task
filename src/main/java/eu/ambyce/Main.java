package eu.ambyce;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("intersections.json");
            IntersectionManagerCLI intersectionManagerCLI = new IntersectionManagerCLI(file);
            intersectionManagerCLI.run(args);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected failure.");
            e.printStackTrace(System.err);
        }
    }
}