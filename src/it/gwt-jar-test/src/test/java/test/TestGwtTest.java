package test;

import com.google.gwt.junit.client.GWTTestCase;

import walkingkooka.collect.list.Lists;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;

@LocaleAware
public class TestGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "test.Test";
    }

    public void testAssertEquals() {
        assertEquals(
            1,
            1
        );
    }

    public void testColumnE1() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        assertEquals(
            kind.create(5),
            SpreadsheetExpressionFunctions.column()
                .apply(
                    Lists.of(SpreadsheetSelection.parseCell("E1")),
                    new FakeSpreadsheetExpressionEvaluationContext() {

                        @Override
                        public ExpressionNumberKind expressionNumberKind() {
                            return kind;
                        }
                    }
                )
        );
    }
}
