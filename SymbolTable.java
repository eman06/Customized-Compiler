package scanner;

import java.util.*;

public class SymbolTable {

    static class Entry {
        String name;
        String type;
        int firstLine;
        int frequency;

        Entry(String name, String type, int line) {
            this.name = name;
            this.type = type;
            this.firstLine = line;
            this.frequency = 1;
        }
    }

    private Map<String, Entry> table = new LinkedHashMap<>();

    public void add(String name, String type, int line) {
        if (table.containsKey(name)) {
            table.get(name).frequency++;
        } else {
            table.put(name, new Entry(name, type, line));
        }
    }

    public void print() {
        System.out.println("\n===== SYMBOL TABLE =====");
        for (Entry e : table.values()) {
            System.out.printf("%-15s Type:%-12s FirstLine:%-5d Freq:%d\n",
                    e.name, e.type, e.firstLine, e.frequency);
        }
    }
}
