package com.rla.board.main;

/**
 * @author Raphaël Laporte
 */
// Raphael.Laporte@skynet.be
// 4 jan. 2012 1st version
// 3 dec. 2014 2nd version
// 17 jun. 2015 3nd version
// 26 aug. 2015 4nd version
// git test : a change
public class BString {

    private static final int DEFAULT_BORDER_WIDTH = 1;
    private static final int DEFAULT_LABEL_WIDTH = 30;
    private static final int DEFAULT_VALUE_WIDTH = 30;

    private static final String END_LINE = System.lineSeparator();

    private static final char BLANK = ' ';

    private static final char DASH = '-';

    private static final char DDASH = '=';

    private static final char PIPE = '|';

    private static String DEFINE = ": ";

    private static final char PLUS = '+';

    private static final char UP = '/';

    private static final char DOWN = '\\';

    private static final String CORRUPT_VALUE = "...";

    private Integer output_Width;

    private static String trueToString = "true";

    private static String falseToString = "false";

    private static String nullToString = "null";

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
            this.borderWidth = DEFAULT_BORDER_WIDTH;
            this.label_Width = DEFAULT_LABEL_WIDTH;
            this.value_Width = DEFAULT_VALUE_WIDTH;
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

    public static BString newInstance() {
        final String header = getHeader();
        BString bs = getMaxBString(getMaxLen(header));
        bs.bCenter(header);
        bs.bSeparationDouble();
        return bs;
    }

    public static BString newInstance(Object... objs) {
        String header = getHeader();

        int max = getMaxLen(objs);
        int maxHead = getMaxLen(header);
        if (maxHead > max) {
            max = maxHead;
        }

        BString bs = getMaxBString(max);
        bs.bCenter(header);
        bs.bSeparationDouble();

        for (Object obj : objs) {
            bs.bCenter(eval(obj));
        }

        return bs;
    }

    public void add(Object... objs) {
        for (Object obj : objs) {
            this.bCenter(eval(obj));
        }
    }

    private static String getHeader() {
        final StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        return caller.getClassName() + " --> " + caller.getMethodName();
    }

    private static BString getMaxBString(int max) {
        BString bs;
        if ((DEFAULT_LABEL_WIDTH + DEFAULT_VALUE_WIDTH) < max) {
            bs = new BString(1, max / 2, max - (max / 2) + 1, false);
        } else {
            bs = new BString();
        }

        return bs;
    }

    private static int getMaxLen(Object... objs) {
        int max = 0;
        for (Object obj : objs) {
            String str = eval(obj);
            if (str != null && str.length() > max) {
                max = str.length();
            }
        }
        return max;
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
     * @param obj
     *            : the string
     */
    public void bLeft(final Object obj) {
        String[] splits = eval(obj).split(System.lineSeparator());
        for (String str : splits) {
            String toLeftTmp = getBorder() + str + getBorder();
            int len = toLeftTmp.length();
            if (len == this.output_Width - 2)
                append(PIPE + toLeftTmp + PIPE + END_LINE);
            else if (len >= this.output_Width - 2)
                append(PIPE + toLeftTmp.substring(0, this.output_Width - 3 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " " + PIPE + END_LINE);
            else {
                int all_blanks_count = this.output_Width - 2 - len;
                append(PIPE + toLeftTmp + repeat(BLANK, all_blanks_count) + PIPE + END_LINE);
            }
        }
    }

    /**
     * prints a string on the right of the board
     * 
     * @param obj
     *            : the string
     */
    public void bRight(final Object obj) {
        String[] splits = eval(obj).split(System.lineSeparator());
        for (String str : splits) {
            String toRightTmp = getBorder() + str + getBorder();
            int len = toRightTmp.length();
            if (len == this.output_Width - 2)
                append(PIPE + toRightTmp + PIPE + END_LINE);
            else if (len > this.output_Width - 2)
                append(PIPE + toRightTmp.substring(0, this.output_Width - 3 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " " + PIPE + END_LINE);
            else {
                int all_blanks_count = this.output_Width - 2 - len;
                append(PIPE + repeat(BLANK, all_blanks_count) + toRightTmp + PIPE + END_LINE);
            }
        }
    }

    /**
     * prints a string on the center of the board
     * 
     * @param obj
     *            : the string
     */
    public void bCenter(final Object obj) {
        String[] splits = eval(obj).split(System.lineSeparator());
        for (String str : splits) {
            String toCenterTmp = getBorder() + str + getBorder();
            int len = toCenterTmp.length();
            if (len == this.output_Width - 2)
                append(PIPE + toCenterTmp + PIPE + END_LINE);
            else if (len >= this.output_Width - 2) {
                append(PIPE + toCenterTmp.substring(0, this.output_Width - 3 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " " + PIPE + END_LINE);
            } else {
                int all_blanks_count = this.output_Width - 2 - len;
                int left_blank = all_blanks_count / 2;
                append(PIPE + repeat(BLANK, left_blank) + toCenterTmp + repeat(BLANK, all_blanks_count - left_blank) + PIPE + END_LINE);
            }
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
    public void bPrintLR(final Object label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + eval(label), this.label_Width);
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
    public void bPrintLL(final Object label, final Object value) {
        String labelStr = getConformedStringLeft(getBorder() + eval(label), this.label_Width);
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
    public void bPrintRL(final Object label, final Object value) {
        String labelStr = getConformedStringRight(getBorder() + eval(label), this.label_Width);
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
    public void bPrintRR(final Object label, final Object value) {
        String labelStr = getConformedStringRight(getBorder() + eval(label), label_Width);
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
    public void bPrintCC(final Object label, final Object value) {
        String labelStr = getConformedStringCentered(getBorder() + eval(label), this.label_Width);
        String valueStr = getConformedStringCentered(eval(value) + getBorder(), this.value_Width - DEFINE.length() + 1);
        append(PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * @return the board as a String
     */
    public String toString() {
        this.board.insert(0, END_LINE);
        bEnd();
        final String ret = this.board.toString();
        this.board = new StringBuilder();
        bStart();
        return ret;
    }

    /**
     * @return the board as a String
     */
    public String toLogString() {
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
            return input.substring(0, width - 1 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " ";
        else
            return input + repeat(BLANK, width - input.length());
    }

    private static String getConformedStringRight(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " ";
        else
            return repeat(BLANK, width - input.length()) + input;
    }

    private static String getConformedStringCentered(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1 - CORRUPT_VALUE.length()) + CORRUPT_VALUE + " ";
        else {
            int total_blank = width - input.length();
            int total_left = total_blank / 2;
            int total_right = total_blank - total_left;
            return repeat(BLANK, total_left) + input + repeat(BLANK, total_right);
        }
    }

    private static String eval(final Object o) {
        if (o == null) {
            return nullToString;
        }
        if (o instanceof Boolean) {
            Boolean bool = (Boolean) o;
            if (bool)
                return trueToString;
            else
                return falseToString;
        }
        return o.toString().replace("\t", repeat(' ', 5));
    }

    /**
     * set the string to print the true value
     * 
     * @param trueToString
     */
    public static void setTrueToString(final String trueToString) {
        BString.trueToString = trueToString;
    }

    /**
     * set the string to print the false value
     * 
     * @param falseToString
     */
    public void setFalseToString(final String falseToString) {
        BString.falseToString = falseToString;
    }

    /**
     * set the string to print the null value
     * 
     * @param nullToString
     * @return
     */
    public String setNullToString(final String nullToString) {
        return BString.nullToString = nullToString;
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
        bs.bCenter("BString");
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
        bs.bCenter("This was a demo with BString");
        bs.bUpDown();
        System.out.println(bs.toLogString());
    }
}
