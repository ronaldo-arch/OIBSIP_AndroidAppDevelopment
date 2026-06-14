package com.intern.quizapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Single quiz question. */
public class Question {
    public final String text;
    public final String[] options;   // exactly 4
    public final int correctIndex;   // 0..3

    public Question(String text, String[] options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    /** Built-in 15-question general knowledge bank. */
    public static List<Question> bank() {
        List<Question> q = new ArrayList<>();
        q.add(new Question("Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Saturn"}, 1));
        q.add(new Question("Who wrote 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "Mark Twain", "William Shakespeare", "Jane Austen"}, 2));
        q.add(new Question("What is the largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
        q.add(new Question("Which gas do plants absorb from the atmosphere?",
                new String[]{"Oxygen", "Nitrogen", "Carbon dioxide", "Hydrogen"}, 2));
        q.add(new Question("The Great Wall of China is visible from which country to the north?",
                new String[]{"Russia", "Mongolia", "Japan", "India"}, 1));
        q.add(new Question("Which language has the most native speakers in the world?",
                new String[]{"English", "Hindi", "Spanish", "Mandarin Chinese"}, 3));
        q.add(new Question("In computing, what does CPU stand for?",
                new String[]{"Central Processing Unit", "Computer Personal Unit",
                        "Central Performance Utility", "Control Process Unit"}, 0));
        q.add(new Question("Mount Everest lies on the border between Nepal and which country?",
                new String[]{"India", "Bhutan", "China", "Pakistan"}, 2));
        q.add(new Question("Who painted the Mona Lisa?",
                new String[]{"Vincent van Gogh", "Pablo Picasso",
                        "Leonardo da Vinci", "Claude Monet"}, 2));
        q.add(new Question("What is the chemical symbol for gold?",
                new String[]{"Gd", "Go", "Au", "Ag"}, 2));
        q.add(new Question("Which country invented paper?",
                new String[]{"Egypt", "China", "Greece", "India"}, 1));
        q.add(new Question("How many continents are there?",
                new String[]{"5", "6", "7", "8"}, 2));
        q.add(new Question("Which is the smallest prime number?",
                new String[]{"0", "1", "2", "3"}, 2));
        q.add(new Question("Who is known as the father of computers?",
                new String[]{"Alan Turing", "Charles Babbage",
                        "Bill Gates", "Steve Jobs"}, 1));
        q.add(new Question("What is the speed of light in vacuum (approx)?",
                new String[]{"3 × 10⁵ m/s", "3 × 10⁸ m/s",
                        "3 × 10⁶ m/s", "3 × 10¹⁰ m/s"}, 1));
        Collections.shuffle(q);
        return q;
    }
}
