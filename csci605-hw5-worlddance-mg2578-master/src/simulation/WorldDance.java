/*
Homework 5 : World of Dance
File Name : WorldDance.java
 */
package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.currentTimeMillis;

/**
 * The main class WorldDance is run on command line as
 * $ java WorldDance filename seed
 *
 * This class does the simulation on the given input files and prints the
 * output.
 * @author Meenu Gigi, mg2578@rit.edu
 * @author Vedika Vishwanath Painjane, vp2312@rit.edu
 */
public class WorldDance {

    /** Stores number of times to run simulation */
    private static final int NUMSIMULATIONS = 50000;
    /**
     * Stores the song and the number of times it is played as key and
     * value respectively when we run the simulation 50000 times.
     */
    private final Map<Song, Integer> jukeBoxMap  = new LinkedHashMap<>();

    /** Used to store the input from the text file */
    private final Set<Song> jukeBoxSet  = new LinkedHashSet<>();

    /** Used to store filename received from command-line arguments */
    private static String filename;

    /** Used to generate random numbers. */
    private static final Random rnd = new Random();

    /** Used to create class instance */
    static WorldDance worldDance;


    /**
     * Constructor initialization
     * @param filename      txt file
     * @param seed      Seed value
     * @throws FileNotFoundException    if the required file is not found.
     */
    public WorldDance(String filename, long seed) throws FileNotFoundException {
        setSeed(seed);
        this.filename = filename;
    }

    /**
     * Setting the seed for the pseudorandom generator
     * @param   seed      the seed
     */
    private static void setSeed(long seed) {
        rnd.setSeed(seed);
    }

    /**
     * reads input file and seed value.
     * iterates through the input file and stores data in a Set.
     * @throws   FileNotFoundException if the required file is not found.
     */
    private void readFile() throws FileNotFoundException {
        Song song;
        String artist;
        String songTitle;
        Scanner in = new Scanner(new File(filename));
//        iterates through songs in track files and stores Songs in jukebox (set).
        while(in.hasNext()){
            String str = in.nextLine();
            String []arrOfLine = str.split("<SEP>", 4);
            artist = arrOfLine[2];
            songTitle = arrOfLine[3];
            song = new Song(artist, songTitle);
            jukeBoxSet.add(song);
        }
        in.close();
    }


    /**
     * main method processes command line arguments, if any, and
     * executes the program.
     *
     * @param    args      command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        long seed;
//        verifies command-line arguments.
        if(args.length != 2){
            System.out.println("Usage: java WorldDance filename seed");
            System.exit(0);
        }
        else{
            filename = args[0];
            seed = Long.parseLong(args[1]);
            worldDance = new WorldDance(filename, seed);
            worldDance.readFile();
            runSimulation(worldDance.jukeBoxSet);
        }
    }


    /**
     * runs simulation 50000 times.
     * checks if a duplicate song is encountered.
     * if duplicate found, breaks iteration.
     * else, adds song to set.
     * if song not present in map, add song with default value 0.
     * else, increment value by 1.
     * compute total number of songs played.
     *
     * @param    jukeBoxSet     stores all songs
     */
    private static void runSimulation(Set<Song> jukeBoxSet) {

//        a list to store all Songs.
        List<Song> songList = new ArrayList<>( jukeBoxSet );
//        a set to catch duplicate songs.
        Set<Song> duplicateSet = new HashSet<>();
        long startTime = currentTimeMillis();
//        tracks total number of songs played.
        int numSongsPlayed = 0;

        for(int i = 0; i<NUMSIMULATIONS; i++){
            while(true){
                int randomIndex = rnd.nextInt(songList.size());
                Song currPlaying = songList.get(randomIndex);
                if (duplicateSet.contains(currPlaying)){
                    break;
                }
                else{
                    duplicateSet.add(currPlaying);
                }
//                stores Song in jukebox (map) with default value 0.
//                increments value by 1 each time a duplicate is encountered.
                worldDance.jukeBoxMap.put(songList.get(randomIndex), worldDance.jukeBoxMap.getOrDefault(songList.get(randomIndex), 0) +1);
            }
//            calculate number of songs played.
            numSongsPlayed += duplicateSet.size();
            duplicateSet.clear();
        }
        long endTime = currentTimeMillis();
        worldDance.displayStatistics(startTime, endTime, numSongsPlayed);
    }


    /**
     * displays statistical information.
     * @param    numSongsPlayed     number of times song has been played
     */
    private void displayStatistics(long startTime, long endTime, int numSongsPlayed){
        Song firstSong = getFirstSong(jukeBoxSet);
        Song lastSong = getLastSong(jukeBoxSet);

        System.out.println("Loading the jukebox with songs:\n" +
                "\tReading songs from " + filename +" into jukebox...\n" +
                "\tJukebox is loaded with " + jukeBoxSet.size() + " songs\n" +
                "\tFirst song in jukebox: Artist: " + firstSong.getArtist() + ", Title: " + firstSong.getTitle() + "\n" +
                "\tLast song in jukebox: Artist: " + lastSong.getArtist() + ", Title: " + lastSong.getTitle() + "\n" +
                "Running the simulation. Jukebox starts rockin'...\n" +
                "\tPrinting first 5 songs played...");
//        to print first five songs played.
        getFirstFiveSongs();
        System.out.println("\tSimulation took "+ (endTime - startTime)/1000 +" second/s");
        System.out.println("Displaying simulation statistics:\n" +
                "\tNumber of simulations run: 50000\n" +
                "\tTotal number of songs played: " +numSongsPlayed + "\n" +
                "\tAverage number of songs played per simulation to get duplicate: " +numSongsPlayed/50000);
//        to obtains song played maximum number of times.
        getMostPlayedSong();
    }


    /**
     * iterates through map containing songs to retrieve first five songs played.
     * if size of map is less than 5, print available song 5 times.
     */
    private void getFirstFiveSongs(){
        int index = 0;
        for (Song key : jukeBoxMap.keySet()) {
            if(jukeBoxMap.size() < 5){
                for(int i=0; i <5; i++){
                    System.out.println("\t\t" +key);
                }
            }
            else{
                index++;
                System.out.println("\t\t" +key);
                if (index == 5) {
                    break;
                }
            }
        }
    }


    /**
     * retrieves song played maximum number of times.
     */
    private void getMostPlayedSong(){
        String mostPlayedSongTitle = null;
        String artistOfMostPlayedSong = null;

//        obtain maximum value from hashmap.
        int maxValueInMap = (Collections.max(jukeBoxMap.values()));
        for (Map.Entry<Song, Integer> entry : jukeBoxMap.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                artistOfMostPlayedSong = entry.getKey().getArtist();
                mostPlayedSongTitle = entry.getKey().getTitle();
            }
        }
        System.out.println("\tMost played song: " + '\u0022' + mostPlayedSongTitle + '\u0022' + " by " + '\u0022' + artistOfMostPlayedSong + '\u0022');
        songsAlphabetically(artistOfMostPlayedSong);
    }


    /**
     * stores song in alphabetical order using tree set.
     * iterates through map to find artist of song with maximum plays.
     * on finding artist, stores all songs of artist in tree set.
     * displays songs with corresponding number of plays.
     * @param    artistOfMostPlayedSong      artist of song with maximum number of plays.
     */
    private void songsAlphabetically(String artistOfMostPlayedSong){
//        to store songs in alphabetical order.
        Set<Song> songsByArtistTreeSet = new TreeSet<>();
        for(Map.Entry<Song, Integer> entry : jukeBoxMap.entrySet()){
            if (entry.getKey().getArtist().equals(artistOfMostPlayedSong)){
                songsByArtistTreeSet.add(entry.getKey());
            }
        }
        System.out.println("\tAll songs alphabetically by " +'\u0022' + artistOfMostPlayedSong + '\u0022' +":");
//        to display song along with number of times song has been played.
        for(Song song : songsByArtistTreeSet){
            if(jukeBoxMap.containsKey(song)){
                System.out.println("\t\t" + '\u0022' + song.getTitle() + '\u0022' + " with " + jukeBoxMap.get(song) + " plays");
            }
        }
    }


    /**
     * retrieves last song from list of songs.
     * @param    jukeBoxSet      stores list of songs.
     */
    private static Song getLastSong(Set<Song> jukeBoxSet) {
        int index = 0;
        for (Song key : jukeBoxSet) {
            index++;
            if (index == jukeBoxSet.size()) {
                return key;
            }
        }
        return null;
    }

    /**
     * retrieves first song from list of songs.
     * @param    jukeBoxSet      stores list of songs.
     */
    private static Song getFirstSong(Set<Song> jukeBoxSet) {
        for (Song key : jukeBoxSet) {
            return key;
        }
        return null;
    }
}