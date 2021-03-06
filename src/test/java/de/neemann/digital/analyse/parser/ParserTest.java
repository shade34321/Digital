package de.neemann.digital.analyse.parser;

import de.neemann.digital.analyse.expression.*;
import de.neemann.digital.analyse.quinemc.QuineMcCluskey;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hneemann
 */
public class ParserTest extends TestCase {

    private static Expression createSingle(String str) throws IOException, ParseException {
        List<Expression> expr = new Parser(str).parse();
        assertEquals(1, expr.size());
        return expr.get(0);
    }

    public void testIdent() throws Exception {
        assertEquals(new Variable("C"), createSingle("C"));
        assertEquals(new Variable("A_1"), createSingle("A_1"));
    }

    public void testConst() throws Exception {
        assertEquals(Constant.ZERO, createSingle("0"));
        assertEquals(Constant.ONE, createSingle("1"));
    }

    public void testParseOr() throws Exception {
        assertTrue(createSingle("a+b") instanceof Operation.Or);
        assertTrue(createSingle("a#b") instanceof Operation.Or);
        assertTrue(createSingle("a∨b") instanceof Operation.Or);
        assertTrue(createSingle("a|b") instanceof Operation.Or);
        assertTrue(createSingle("a||b") instanceof Operation.Or);
    }

    public void testParseXOr() throws Exception {
        assertTrue(createSingle("a^b") instanceof Operation.XOr);
        assertTrue(createSingle("a⊻b") instanceof Operation.XOr);
    }

    public void testParseAnd() throws Exception {
        assertTrue(createSingle("a*b") instanceof Operation.And);
        assertTrue(createSingle("a∧b") instanceof Operation.And);
        assertTrue(createSingle("a&b") instanceof Operation.And);
        assertTrue(createSingle("a&&b") instanceof Operation.And);
    }

    public void testParseParenthesis() throws Exception {
        Expression exp = createSingle("a*(b+c)");
        assertTrue(exp instanceof Operation.And);
        ArrayList<Expression> expList = ((Operation) exp).getExpressions();
        assertEquals(2, expList.size());
        assertTrue(expList.get(0) instanceof Variable);
        assertTrue(expList.get(1) instanceof Operation.Or);
        expList = ((Operation) expList.get(1)).getExpressions();
        assertEquals(2, expList.size());
        assertEquals(new Variable("b"), expList.get(0));
        assertEquals(new Variable("c"), expList.get(1));
    }

    public void testParseNot() throws Exception {
        Expression exp = createSingle("!a");
        assertTrue(exp instanceof Not);
        assertTrue(((Not) exp).getExpression() instanceof Variable);
        assertTrue(createSingle("~a") instanceof Not);
        assertTrue(createSingle("¬a") instanceof Not);
    }

    public void testParseLet() throws Exception {
        Expression exp = createSingle("let u=a+b");
        assertTrue(exp instanceof NamedExpression);
        assertEquals("u", ((NamedExpression)exp).getName());
        assertTrue(((NamedExpression)exp).getExpression() instanceof Operation.Or);
    }

    public void testParseLetError() throws Exception {
        try {
            createSingle("let u+a+b");
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    public void testParseList() throws Exception {
        ArrayList<Expression> expList = new Parser("a,b,c").parse();
        assertEquals(3, expList.size());
        assertEquals(new Variable("a"), expList.get(0));
        assertEquals(new Variable("b"), expList.get(1));
        assertEquals(new Variable("c"), expList.get(2));
    }

    public void testParseRegression() throws Exception {
        Expression e = createSingle("B*(B+A)*(B+C)*(A+B+C)");
        Expression simplified = QuineMcCluskey.simplify(e);
        assertEquals(new Variable("B"), simplified);
    }

    public void testParseRegression2() throws Exception {
        Expression e = createSingle("(C ∨ B) ∧ (A ∨ C) ∧ (B ∨ ¬C) ∧ (C ∨ ¬A)");
        Expression simplified = QuineMcCluskey.simplify(e);
        assertTrue(simplified instanceof Operation.And);
        ArrayList<Expression> expList = ((Operation) simplified).getExpressions();
        assertEquals(2, expList.size());
        assertEquals(new Variable("B"), expList.get(0));
        assertEquals(new Variable("C"), expList.get(1));
    }

    public void testParseException() throws Exception {
        Parser p = new Parser("C+");
        try {
            p.parse();
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    public void testParseException2() throws Exception {
        Parser p = new Parser("(C");
        try {
            p.parse();
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    public void testParseException3() throws Exception {
        Parser p = new Parser("*C");
        try {
            p.parse();
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    public void testParseException4() throws Exception {
        Parser p = new Parser("A )");
        try {
            p.parse();
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    public void testParseException5() throws Exception {
        Parser p = new Parser("ö");
        try {
            p.parse();
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

}