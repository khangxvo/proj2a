package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {
    private final NGramMap nGramMap;

    public HistoryHandler(NGramMap map) {
        this.nGramMap = map;
    }

    public String handle(NgordnetQuery query) {
        if (query.words() == null || query.words().isEmpty() || query.startYear() < TimeSeries.MIN_YEAR
            || query.endYear() > TimeSeries.MAX_YEAR) {
            return "Invalid query parameters";
        }

        //        TimeSeries parabola = new TimeSeries();
        //        for (int i = 0; i < 100; i++) {
        //            parabola.put(i, (i - 50.0) * (i - 50.0) + 3);
        //        }
        //
        //        TimeSeries sinWave = new TimeSeries();
        //        for (int i = 0; i < 100; i++) {
        //            sinWave.put(i, 1000 + 500 * Math.sin(i / 100.0 * 2 * Math.PI));
        //        }
        //
        //        ArrayList<TimeSeries> timeSeriesList = new ArrayList<>();
        //        ArrayList<String> labels = new ArrayList<>();
        //
        //        labels.add("parabola");
        //        labels.add("sine wave");
        //
        //        timeSeriesList.add(parabola);
        //        timeSeriesList.add(sinWave);
        //
        //        XYChart chart = Plotter.generateTimeSeriesChart(labels, timeSeriesList);
        //        String encodedImage = Plotter.encodeChartAsString(chart);
        //
        //        return encodedImage;

        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (String word : query.words()) {
            labels.add(word);
            lts.add(nGramMap.weightHistory(word, query.startYear(), query.endYear()));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;

    }
}
