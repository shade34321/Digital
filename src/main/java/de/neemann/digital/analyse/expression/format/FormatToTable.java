package de.neemann.digital.analyse.expression.format;


import de.neemann.digital.analyse.expression.ContextFiller;
import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;

/**
 * Creates a table representation of the given expression
 *
 * @author hneemann
 */
public class FormatToTable implements Formatter {

    @Override
    public String format(String name, Expression expression) throws FormatterException, ExpressionException {
        StringBuilder sb = new StringBuilder();
        ContextFiller cf = new ContextFiller(expression);
        formatHead(sb, cf.getVarCount());
        for (Variable v : cf)
            sb.append(formatVariable(v));
        sb.append(formatResultVariable());
        sb.append("\n");
        formatTableStart(sb);

        for (int i = 0; i < cf.getRowCount(); i++) {
            cf.setContextTo(i);
            for (Variable v : cf)
                sb.append(formatValue(cf.get(v)));
            sb.append(formatResult(expression.calculate(cf)));
            sb.append("\n");
        }
        formatEnd(sb);
        return sb.toString();
    }

    /**
     * Formats the table head
     *
     * @param sb       the string builder to add the text to
     * @param varCount the number of variables
     */
    protected void formatHead(StringBuilder sb, int varCount) {
    }

    /**
     * Formats the table start
     *
     * @param sb the string builder to add the text to
     */
    protected void formatTableStart(StringBuilder sb) {
    }

    /**
     * Formats the table end
     *
     * @param sb the string builder to add the text to
     */
    protected void formatEnd(StringBuilder sb) {
    }

    /**
     * Formats a variable
     *
     * @param v the variable
     * @return the formatted text
     */
    protected String formatVariable(Variable v) {
        return v.getIdentifier();
    }


    /**
     * formats the result variable
     *
     * @return the formatted text
     */
    protected String formatResultVariable() {
        return "|Y";
    }

    /**
     * Formats a result value
     *
     * @param value the value to format
     * @return the formatted text
     */
    protected String formatResult(boolean value) {
        return "|" + formatValue(value);
    }

    /**
     * Formats a value
     *
     * @param val the value
     * @return the formatted text
     */
    protected String formatValue(boolean val) {
        if (val) return "1";
        else return "0";
    }
}
