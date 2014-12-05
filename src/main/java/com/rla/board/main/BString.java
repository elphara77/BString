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

    private Integer output_Width;

    private String trueToString = "true";

    private String falseToString = "false";

    private String nullToString = "null";

    private Integer borderWidth = 0;
    private static Integer borderWidth_Setting;

    private Integer label_Width;
    private static Integer label_Width_Setting;

    private Integer value_Width;
    private static Integer value_Width_Setting;

    private StringBuilder board = new StringBuilder();

    public BString() {
        if (borderWidth_Setting != null) {
            this.borderWidth = borderWidth_Setting;
            this.label_Width = label_Width_Setting;
            this.value_Width = value_Width_Setting;
        } else {
            this.borderWidth = 1;
            this.label_Width = 20;
            this.value_Width = 20;
        }
        this.output_Width = this.label_Width + this.value_Width + 3;// 2PIPES+DEFINE
        
        bStart();
    }

    public BString(final int borderWidth, final int label_Width, final int value_Width, boolean keepSettings) {
        this.borderWidth = borderWidth;
        this.label_Width = label_Width;
        this.value_Width = value_Width;

        this.output_Width = this.label_Width + this.value_Width + 3;// 2PIPES+DEFINE

        if (keepSettings) {
            borderWidth_Setting = borderWidth;
            label_Width_Setting = label_Width;
            value_Width_Setting = value_Width;
        }

        bStart();
    }
    
    public static void resetSettings() {
        borderWidth_Setting = null;
        label_Width_Setting = null;
        value_Width_Setting = null;
    }

    /**
     * starts a boardString
     */
    private void bStart() {
        this.output_Width = this.label_Width + this.value_Width + 3;// 2PIPES+DEFINE
        append(repeat(DASH, this.output_Width) + END_LINE);
    }

    /**
     * ends a boardString
     */
    private void bEnd() {
        append(repeat(DASH, this.output_Width) + END_LINE);
    }

    /**
     * prints a simple separation
     */
    public void bSeparation() {
        append(PLUS + repeat(DASH, this.output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a double separation
     */
    public void bSeparationDouble() {
        append(PLUS + repeat(DDASH, this.output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a new line into boardString
     */
    public void bNewLine() {
        append(PIPE + repeat(BLANK, this.output_Width - 2) + PIPE + END_LINE);
    }

    /**
     * prints alternatively up and down chars begins with up char see
     * bDownUp
     */
    public void bUpDown() {
        for (int i = 0; i < this.output_Width; i++)
            append((i % 2 == 0) ? UP : DOWN);
        append(END_LINE);
    }

    /**
     * prints alternatively down and up chars begins with down char
     * see bUpDown
     */
    public void bDownUp() {
        for (int i = 0; i < this.output_Width; i++)
            append((i % 2 == 0) ? DOWN : UP);
        append(END_LINE);
    }

    /**
     * prints a string on the left of the board
     * 
     * @param toLeft
     *            : the string
     */
    public void bLeft(final String toLeft) {
        String toLeftTmp = getBorder() + toLeft + getBorder();
        int len = toLeftTmp.length();
        if (len == this.output_Width - 2)
            append(PIPE + toLeftTmp + PIPE + END_LINE);
        else if (len >= this.output_Width - 2)
            append(PIPE + toLeftTmp.substring(0, this.output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = this.output_Width - 2 - len;
            append(PIPE + toLeftTmp + repeat(BLANK, all_blanks_count) + PIPE + END_LINE);
        }
    }

    /**
     * prints a string on the right of the board
     * 
     * @param toRight
     *            : the string
     */
    public void bRight(final String toRight) {
        String toRightTmp = getBorder() + toRight + getBorder();
        int len = toRightTmp.length();
        if (len == this.output_Width - 2)
            append(PIPE + toRightTmp + PIPE + END_LINE);
        else if (len > this.output_Width - 2)
            append(PIPE + toRightTmp.substring(0, this.output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = this.output_Width - 2 - len;
            append(PIPE + repeat(BLANK, all_blanks_count) + toRightTmp + PIPE + END_LINE);
        }
    }

    /**
     * prints a string on the center of the board
     * 
     * @param toCenter
     *            : the string
     */
    public void bCenter(final String toCenter) {
        String toCenterTmp = getBorder() + toCenter + getBorder();
        int len = toCenterTmp.length();
        if (len == this.output_Width - 2)
            append(PIPE + toCenterTmp + PIPE + END_LINE);
        else if (len >= this.output_Width - 2) {
            append(PIPE + toCenterTmp.substring(0, this.output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        } else {
            int all_blanks_count = this.output_Width - 2 - len;
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
    public void bPrintLR(final String label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + label, this.label_Width);
        String valueStr = getConformedStringRight(eval(value) + getBorder(), this.value_Width - DEFINE.length() + 1);
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
    public void bPrintLL(final String label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + label, this.label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), this.value_Width - DEFINE.length() + 1);
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
        String labelStr = getConformedStringRight(getBorder() + label, this.label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), this.value_Width - DEFINE.length() + 1);
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
    public void bPrintCC(final String label, final Object value) {
        String labelStr = getConformedStringCentered(getBorder() + label, this.label_Width);
        String valueStr = getConformedStringCentered(eval(value) + getBorder(), this.value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * @return the board as a String
     */
    public String toString() {
        bEnd();
        final String ret = this.board.toString();
        this.board=new StringBuilder();
        bStart();
        return ret;
    }

    /**
     * @return the board as a String
     */
    public String toLogString() {
        this.board.insert(0, END_LINE);
        return toString();
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

    private String eval(final Object o) {
        if (o instanceof Boolean) {
            Boolean bool = (Boolean) o;
            if (bool)
                return this.trueToString;
            else
                return this.falseToString;
        }
        return (o != null) ? o.toString() : this.nullToString;
    }

    /**
     * set the string to print the true value
     * 
     * @param trueToString
     */
    public void setTrueToString(final String trueToString) {
        this.trueToString = trueToString;
    }

    /**
     * set the string to print the false value
     * 
     * @param falseToString
     */
    public void setFalseToString(final String falseToString) {
        this.falseToString = falseToString;
    }

    /**
     * set the string to print the null value
     * 
     * @param nullToString
     * @return
     */
    public String setNullToString(final String nullToString) {
        return this.nullToString = nullToString;
    }

    private String getBorder() {
        return repeat(BLANK, this.borderWidth);
    }

    /**
     * set the string for the define char between label and value
     * 
     * @param newDefineString
     */
    public void setDefineString(final String newDefineString) {
        DEFINE = newDefineString;
    }

    private void append(final String toAppend) {
        this.board.append(toAppend);
    }

    private void append(final char toAppend) {
        this.board.append(toAppend);
    }

    public int getLabel_Width() {
        return this.label_Width;
    }

    public int getValue_Width() {
        return value_Width;
    }

    public static void main(String[] args) {
        BString bs = new BString(10, 50, 50, false);
        bs.bCenter("This is a demo with BoardString");
        bs.bSeparationDouble();
        bs.bNewLine();
        bs.bLeft("this appears on left");
        bs.bNewLine();
        bs.bRight("this on right");
        bs.bNewLine();
        bs.bSeparation();
        bs.bCenter("label value prints :");
        bs.bSeparation();
        bs.bNewLine();
        bs.bPrintLL("my Label", Boolean.TRUE);
        bs.bNewLine();
        bs.setDefineString(">>>>>> ");
        bs.bPrintCC("my Label centered", 12.34);
        bs.bNewLine();
        bs.bSeparation();
        bs.setDefineString("");
        bs.bPrintLR("4 jan. 2012", "written by rla");
        bs.setDefineString(": ");
        System.out.println();
        System.out.println(bs.toString());
        bs.bUpDown();
        bs.bCenter("This was a demo with BoardString");
        bs.bUpDown();
        System.out.println(bs.toLogString());
    }
}
