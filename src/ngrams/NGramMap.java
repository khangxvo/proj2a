package ngrams;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;
import edu.princeton.cs.algs4.In;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private HashMap<String, TimeSeries> wordsTimeSeries;
    private TimeSeries yearlyTotalCounts;
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordsTimeSeries = new HashMap<>();
        yearlyTotalCounts = new TimeSeries();

        In input = new In(wordsFilename);
        while (input.hasNextLine()) {
            String nextLine = input.readLine();
            String[] splitLine = nextLine.split("\t");
            String word = splitLine[0];
            int year = Integer.parseInt(splitLine[1]);
            double count = Double.parseDouble(splitLine[2]);
            if (wordsTimeSeries.containsKey(word)) {
                wordsTimeSeries.get(word).put(year, count);
            } else {
                wordsTimeSeries.put(word, new TimeSeries());
                wordsTimeSeries.get(word).put(year, count);
            }
        }

        input = new In(countsFilename);
        while (input.hasNextLine()) {
            String nextLine = input.readLine();
            String[] splitLine = nextLine.split(",");
            int year = Integer.parseInt(splitLine[0]);
            Double count = Double.parseDouble(splitLine[1]);
            yearlyTotalCounts.put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(wordsTimeSeries.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return wordsTimeSeries.get(word);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return yearlyTotalCounts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    /**
     * Take (counts of word in that year) / (total words count that year)
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordsTimeSeries.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = new TimeSeries(wordsTimeSeries.get(word), startYear, endYear);
        return ts.dividedBy(yearlyTotalCounts);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */

    /**
     * Take (counts of word in that year) / (total words count that year)
     */
    public TimeSeries weightHistory(String word) {
        if (!wordsTimeSeries.containsKey(word)) {
            return new TimeSeries();
        }
        return wordsTimeSeries.get(word).dividedBy(yearlyTotalCounts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    /**
     * Create new TimeSeries
     * Take (relative frequency of that year) + (other words relative frequency in that year)
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        return new TimeSeries(summedWeightHistory(words), startYear, endYear);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    /**
     * Create new TimeSeries
     * Take (relative frequency of that year) + (other words relative frequency in that year)
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ts = new TimeSeries();
        for (String word : words) {
            if (wordsTimeSeries.containsKey(word)) {
                ts = ts.plus(weightHistory(word));
            }
        }
        return ts;
    }
}
