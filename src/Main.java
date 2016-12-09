/**
 * Created by gentryrolofson on 12/6/16.
 */

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws IOException {
        // new Instance to handle file.
        File saveFile = new File("json/leaderboard.json");
        // Create folders that don't exist yet.
        saveFile.getParentFile().mkdirs();

        // Create a new Leaderboard instance, load from the file if available
        Leaderboard board = new Leaderboard(saveFile.getAbsolutePath());

        Scanner scanner = new Scanner(System.in);
        // Add a new score
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        int score;
        System.out.print("Enter Score: ");
        while(true) {
//
            String input = scanner.nextLine().replace("\n","");
            try {
                score = Integer.parseInt(input);
                break;
            } catch(NumberFormatException e) {
                System.err.print("Enter in a number: ");
            }
        }
        board.addScore(new Leaderboard.Item(name,score));

        // Save the Leaderboard
        board.store();

        // Print it out to the console.
//        System.out.println(board);
        board.print();
    }
}