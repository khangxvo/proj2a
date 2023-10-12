package main;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import browser.NgordnetQuery;
public class HistoryTextHandler extends NgordnetQueryHandler {

    private final NGramMap nGramMap;

    public HistoryTextHandler(NGramMap map) {
        this.nGramMap = map;
    }

    public String handle(NgordnetQuery query) {
        if (query.words() == null || query.words().isEmpty() || query.startYear() < 0 || query.endYear() < 0) {
            return "Invalid query parameters.";
        }
        /* Create scientific notation for relative popularity by year */
        //DecimalFormat scientificNotFor = new DecimalFormat("0.################E0");
        StringBuilder historyText = new StringBuilder();
        for (String word : query.words()) {
            TimeSeries wordHistory = nGramMap.countHistory(word, query.startYear(), query.endYear());
            TimeSeries totalHistory = nGramMap.totalCountHistory();
            historyText.append(word).append(": {");
            for (int year = query.startYear(); year <= query.endYear(); year++) {
                Double wordCount = wordHistory.get(year);
                Double totalCount = totalHistory.get(year);
                if (wordCount != null && totalCount != null && totalCount != 0) {
                    double relativeValue = wordCount / totalCount;
                    //historyText.append(year).append("=").append(scientificNotFor.format(relativeValue)).append(", ");
                    historyText.append(year).append("=").append(relativeValue).append(", ");
                }
            }
            if (historyText.toString().endsWith(", ")) {
                historyText.setLength(historyText.length() - 2);
            }
            historyText.append("}\n");
        }
        return historyText.toString();
    }
}
