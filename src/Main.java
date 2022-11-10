import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main{

    public static void main(String[] args) throws IOException {
        String tmp = Files.readString(Paths.get("resources/input.ts"));
        Lexer lexer = new Lexer(tmp);
        ArrayList<Token> res = lexer.Tokenize();
        for(Token t : res) {
            t.Print();
        }
    }

}