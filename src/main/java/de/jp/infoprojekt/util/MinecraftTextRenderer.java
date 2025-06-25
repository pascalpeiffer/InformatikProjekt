package de.jp.infoprojekt.util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MinecraftTextRenderer {

    // Mapping Minecraft color codes to actual colors
    private static final Map<Character, Color> colorMap = new HashMap<>();
    static {
        colorMap.put('0', new Color(0x000000));
        colorMap.put('1', new Color(0x0000AA));
        colorMap.put('2', new Color(0x00AA00));
        colorMap.put('3', new Color(0x00AAAA));
        colorMap.put('4', new Color(0xAA0000));
        colorMap.put('5', new Color(0xAA00AA));
        colorMap.put('6', new Color(0xFFAA00));
        colorMap.put('7', new Color(0xAAAAAA));
        colorMap.put('8', new Color(0x555555));
        colorMap.put('9', new Color(0x5555FF));
        colorMap.put('a', new Color(0x55FF55));
        colorMap.put('b', new Color(0x55FFFF));
        colorMap.put('c', new Color(0xFF5555));
        colorMap.put('d', new Color(0xFF55FF));
        colorMap.put('e', new Color(0xFFFF55));
        colorMap.put('f', new Color(0xFFFFFF));
    }

    public static void drawFormattedString(Graphics g, String text, int x, int y, int maxWidth, int maxHeight, Font mainFont, int renderIndex) {
        int cursorX = x;
        int cursorY = y;

        Font baseFont = mainFont;
        Font currentFont = baseFont;
        FontMetrics fm = g.getFontMetrics(currentFont);

        Color currentColor = colorMap.getOrDefault('f', Color.WHITE);
        boolean bold = false, italic = false, underline = false, strikethrough = false;

        int visibleCharCount = 0;

        int i = 0;
        while (i < text.length()) {

            // Check for formatting
            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                i += 2;

                if (colorMap.containsKey(code)) {
                    currentColor = colorMap.get(code);
                    bold = italic = underline = strikethrough = false;
                } else {
                    switch (code) {
                        case 'l': bold = true; break;
                        case 'o': italic = true; break;
                        case 'n': underline = true; break;
                        case 'm': strikethrough = true; break;
                        case 'r':
                            bold = italic = underline = strikethrough = false;
                            currentColor = colorMap.getOrDefault('f', Color.WHITE);
                            break;
                    }
                }

                int style = Font.PLAIN;
                if (bold) style |= Font.BOLD;
                if (italic) style |= Font.ITALIC;
                currentFont = baseFont.deriveFont(style);
                fm = g.getFontMetrics(currentFont);
                continue;
            }

            // Get word
            int wordStart = i;
            while (i < text.length() && text.charAt(i) != ' ' && text.charAt(i) != '§') {
                i++;
            }
            String word = text.substring(wordStart, i);
            int wordWidth = fm.stringWidth(word);

            // Check wrap
            if (cursorX + wordWidth > x + maxWidth) {
                cursorX = x;
                cursorY += fm.getHeight();
            }

            // Stop if exceeds maxHeight
            if (cursorY + fm.getHeight() > y + maxHeight) return;

            // Draw word char by char
            for (int j = 0; j < word.length(); j++) {
                if (visibleCharCount >= renderIndex) return;

                char c = word.charAt(j);
                int charWidth = fm.charWidth(c);

                g.setFont(currentFont);
                g.setColor(currentColor);
                g.drawString(String.valueOf(c), cursorX, cursorY);

                if (underline) {
                    g.drawLine(cursorX, cursorY + 1, cursorX + charWidth, cursorY + 1);
                } else if (strikethrough) {
                    g.drawLine(cursorX, cursorY - fm.getAscent() / 2, cursorX + charWidth, cursorY - fm.getAscent() / 2);
                }

                cursorX += charWidth;
                visibleCharCount++;
            }

            // Handle space
            if (i < text.length() && text.charAt(i) == ' ') {
                if (visibleCharCount >= renderIndex) return;

                int spaceWidth = fm.charWidth(' ');
                if (cursorX + spaceWidth > x + maxWidth) {
                    cursorX = x;
                    cursorY += fm.getHeight();
                } else {
                    cursorX += spaceWidth;
                }

                // Check height again
                if (cursorY + fm.getHeight() > y + maxHeight) return;

                i++;
                visibleCharCount++;
            }
        }
    }

}
