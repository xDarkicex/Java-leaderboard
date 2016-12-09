import java.io.*;
import java.util.ArrayList;
import javax.json.*;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Created by gentryrolofson on 12/6/16.
 */

public class Leaderboard {
    public static class Item {
        public String name;
        public int score;
        Item(String name, int score) {
            this.name = name;
            this.score = score;
        }
        public JsonObject toJSON() {
            return Json.createObjectBuilder()
                .add("name", name)
                .add("score", score)
                .build();
        }
    }
    public ArrayList<Item> scores = new ArrayList<Item>();
    public String fileName;
    Leaderboard(){
       this("board.json");
    }
    Leaderboard(String fileName) {
        this.fileName = fileName;
        //
        load();
    }
    public void addScore(Item score) {

        scores.add(score);
        scores.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return (o1.score < o2.score)?1:-1;
            }
        });
        scores.removeIf(new Predicate<Item>() {
            @Override
            public boolean test(Item item) {
                return scores.indexOf(item) >= 10;
            }
        });
    }
    public void store() {
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(this);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e);
        }
    }
    public void load() {
        try {
            JsonReader reader = Json.createReader(new FileReader(fileName));
            JsonObject jsonst = (JsonObject) reader.read();
            JsonArray jsonScores = (JsonArray) jsonst.get("scores");

            for (JsonValue value : jsonScores) {
                JsonObject score = (JsonObject) value;
                addScore(new Item(((JsonString) score.get("name")).getString(), ((JsonNumber) score.get("score")).intValue()));
            }
        }
        catch(IOException e) {
            System.err.println("Error reading file: " + e);
        }

    }
    public JsonObject toJSON() {
        JsonArrayBuilder jsonScores = Json.createArrayBuilder();
        for(Item score : scores)
            jsonScores.add(score.toJSON());
        return Json.createObjectBuilder()
            .add("scores", jsonScores)
            .build();
    }
    public String toString() {
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(toJSON());
        jsonWriter.close();
        return stWriter.toString();
    }
    public void print(){
        File file = new File(fileName);
        System.out.printf("%40s\n","High Scores for " + file.getName());
        System.out.printf("%5s%15s%20s\n","Rank","Name","Score");
        System.out.println("-----------------------------------------");
        for(Item score : scores)
            System.out.printf("%5d%15s%20s\n",scores.indexOf(score)+1,score.name,score.score);
    }
}