package eu.ambyce;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Main {
    private static ArrayList<Intersection> intersections;

    public static void main(String[] args) {
        try {
            if (args.length < 1)
                throw new IllegalArgumentException("Program expects at least one argument! (Available usage: --list-items, --create <ID> <NAME>, --delete <ID>)");

            File file = new File("intersections.json");
            intersections = loadIntersections(file);
            if (intersections == null)
                intersections = new ArrayList<>();

            switch (args[0])
            {
                case "--list-items":
                    listIntersections(intersections);
                    break;
                case "--create":
                    createIntersection(args);
                    saveIntersections(intersections, file);
                    break;
                case "--delete":
                    deleteIntersection(args);
                    saveIntersections(intersections, file);
                    break;
                default:
                    throw new IllegalArgumentException("First passed argument is not valid! (Available usage: --list-items, --create <ID> <NAME>, --delete <ID>)");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected failure.");
            e.printStackTrace(System.err);
        }
    }

    private static ArrayList<Intersection> loadIntersections(File file) throws Exception {
        if (!file.exists())
            file.createNewFile();

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Intersection>>(){}.getType();
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, listType);
        }
    }

    private static void saveIntersections(ArrayList<Intersection> intersections, File file) throws Exception {
        if (!file.exists())
            file.createNewFile();

        Gson gson = new Gson();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(intersections, writer);
        }
    }

    private static void listIntersections(ArrayList<Intersection> intersections) throws Exception {
        if (intersections.isEmpty())
            throw new Exception("No intersections were persisted (the file is empty)!");

        System.out.println("[INFO] Listing persisted intersections...");
        for (Intersection intersection : intersections)
            System.out.println(" - (#" + intersection.getId() + ") " +  intersection.getName());
    }

    private static void createIntersection(String[] args) throws IllegalArgumentException {
        if (args.length < 3)
            throw new IllegalArgumentException("Command line argument '--create' should be in format '--create <ID> <NAME>'!");
        else if (args.length > 3)
            throw new IllegalArgumentException("Command line argument '--create' should be in format '--create <ID> <NAME>', check your usage because you passed more than 3 arguments!");

        int id = validateIdArgument(args[1]);
        String name = args[2];

        if (id < 1000 || id > 99999)
            throw new IllegalArgumentException("Command line argument '--create <ID> <NAME>' expects ID to have 4-5 digits!");

        for (Intersection intersection : intersections) {
            if (intersection.getId() == id) {
                throw new IllegalArgumentException("Intersection with ID '" + id + "' already exists!");
            }
        }

        Intersection intersection = new Intersection(id, name);
        intersections.add(intersection);
        System.out.println("[INFO] Successfully added new intersection '" + intersection.getName() + "' (#" + intersection.getId() + ").");
    }

    private static void deleteIntersection(String[] args) throws IllegalArgumentException {
        if (args.length < 2)
            throw new IllegalArgumentException("Command line argument '--delete' should be in format '--delete <ID>'!");
        else if (args.length > 2)
            throw new IllegalArgumentException("Command line argument '--delete' should be in format '--delete <ID>', check your usage because you passed more than 2 arguments!");

        int id = validateIdArgument(args[1]);

        Intersection foundIntersection = null;
        for (Intersection intersection : intersections) {
            if (intersection.getId() == id) {
                foundIntersection = intersection;
            }
        }

        if (foundIntersection == null)
            throw new IllegalArgumentException("Intersection with ID '" + id + "' does not exist!");

        intersections.remove(foundIntersection);
        System.out.println("[INFO] Successfully deleted intersection '" + foundIntersection.getName() + "' (#" + foundIntersection.getId() + ").");
    }

    private static int validateIdArgument(String stringId) throws IllegalArgumentException {
        int id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("ID of intersection must be an integer!");
        }

        return id;
    }
}