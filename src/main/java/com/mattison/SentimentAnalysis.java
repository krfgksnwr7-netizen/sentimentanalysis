package com.mattison;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SentimentAnalysis {

    public static void main(String[] args) {

        // Create the hash table for sentiment ratings
        HashTable<String, Integer> sentiments = new HashTable<>(100);

        // Read the sentiment file and store the ratings
        try (Scanner file = new Scanner(new File("sentiments.txt"))) {

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Split the phrase and rating at the comma
                String[] parts = line.split(",", 2);

                if (parts.length == 2) {
                    String phrase = parts[0].trim().toLowerCase();
                    int rating = Integer.parseInt(parts[1].trim());

                    sentiments.put(phrase, rating);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find sentiments.txt");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating in sentiments.txt");
            return;
        }

        // Read text from the keyboard
        Scanner keyboard = new Scanner(System.in);

        int wordCount = 0;
        int totalSentiment = 0;
        String previousWord = "";

        System.out.println("Enter text:");

        // Continue reading until END is entered
        while (keyboard.hasNextLine()) {
            String input = keyboard.nextLine();

            if (input.trim().equals("END")) {
                break;
            }

            // Remove punctuation and convert to lowercase
            String cleaned = input.replaceAll(
                    "[^a-zA-Z ]", ""
            ).toLowerCase().trim();

            if (cleaned.isEmpty()) {
                continue;
            }

            String[] words = cleaned.split("\\s+");

            for (String word : words) {
                wordCount++;

                // Add the score for an individual word
                Integer score = sentiments.get(word);

                if (score != null) {
                    totalSentiment += score;
                }

                // Check for two-word phrases
                if (!previousWord.isEmpty()) {
                    String phrase = previousWord + " " + word;

                    Integer phraseScore = sentiments.get(phrase);

                    if (phraseScore != null) {
                        totalSentiment += phraseScore;
                    }
                }

                previousWord = word;
            }
        }

        // Calculate the average sentiment
        double average = 0.0;

        if (wordCount > 0) {
            average = (double) totalSentiment / wordCount;
        }

        // Display the results
        System.out.println("Words: " + wordCount);
        System.out.println("Sentiment: " + totalSentiment);
        System.out.printf("Overall: %.2f%n", average);

        keyboard.close();
    }
}
