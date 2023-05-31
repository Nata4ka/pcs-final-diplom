import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> wordsMap = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] files = pdfsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                var doc = new PdfDocument(new PdfReader(file));
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                    var words = text.split("\\P{IsAlphabetic}+");
                    Map<String, Integer> freqs = new HashMap<>();
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    for (var word : freqs.keySet()) {
                        int page = i;
                        List<PageEntry> list = new ArrayList<>();
                        if (!wordsMap.containsKey(word)) {
                            list.add(new PageEntry(file.getName(), page, freqs.get(word)));
                            list.sort(PageEntry::compareTo);
                            wordsMap.put(word, list);
                        } else {
                            List<PageEntry> entry = wordsMap.get(word);
                            entry.add(new PageEntry(file.getName(), page, freqs.get(word)));
                            entry.sort(PageEntry::compareTo);
                            wordsMap.put(word, entry);
                        }
                    }
                }
            }
        }
    }


    @Override
    public List<PageEntry> search(String word) {
        String wordReg = word.toLowerCase();
        List<PageEntry> pageEntryList = wordsMap.getOrDefault(wordReg, Collections.emptyList());

        return pageEntryList;
    }
}
