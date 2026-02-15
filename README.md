# README.md - MyScript Compiler (Lexical Analyzer)

## 1. Project Information
* **Language Name:** MyScript
* **File Extension:** .lang
* **Team Members:**
    * Member 1 (Roll No: 23i-0530)
    * Member 2 (Roll No: 23i-0790)
* **Project Phase:** Phase 1 - Lexical Analysis

---

## 2. Keyword List & Meanings
| Keyword | Meaning |
| :--- | :--- |
| **start** | The entry point of the program execution block. |
| **finish** | The termination point of the program execution block. |
| **declare** | Used to initialize and define variables in the symbol table. |
| **condition** | Used for logical branching (Equivalent to an 'if' statement). |
| **else** | Optional branch used when the condition evaluates to false. |
| **loop** | Initiates an iterative control structure (Equivalent to 'while'). |
| **output** | Built-in command to display values to the standard output. |
| **input** | Command to read user input from the standard input. |
| **function** | Defines a reusable block of code or sub-routine. |
| **return** | Specifies the value to be returned from a function. |
| **true** | Boolean literal representing the logical "truth" state. |
| **false** | Boolean literal representing the logical "false" state. |

---

## 3. Identifier Rules
* **Case Sensitivity:** Identifiers are case-sensitive.
* **Starting Character:** Must start with an Uppercase letter [A-Z].
* **Follow-up Characters:** Can contain letters, digits, or underscores (_).
* **Maximum Length:** 31 characters.
* **Examples:** Counter, Variable_1, Total_Sum.
* **Invalid:** myVar (lowercase), 123Var (digit start).

---

## 4. Literal Formats
* **Integer:** Whole numbers (e.g., 42, -100).
* **Float:** Decimals and scientific notation (e.g., 3.14, -5.5e10).
* **String:** Enclosed in double quotes. Supports escapes (e.g., "Hello \"World\"").
* **Char:** Single character in single quotes (e.g., 'A', '\n').

---

## 5. Operator List & Precedence (High to Low)
1. Grouping: ( ), [ ], { }
2. Exponentiation: **
3. Multiplicative: *, /, %
4. Additive: +, -
5. Relational: <, >, <=, >=
6. Equality: ==, !=
7. Logical: &&, ||
8. Assignment: =, +=, -=, *=, /=
9. Unary: ++, --

---

## 6. Comment Syntax
* **Single-line:** Starts with ##.
* **Multi-line:** Starts with #* and ends with *#.

---

## 7. Sample Programs

### Program 1: Arithmetic
start
declare Price = 150;
declare Tax = 0.08;
declare Total = Price + (Price * Tax);
output Total;
finish
### Program 2: Loop
start
declare Index = 5;
loop (Index > 0) {
output Index;
Index -= 1;
}
finish
---

## 8. Compilation and Execution
1. Generate Scanner: `java -jar lib/jflex-full-1.9.1.jar cc/Scanner.flex`
2. Compile: `javac cc/*.java`
3. Run: `java cc.ComparisonTest`
