package test;

import br.util.Tiracentos;
import org.junit.Test;
import static org.junit.Assert.*;

public class TiracentosTest {

    private static final String ASCII_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS_ASCII_KEYBOARD = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    private static final String CHARS_ACCENTED_ORIGINAL = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ";
    private static final String CHARS_ACCENTED_MODIFIED = "AAAAAAECEEEEIIIIDNOOOOOOUUUUYPBaaaaaaeceeeeiiiidnoooooouuuuypy";

    private static final String SYMBOLS_EXTENDED_ORIGINAL = " ¡¢£€¥Š§š©ª«¬­®¯°±²³Žµ¶·ž¹º»ŒœŸ¿×÷";
    private static final String SYMBOLS_EXTENDED_MODIFIED = " !cLoY|S:Ca<--R-o~23\'uP.,1o>123?x/";

    @Test
    public void testToAscii() {
        String alphabet = Tiracentos.toAscii(ASCII_ALPHABET);
        String numeros  = Tiracentos.toAscii(NUMBERS);
        String symbols  = Tiracentos.toAscii(SYMBOLS_ASCII_KEYBOARD);
        assertEquals(alphabet, ASCII_ALPHABET);
        assertEquals(numeros, NUMBERS);
        assertEquals(symbols, SYMBOLS_ASCII_KEYBOARD);

        String accented = Tiracentos.toAscii(CHARS_ACCENTED_ORIGINAL);
        assertEquals(CHARS_ACCENTED_MODIFIED, accented);

        String modified = Tiracentos.toAscii(SYMBOLS_EXTENDED_ORIGINAL);
        assertEquals(SYMBOLS_EXTENDED_MODIFIED, modified);
    }

}
