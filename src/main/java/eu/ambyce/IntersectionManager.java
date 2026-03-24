package eu.ambyce;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntersectionManager {
    private ArrayList<Intersection> intersections = new ArrayList<>();

    public List<Intersection> getIntersections() {
        return Collections.unmodifiableList(intersections);
    }

    public void addIntersection(Intersection intersection) throws IllegalArgumentException {
        for (Intersection loopIntersection : intersections) {
            if (loopIntersection.id() == intersection.id()) {
                throw new IllegalArgumentException("Intersection with ID '" + intersection.id() + "' already exists!");
            }
        }

        intersections.add(intersection);
    }

    public void deleteIntersection(int id) throws IllegalArgumentException {
        Intersection foundIntersection = null;
        for (Intersection intersection : intersections) {
            if (intersection.id() == id) {
                foundIntersection = intersection;
            }
        }

        if (foundIntersection == null)
            throw new IllegalArgumentException("Intersection with ID '" + id + "' does not exist!");

        intersections.remove(foundIntersection);
    }

    public void loadIntersectionsFromFile(File file) throws IOException {
        file.createNewFile();

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Intersection>>(){}.getType();
        try (Reader reader = new FileReader(file)) {
            ArrayList<Intersection> loadedIntersections = gson.fromJson(reader, listType);
            if (loadedIntersections != null)
                intersections = loadedIntersections;
        }
    }

    public void saveIntersectionsToFile(File file) throws IOException {
        file.createNewFile();

        Gson gson = new Gson();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(intersections, writer);
        }
    }
}
