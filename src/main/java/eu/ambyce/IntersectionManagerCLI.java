package eu.ambyce;

import java.io.File;
import java.io.IOException;

public class IntersectionManagerCLI {
    private final IntersectionManager intersectionManager;
    private final File file;

    public IntersectionManagerCLI(File file) throws Exception {
        this.file = file;
        this.intersectionManager = new IntersectionManager();
        this.intersectionManager.loadIntersectionsFromFile(file);
    }

    public void run(String[] args) throws IOException, IllegalArgumentException {
        if (args.length < 1)
            throw new IllegalArgumentException("Program expects at least one argument! (Available usage: --list-items, --create <ID> <NAME>, --delete <ID>)");

        switch (args[0])
        {
            case "--list-items":
                listIntersections();
                break;
            case "--create":
                createIntersection(args);
                this.intersectionManager.saveIntersectionsToFile(this.file);
                break;
            case "--delete":
                deleteIntersection(args);
                this.intersectionManager.saveIntersectionsToFile(this.file);
                break;
            default:
                throw new IllegalArgumentException("First passed argument is not valid! (Available usage: --list-items, --create <ID> <NAME>, --delete <ID>)");
        }
    }

    public void listIntersections() {
        System.out.println("[INFO] Listing persisted intersections...");
        for (Intersection intersection : this.intersectionManager.getIntersections())
            System.out.println(" - (#" + intersection.id() + ") " +  intersection.name());
    }

    public void createIntersection(String[] args) {
        if (args.length < 3)
            throw new IllegalArgumentException("Command line argument '--create' should be in format '--create <ID> <NAME>'!");
        else if (args.length > 3)
            throw new IllegalArgumentException("Command line argument '--create' should be in format '--create <ID> <NAME>', check your usage because you passed more than 3 arguments!");

        int id = validateIdArgument(args[1]);
        String name = args[2];

        if (String.valueOf(id).length() < 4 || String.valueOf(id).length() > 5)
            throw new IllegalArgumentException("Command line argument '--create <ID> <NAME>' expects ID to have 4-5 digits!");

        Intersection intersection = new Intersection(id, name);
        this.intersectionManager.addIntersection(intersection);
        System.out.println("[INFO] Successfully created new intersection '" + intersection.name() + "' (#" + intersection.id() + ").");
    }

    public void deleteIntersection(String[] args) throws IllegalArgumentException {
        if (args.length < 2)
            throw new IllegalArgumentException("Command line argument '--delete' should be in format '--delete <ID>'!");
        else if (args.length > 2)
            throw new IllegalArgumentException("Command line argument '--delete' should be in format '--delete <ID>', check your usage because you passed more than 2 arguments!");

        int id = validateIdArgument(args[1]);

        this.intersectionManager.deleteIntersection(id);
        System.out.println("[INFO] Successfully deleted intersection #" + id + ".");
    }

    private int validateIdArgument(String stringId) throws IllegalArgumentException {
        int id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("ID of intersection must be an integer!");
        }

        return id;
    }
}
