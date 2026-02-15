package scanner;

import java.util.*;

public class ManualScanner {

    private String input;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    private List<Token> tokens = new ArrayList<>();
    private Map<String,Integer> stats = new HashMap<>();
    private int commentsRemoved = 0;

    private SymbolTable symbolTable = new SymbolTable();

    public ManualScanner(String text) {
        this.input = text;
    }

    private char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    private char advance() {
        char c = peek();
        pos++;
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    private void addToken(String type, String lexeme, int startCol) {
        tokens.add(new Token(type, lexeme, line, startCol));
        stats.put(type, stats.getOrDefault(type,0)+1);
    }

    public void scan() {
        while (pos < input.length()) {

            char c = peek();

            /* ---------- WHITESPACE ---------- */
            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            int startCol = col;

            /* ---------- COMMENTS ## ---------- */
            if (c == '#' && pos+1 < input.length() && input.charAt(pos+1) == '#') {
                while (peek() != '\n' && peek() != '\0')
                    advance();
                commentsRemoved++;
                continue;
            }

            /* ---------- STRING LITERAL ---------- */
            if (c == '"') {
                StringBuilder sb = new StringBuilder();
                sb.append(advance());

                while (peek() != '"' && peek() != '\0') {
                    if (peek() == '\\') {
                        sb.append(advance());
                        sb.append(advance());
                    } else {
                        sb.append(advance());
                    }
                }

                if (peek() == '"')
                    sb.append(advance());

                addToken("STRING", sb.toString(), startCol);
                continue;
            }

            /* ---------- IDENTIFIER ---------- */
            if (Character.isUpperCase(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(advance());

                while (Character.isLowerCase(peek())
                        || Character.isDigit(peek())
                        || peek() == '_') {

                    if (sb.length() >= 31)
                        break;

                    sb.append(advance());
                }

                String lex = sb.toString();

                // strict validation
                if (lex.matches("[A-Z][a-z0-9_]{0,30}")) {
                    addToken("IDENTIFIER", lex, startCol);
                    symbolTable.add(lex, "identifier", line);
                } else {
                    addToken("INVALID_IDENTIFIER", lex, startCol);
                }

                continue;
            }


            /* ---------- NUMBER ---------- */
            if (c=='+' || c=='-' || Character.isDigit(c)) {

                int tempPos = pos;
                StringBuilder sb = new StringBuilder();

                if (c=='+'||c=='-')
                    sb.append(advance());

                boolean hasDot = false;
                boolean hasExp = false;

                while (true) {
                    char p = peek();

                    if (Character.isDigit(p)) {
                        sb.append(advance());
                    }
                    else if (p=='.' && !hasDot && !hasExp) {
                        hasDot = true;
                        sb.append(advance());
                    }
                    else if ((p=='e'||p=='E') && !hasExp) {
                        hasExp = true;
                        sb.append(advance());
                        if (peek()=='+'||peek()=='-')
                            sb.append(advance());
                    }
                    else break;
                }

                String lex = sb.toString();

                if (lex.matches("[+-]?[0-9]+\\.[0-9]{1,6}([eE][+-]?[0-9]+)?"))
                    addToken("FLOAT", lex, startCol);
                else if (lex.matches("[+-]?[0-9]+"))
                    addToken("INTEGER", lex, startCol);
                else {
                    pos = tempPos;
                    col = startCol;
                }
                continue;
            }

            /* ---------- BOOLEAN ---------- */
            if (input.startsWith("true", pos) || input.startsWith("false", pos)) {
                String val = input.startsWith("true", pos) ? "true" : "false";
                for(int i=0;i<val.length();i++) advance();
                addToken("BOOLEAN", val, startCol);
                continue;
            }

            /* ---------- OPERATORS ---------- */

            // longest match first
            if (input.startsWith("**",pos)) {
                advance(); advance();
                addToken("OPERATOR","**",startCol);
                continue;
            }
            if (input.startsWith("&&",pos)) {
                advance(); advance();
                addToken("OPERATOR","&&",startCol);
                continue;
            }
            if (input.startsWith("||",pos)) {
                advance(); advance();
                addToken("OPERATOR","||",startCol);
                continue;
            }

            if ("+-*/%!".indexOf(c)>=0) {
                addToken("OPERATOR", String.valueOf(advance()), startCol);
                continue;
            }

            /* ---------- UNKNOWN ---------- */
            addToken("UNKNOWN", String.valueOf(advance()), startCol);
        }
    }

    public void printTokens() {
        System.out.println("\n===== TOKENS =====");
        for (Token t : tokens)
            System.out.println(t);
    }

    public void printStats() {
        System.out.println("\n===== STATISTICS =====");
        int total = tokens.size();
        System.out.println("Total Tokens: " + total);

        for (String k : stats.keySet())
            System.out.println(k + ": " + stats.get(k));

        System.out.println("Lines processed: " + line);
        System.out.println("Comments removed: " + commentsRemoved);
    }

    public void printSymbolTable() {
        symbolTable.print();
    }

    /* ---------- MAIN DRIVER ---------- */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Provide input file.");
            return;
        }

        String text = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(args[0])));

        ManualScanner scanner = new ManualScanner(text);
        scanner.scan();
        scanner.printTokens();
        scanner.printStats();
        scanner.printSymbolTable();
    }
}
