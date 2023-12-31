package ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        //        for (int start = startYear; start <= endYear; start++) {
        //            this.put(start, ts.get(start));
        //        }
        NavigableMap<Integer, Double> newTS = ts.subMap(startYear, true, endYear, true);
        for (Integer i : newTS.keySet()) {
            this.put(i, ts.get(i));
        }

    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> years = new ArrayList<>();
        Set<Integer> setYears = keySet();
        for (int y : setYears) {
            years.add(y);
        }
        return years;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Integer> years = years();
        List<Double> data = new ArrayList<>();
        for (int y : years) {
            double d = get(y);
            data.add(d);
        }
        return data;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries sum = new TimeSeries();
        List<Integer> sumYear = years();
        /**
         * Create an array that contain years of both ts.
         */
        for (int y : ts.years()) {
            if (!sumYear.contains(y)) {
                sumYear.add(y);
            }
        }

        for (int y : sumYear) {
            if (!containsKey(y)) {
                sum.put(y, ts.get(y));
            } else if (!ts.containsKey(y)) {
                sum.put(y, get(y));
            } else {
                sum.put(y, ts.get(y) + get(y));
            }
        }

        return sum;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries quotedTS = new TimeSeries();
        for (int y : years()) {
            if (!ts.containsKey(y)) {
                throw new IllegalArgumentException();
            } else {
                quotedTS.put(y, get(y) / ts.get(y));
            }
        }
        return quotedTS;
    }
}
