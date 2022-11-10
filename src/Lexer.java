import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class Lexer {
    private String _input;
    private int _currPos;
    private DSA _automata;
    private ArrayList<Token> _tokens;

    private ArrayList<String> _reservedWords;
    private ArrayList<String> _complexOperators;

    public Lexer() {
        _automata = new DSA();
        _reservedWords = new ArrayList<>(Arrays.asList("abstract", "arguments",	"await", "boolean",
                "break", "byte", "case", "catch", "char", "class", "const",	"continue", "debugger",	"default",
                "delete", "do", "double", "else", "enum", "eval", "export",	"extends", "false", "final",
                "finally", "float",	"for", "function", "goto", "if", "implements", "import", "in", "instanceof",
                "int", "interface", "let", "long", "native", "new", "null",	"package", "private", "protected",
                "public", "return", "short", "static", "super",	"switch", "synchronized", "this", "throw",
                "throws", "transient", "true", "try", "typeof",	"var", "void", "volatile", "while",	"with",	"yield"));
        _complexOperators = new ArrayList<>(Arrays.asList("+=", "-=", "*=", "/=", "%=", "**", "++", "--", "===", "==",
                "!=", "!==", ">=", "<=", "&&", "||", "<<", ">>", ">>>"));
    }
    public Lexer(String input) { this(); SetInput(input); }

    public void SetInput(String input) { _input = input; }

    private void AddToken(String word, TokenType type) {
        if(type != null && word != "") {
            if(type == TokenType.WORD) {
                if(_reservedWords.contains(word)) {
                    type = TokenType.KEYWORD;
                } else {
                    type = TokenType.IDENTIFIER;
                }
            }
            _tokens.add(new Token(type, word));
        }
    }

    public ArrayList<Token> Tokenize() {
        _tokens = new ArrayList<>();
        _currPos = 0;

        char c;
        String word = "";
        TokenType type = null, tmp_type;
        while(_currPos < _input.length()) {
            c = _input.charAt(_currPos);
            _automata.Evaluate(c);

            tmp_type = _automata.GetTokenType();

            if(tmp_type == TokenType.SLASH) {
                AddToken(word, type);
                word = "" + c;
                if (_complexOperators.contains(word + _input.charAt(_currPos + 1))) {
                    _currPos += 1;
                    word += _input.charAt(_currPos);
                    AddToken(word, tmp_type);
                    word = "";
                } else if(_input.charAt(_currPos+1) == '/') {
                    while (_input.charAt(_currPos) != '\n') {
                        _currPos += 1;
                    }
                } else if(_input.charAt(_currPos+1) == '*') {
                    while (!_input.substring(_currPos, _currPos+2).contains("*/")) {
                        _currPos += 1;
                    }
                }
                word = "";
                _automata.ResetDSA();
            } else if(tmp_type == TokenType.OPERATOR) {
                AddToken(word, type);
                word = "" + c;
                if (_complexOperators.contains(word + _input.charAt(_currPos + 1))) {
                    _currPos += 1;
                    word += _input.charAt(_currPos);
                    if (_complexOperators.contains(word + _input.charAt(_currPos + 1))) {
                        _currPos += 1;
                        word += _input.charAt(_currPos);
                    }
                }
                AddToken(word, tmp_type);
                word = "";
                _automata.ResetDSA();
            } else if(tmp_type == TokenType.PUNCTUATION || tmp_type == TokenType.WHITESPACE) {
                AddToken(word, type);
                word = "";
                if(tmp_type != TokenType.WHITESPACE) {
                    _tokens.add(new Token(tmp_type, String.valueOf(c)));
                }
                _automata.ResetDSA();
            } else {
                type = tmp_type;
                word += c;
            }

            _currPos += 1;
        }
        AddToken(word, type);

        return _tokens;
    }
}
