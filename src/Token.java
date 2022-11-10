public class Token {
    public TokenType type;
    public String value;

    public Token(TokenType type_, String value_) {
        type = type_;
        value = value_;
    }

    public void Print() {
        System.out.println("Token {\nvalue: " + value + "\ntype: " + type + "\n}\n");
    }
}