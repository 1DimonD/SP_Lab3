import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class DSA {

    private class State {
        public int num;
        public TokenType tokenType;
        public Function<Character, Integer> transition;

        public State(int num_, TokenType type, Function<Character, Integer> func) {
            num = num_;
            tokenType = type;
            transition = func;
        }
    }

    private ArrayList<State> _states;
    private int _errorState;
    private int _currState;

    public DSA() {
        _states = new ArrayList<>(){{
            // start state (word is empty)
            add(new State(0, TokenType.ERROR, (Character c) -> {
                if(c == '0') {
                    return 1;
                } else if(Character.isDigit(c)) {
                    return 3;
                } else if('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z') {
                    return 7;
                } else if(c == '"' || c == '\'') {
                    return 11;
                } else if(c == '.') {
                    return 10;
                } else {
                    return _errorState;
                }
            }));

            // 1-2 for handling 16-bit
            add(new State(1, TokenType.NUMBER, (Character c) -> {
                if(c == 'x') {
                    return 2;
                } else if(Character.isDigit(c)) {
                    return 3;
                } else if(c == '.') {
                    return 4;
                } else if(c == 'e') {
                    return 5;
                } else {
                    return _errorState;
                }
            }));
            add(new State(2, TokenType.NUMBER, (Character c) -> {
                if(Character.isDigit(c) || ('A' <= c && c <= 'F')) {
                    return 2;
                } else {
                    return _errorState;
                }
            }));

            // 3-6 for different num writings
            add(new State(3, TokenType.NUMBER, (Character c) -> {
                if(Character.isDigit(c)) {
                    return 3;
                } else if(c == '.') {
                    return 4;
                } else if(c == 'e') {
                    return 5;
                } else {
                    return _errorState;
                }
            }));

            add(new State(4, TokenType.NUMBER, (Character c) -> {
                if(Character.isDigit(c)) {
                    return 4;
                } else {
                    return _errorState;
                }
            }));

            add(new State(5, TokenType.ERROR, (Character c) -> {
                if(Character.isDigit(c) || c == '-') {
                    return 6;
                } else {
                    return _errorState;
                }
            }));

            add(new State(6, TokenType.NUMBER, (Character c) -> {
                if(Character.isDigit(c)) {
                    return 6;
                } else {
                    return _errorState;
                }
            }));

            // 7 for words
            add(new State(7, TokenType.WORD, (Character c) -> {
                if(c == '.') {
                    return 10;
                } else {
                    return 7;
                }
            }));

            //8-10 for simple classes
            add(new State(8, TokenType.WHITESPACE, (Character c) -> {
                return _errorState;
            }));
            add(new State(9, TokenType.PUNCTUATION, (Character c) -> {
                return _errorState;
            }));
            add(new State(10, TokenType.OPERATOR, (Character c) -> {
                return _errorState;
            }));

            //11-12 for strings
            add(new State(11, TokenType.ERROR, (Character c) -> {
                if(c == '"' || c == '\'') {
                    return 12;
                } else {
                    return 11;
                }
            }));
            add(new State(12, TokenType.STRING, (Character c) -> {
                return _errorState;
            }));

            //13 for comments
            add(new State(13, TokenType.SLASH, (Character c) -> {
                return _errorState;
            }));

            //14 for errors
            add(new State(14, TokenType.ERROR, (Character c) -> {
                return _errorState;
            }));
        }};

        _currState = 0;
        _errorState = _states.size()-1;
    }

    private boolean IsPunctuation(char c) {
        ArrayList<Character> punct = new ArrayList<>(Arrays.asList(',',';','{','}'));
        return punct.contains(c);
    }

    private boolean IsOperator(char c) {
        ArrayList<Character> operators = new ArrayList<>(Arrays.asList(':','?','(',')','[',']',
                '+', '-', '*', '%', '=', '!', '|', '&', '~', '^', '<', '>'));
        return operators.contains(c);
    }

    public void Evaluate(char letter) {
        if(letter == '/') {
            _currState = 13;
        } else if(_currState == 11) {
            _currState = _states.get(_currState).transition.apply(letter);
        } else if(Character.isWhitespace(letter)) {
            _currState = 8;
        } else if(IsPunctuation(letter)) {
            _currState = 9;
        } else if(IsOperator(letter)) {
            _currState = 10;
        } else {
            _currState = _states.get(_currState).transition.apply(letter);
        }
    }

    public TokenType GetTokenType() {
        return _states.get(_currState).tokenType;
    }

    public void ResetDSA() {
        _currState = 0;
    }
}
