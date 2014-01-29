package com.rla.board.main;

/**
 * @author Raphaël Laporte 4 jan. 2012 rl@pcsol.be,
 *         raphael.laporte@skynet.be
 * 
 */

public class BoardString {
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

    private Object board;

    private boolean threadSafe;

    @SuppressWarnings("unused")
    private BoardString() {
    }

    /**
     * (is not append threadSafe)
     * 
     * @param widthLabel
     *            : width of space for the labels prints
     * @param widthValue
     *            : width of space for the values prints this board
     */
    public BoardString(final int widthLabel, final int widthValue) {
        this(widthLabel, widthValue, false);
    }

    /**
     * @param widthLabel
     *            : width of space for the labels prints
     * @param widthValue
     *            : width of space for the values prints
     * @param threadSafe
     *            : true if you want this board is append threadSafe
     */
    public BoardString(final int widthLabel, final int widthValue, final boolean appendThreadSafe) {
        this.output_Width = widthLabel + widthValue + 3;// 2PIPES+DEFINE
        this.label_Width = widthLabel;
        this.value_Width = widthValue;
        this.threadSafe = appendThreadSafe;
        if (this.threadSafe)
            board = new StringBuffer();
        else
            board = new StringBuilder();
    }

    /**
     * starts a boardString
     */
    public void bStart() {
        append("" + END_LINE + repeat(DASH, output_Width) + END_LINE);

    }

    /**
     * ends a boardString
     */
    public void bEnd() {
        append("" + repeat(DASH, output_Width) + END_LINE);
    }

    /**
     * prints a simple separation
     */
    public void bSeparation() {
        append("" + PLUS + repeat(DASH, output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a double separation
     */
    public void bSeparationDouble() {
        append("" + PLUS + repeat(DDASH, output_Width - 2) + PLUS + END_LINE);
    }

    /**
     * prints a new line into boardString
     */
    public void bNewLine() {
        append("" + PIPE + repeat(BLANK, output_Width - 2) + PIPE + END_LINE);
    }

    /**
     * prints alternatively up and down chars begins with up char see
     * bDownUp
     */
    public void bUpDown() {
        for (int i = 0; i < output_Width; i++)
            append("" + ((i % 2 == 0) ? UP : DOWN));
        append("" + END_LINE);
    }

    /**
     * prints alternatively down and up chars begins with down char
     * see bUpDown
     */
    public void bDownUp() {
        for (int i = 0; i < output_Width; i++)
            append("" + ((i % 2 == 0) ? DOWN : UP));
        append("" + END_LINE);
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
        if (len == output_Width - 2)
            append("" + PIPE + toLeftTmp + PIPE + END_LINE);
        else if (len >= output_Width - 2)
            append("" + PIPE + toLeftTmp.substring(0, output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = output_Width - 2 - len;
            append("" + PIPE + toLeftTmp + repeat(BLANK, all_blanks_count) + PIPE + END_LINE);
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
        if (len == output_Width - 2)
            append("" + PIPE + toRightTmp + PIPE + END_LINE);
        else if (len > output_Width - 2)
            append("" + PIPE + toRightTmp.substring(0, output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        else {
            int all_blanks_count = output_Width - 2 - len;
            append("" + PIPE + repeat(BLANK, all_blanks_count) + toRightTmp + PIPE + END_LINE);
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
        if (len == output_Width - 2)
            append("" + PIPE + toCenterTmp + PIPE + END_LINE);
        else if (len >= output_Width - 2) {
            append("" + PIPE + toCenterTmp.substring(0, output_Width - 3) + CORRUPT_VALUE + PIPE + END_LINE);
        } else {
            int all_blanks_count = output_Width - 2 - len;
            int left_blank = all_blanks_count / 2;
            append("" + PIPE + repeat(BLANK, left_blank) + toCenterTmp + repeat(BLANK, all_blanks_count - left_blank) + PIPE + END_LINE);
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
        String labelStr = getConformedStringLeft(getBorder() + label, label_Width);
        String valueStr = getConformedStringRight(eval(value) + getBorder(), value_Width - DEFINE.length() + 1);
        append("" + PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
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
        String labelStr = getConformedStringLeft(getBorder() + label, label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), value_Width - DEFINE.length() + 1);
        append("" + PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
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
        String labelStr = getConformedStringRight(getBorder() + label, label_Width);
        String valueStr = getConformedStringLeft(eval(value) + getBorder(), value_Width - DEFINE.length() + 1);
        append("" + PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
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
        append("" + PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
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
        String labelStr = getConformedStringCentered(getBorder() + label, label_Width);
        String valueStr = getConformedStringCentered(eval(value) + getBorder(), value_Width - DEFINE.length() + 1);
        append("" + PIPE + labelStr + DEFINE + valueStr + PIPE + END_LINE);
    }

    /**
     * @return the board as a String
     */
    public String getBoardString() {
        String ret = board.toString();
        return ret;
    }

    /**
     * @return the board as a String for html placement
     */
    public String getWebBoardString() {
        return getBoardString().replace(" ", "&nbsp;");
    }

    private String repeat(final char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (; count-- > 0;)
            sb.append(c);
        return sb.toString();
    }

    private String getConformedStringLeft(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1) + CORRUPT_VALUE;
        else
            return input + repeat(BLANK, width - input.length());
    }

    private String getConformedStringRight(final String input, final int width) {
        if (input.length() == width)
            return input;
        else if (input.length() > width)
            return input.substring(0, width - 1) + CORRUPT_VALUE;
        else
            return repeat(BLANK, width - input.length()) + input;
    }

    private String getConformedStringCentered(final String input, final int width) {
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
                return trueToString;
            else
                return falseToString;
        }
        return (o != null) ? o.toString() : nullToString;
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

    /**
     * set the width between the border of the board and the labels or
     * values
     * 
     * @param width
     */
    public void setBorderWidth(final int width) {
        this.borderWidth = width;
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
        if (!this.threadSafe)
            ((StringBuilder) (board)).append(toAppend);
        else
            ((StringBuffer) (board)).append(toAppend);
    }

    public static void main(String[] args) {
        BoardString bs = new BoardString("1234567901234567901234567890".length(), "12345679012345679012345".length(), false);
        bs.setBorderWidth(1);
        bs.bStart();
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
        bs.bUpDown();
        bs.setDefineString("");
        bs.bPrintLR("4 jan. 2012", "written by rla");
        bs.setDefineString(": ");
        bs.bUpDown();
        bs.bEnd();
        System.out.println(bs.getBoardString());
    }
}
