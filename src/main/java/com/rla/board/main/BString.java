package com.rla.board.main;

/**
 * @author Raphaël Laporte <br/>
 *         raphael.laporte@skynet.be <br/>
 *         4 jan. 2012 1st version <br/>
 *         3 dec. 2014 2nd version
 */

public class BString {

    private static final char END_LINE = '\n';

    private static final char BLANK = ' ';

    private static final char DASH = '-';

    private static final char DDASH = '=';

    private static final char PIPE = '|';

    private static String DEFINE = ": ";

    private static final char PLUS = '+';

    private static final char UP = '/';

    private static final char DOWN = '\\';

    private static final char CORRUPT_VALUE = '?';

    private int output_Width;

    private int label_Width;

    private int value_Width;

    private String trueToString = "true";

    private String falseToString = "false";

    private String nullToString = "null";

    private int borderWidth = 0;

    private StringBuilder board = new StringBuilder();

    private static final BString instance = new BString();

    private static BString i() {
        return instance;
    }

    private BString() {
    }

    /**
     * starts a boardString
     */
    private static void bStart() {
        i().output_Width = i().label_Width + i().value_Width + 3;// 2PIPES+DEFINE
        append(repeat(DASH, i().output_Width) + END_LINE);

    }

    /**
     * ends a boardString
     */
    private static void bEnd() {
        append(repeat(DASH, i().output_Width) + END_LINE);
    }

    /**
     * prints a simple separation
     */
    public static void bSeparation() {
        append(PLUS + repeat(DASH, i().output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a double separation
     */
    public static void bSeparationDouble() {
        append(PLUS + repeat(DDASH, i().output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a new line into boardString
     */
    public static void bNewLine() {
        append(PIPE + repeat(BLANK, i().output_Width - 2) + PIPE + END_LINE);
    }

    /**
     * prints alternatively up and down chars begins with up char see
     * bDownUp
     */
    public static void bUpDown() {
        for (int i = 0; i < i().output_Width; i++)
            append((i % 2 == 0) ? UP : DOWN);
        append(END_LINE);
    }

    /**
     * prints alternatively down and up chars begins with down char
     * see bUpDown
     */
    public static void bDownUp() {
        for (int i = 0; i < i().output_Width; i++)
            append((i % 2 == 0) ? DOWN : UP);
        append(END_LINE);
    }

    /**
     * prints a string on the left of the board
     * 
     * @param toLeft
     *            : the string
     */
    public static void bLeft(final String toLeft) {
        String toLeftTmp = getBorder() + toLeft + getBorder();
        int len = toLeftTmp.length();
        if (len == i().output_Width - 2)
            append(PIPE + toLeftTmp + PIPE + END_LINE);
        else if (len >= i().output_Width - 2)
            append(PIPE + toLeftTmp.substring(0, i().output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = i().output_Width - 2 - len;
            append(PIPE + toLeftTmp + repeat(BLANK, all_blanks_count) + PIPE + END_LINE);
        }
    }

    /**
     * prints a string on the right of the board
     * 
     * @param toRight
     *            : the string
     */
    public static void bRight(final String toRight) {
        String toRightTmp = getBorder() + toRight + getBorder();
        int len = toRightTmp.length();
        if (len == i().output_Width - 2)
            append(PIPE + toRightTmp + PIPE + END_LINE);
        else if (len > i().output_Width - 2)
            append(PIPE + toRightTmp.substring(0, i().output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = i().output_Width - 2 - len;
            append(PIPE + repeat(BLANK, all_blanks_count) + toRightTmp + PIPE + END_LINE);
        }
    }

    /**
     * prints a string on the center of the board
     * 
     * @param toCenter
     *            : the string
     */
    public static void bCenter(final String toCenter) {
        String toCenterTmp = getBorder() + toCenter + getBorder();
        int len = toCenterTmp.length();
        if (len == i().output_Width - 2)
            append(PIPE + toCenterTmp + PIPE + END_LINE);
        else if (len >= i().output_Width - 2) {
            append(PIPE + toCenterTmp.substring(0, i().output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        } else {
            int all_blanks_count = i().output_Width - 2 - len;
            int left_blank = all_blanks_count / 2;
            append(PIPE + repeat(BLANK, left_blank) + toCenterTmp + repeat(BLANK, all_blanks_count - left_blank) + PIPE + END_LINE);
        }
    }

    /**
     * prints the string label on the left of label's space and the
     * value on the right of value's space
     * 
     * @param label
     *            : the string label
     * @param value
     *            : the value to print
     */
    public static void bPrintLR(final String label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + label, i().label_Width);
        String valueStr = getConformedStringRight(eval(value) + getBorder(), i().value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * prints the string label on the left of label's space and the
     * value on the left of value's space
     * 
     * @param label
     *            : the string label
     * @param value
     *            : the value to print
     */
    public static void bPrintLL(final String label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + label, i().label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), i().value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * prints the string label on the right of label's space and the
     * value on the left of value's space
     * 
     * @param label
     *            : the string label
     * @param value
     *            : the value to print
     */
    public void bPrintRL(final String label, final Object value) {
        String labelStr = getConformedStringRight(getBorder() + label, i().label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), i().value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * prints the string label on the right of label's space and the
     * value on the right of value's space
     * 
     * @param label
     *            : the string label
     * @param value
     *            : the value to print
     */
    public void bPrintRR(final String label, final Object value) {
        String labelStr = getConformedStringRight(getBorder() + label, label_Width);
        String valueStr = getConformedStringRight(eval(value) + getBorder(), value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * prints the string label on the center of label's space and the
     * value on the center of value's space
     * 
     * @param label
     *            : the string label
     * @param value
     *            : the value to print
     */
    public static void bPrintCC(final String label, final Object value) {
        String labelStr = getConformedStringCentered(getBorder() + label, i().label_Width);
        String valueStr = getConformedStringCentered(eval(value) + getBorder(), i().value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * @return the board as a String
     */
    public static String t2String() {
        bEnd();
        final String ret = i().board.toString();
        i().board = new StringBuilder();
        bStart();
        return ret;
    }

    /**
     * @return the board as a String for html placement
     */
    public String getWebBoardString() {
        return toString().replace(" ", "&nbsp;");
    }

    private static String repeat(final char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (; count-- > 0;)
            sb.append(c);
        return sb.toString();
    }

    private static String getConformedStringLeft(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1) + CORRUPT_VALUE;
        else
            return input + repeat(BLANK, width - input.length());
    }

    private static String getConformedStringRight(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1) + CORRUPT_VALUE;
        else
            return repeat(BLANK, width - input.length()) + input;
    }

    private static String getConformedStringCentered(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1) + CORRUPT_VALUE;
        else {
            int total_blank = width - input.length();
            int total_left = total_blank / 2;
            int total_right = total_blank - total_left;
            return repeat(BLANK, total_left) + input + repeat(BLANK, total_right);
        }
    }

    private static String eval(final Object o) {
        if (o instanceof Boolean) {
            Boolean bool = (Boolean) o;
            if (bool)
                return i().trueToString;
            else
                return i().falseToString;
        }
        return (o != null) ? o.toString() : i().nullToString;
    }

    /**
     * set the string to print the true value
     * 
     * @param trueToString
     */
    public static void setTrueToString(final String trueToString) {
        i().trueToString = trueToString;
    }

    /**
     * set the string to print the false value
     * 
     * @param falseToString
     */
    public static void setFalseToString(final String falseToString) {
        i().falseToString = falseToString;
    }

    /**
     * set the string to print the null value
     * 
     * @param nullToString
     * @return
     */
    public static String setNullToString(final String nullToString) {
        return i().nullToString = nullToString;
    }

    private static String getBorder() {
        return repeat(BLANK, i().borderWidth);
    }

    /**
     * set the string for the define char between label and value
     * 
     * @param newDefineString
     */
    public static void setDefineString(final String newDefineString) {
        DEFINE = newDefineString;
    }

    private static void append(final String toAppend) {
        i().board.append(toAppend);
    }

    private static void append(final char toAppend) {
        i().board.append(toAppend);
    }

    public int getLabel_Width() {
        return i().label_Width;
    }

    /**
     * set the width between the border of the board and the labels or
     * values
     * 
     * @param width
     */
    public static void init(final int border_width, int label_Width, int value_Width) {
        i().borderWidth = border_width;
        i().label_Width = label_Width;
        i().value_Width = value_Width;
        i().output_Width = i().label_Width + i().value_Width + 3;// 2PIPES+DEFINE
        bStart();

    }

    public int getValue_Width() {
        return value_Width;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        BString.init(10, 50, 50);
        BString.bCenter("This is a demo with BoardString");
        BString.bSeparationDouble();
        BString.bNewLine();
        BString.bLeft("this appears on left");
        BString.bNewLine();
        BString.bRight("this on right");
        BString.bNewLine();
        BString.bSeparation();
        BString.bCenter("label value prints :");
        BString.bSeparation();
        BString.bNewLine();
        BString.bPrintLL("my Label", Boolean.TRUE);
        BString.bNewLine();
        BString.setDefineString(">>>>>> ");
        BString.bPrintCC("my Label centered", 12.34);
        BString.bNewLine();
        BString.bSeparation();
        BString.setDefineString("");
        BString.bPrintLR("4 jan. 2012", "written by rla");
        BString.setDefineString(": ");
        System.out.println();
        System.out.println(BString.t2String());
        BString.bUpDown();
        BString.bCenter("This was a demo with BoardString");
        BString.bUpDown();
        System.out.println(BString.t2String());
        System.out.println(System.nanoTime() - start);
    }
}
