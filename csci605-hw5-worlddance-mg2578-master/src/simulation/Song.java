/*
Homework 5 : World of Dance
File Name : Song.java
 */
package simulation;

/**
 * A class to represent the song, each song has an artist and a title.
 *
 * @author Meenu Gigi, mg2578@rit.edu
 * @author Vedika Vishwanath Painjane, vp2312@rit.edu
 */
public class Song implements Comparable<Song>{

    /**
     * Title of the song
     */
    private final String title;
    /**
     * Artist of the song
     */
    private final String artist;

    /**
     * Constructor Initialization
     * @param artist Artist name
     * @param title Song title
     */
    public Song(String artist, String title){
        this.artist = artist;
        this.title = title;
    }

    /**
     * Getter, title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter, artist
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * String format of the song
     * @return song information, artist and titile
     */
    @Override
    public String toString() {
        return "Artist: " + getArtist() + ", Title: " + getTitle();
    }

    /**
     * Comparing songs, if artist is same, then comparing the title
     * @param song  song
     * @return integer value
     */
    @Override
    public int compareTo(Song song) {
        int result = this.artist.compareTo((song).getArtist());
        if(result == 0){
            result = this.title.compareTo((song).getTitle());
        }
        return result;
    }

    /**
     * Two songs are equal or not
     * @param o Object(Song)
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song)) return false;
        else{
            return this.artist.equals(((Song) o).getArtist()) &&
                    this.title.equals(((Song) o).getTitle());
        }
    }

    /**
     * Calculates the hashcode of the song
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return artist.hashCode() + title.hashCode();
    }

}
