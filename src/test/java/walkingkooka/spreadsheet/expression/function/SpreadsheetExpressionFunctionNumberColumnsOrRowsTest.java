/*
 * Copyright 2022 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionNumberColumnsOrRowsTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberColumnsOrRows> {

    @Test
    public void testTwoParametersFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.apply2(
                "1a", "2b"
            )
        );
    }

    @Test
    public void testColumnReferenceParameterMissingFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.apply2(
                SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS,
                Lists.empty()
            )
        );
    }

    @Test
    public void testRowReferenceParameterMissingFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.apply2(
                SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS,
                Lists.empty()
            )
        );
    }

    private final static SpreadsheetCellReference B5 = SpreadsheetSelection.parseCell("B5");

    @Test
    public void testColumnReferenceParameter() {
        this.applyAndCheck2(
            SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS,
            Lists.of(B5),
            KIND.one()
        );
    }

    @Test
    public void testRowReferenceParameter() {
        this.applyAndCheck2(
            SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS,
            Lists.of(B5),
            KIND.one()
        );
    }


    @Test
    public void testColumnReferenceRange() {
        this.applyAndCheck3(
            SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS,
            SpreadsheetSelection.parseCellRange("B2:E3"),
            4
        );
    }

    @Test
    public void testRowReferenceRange() {
        this.applyAndCheck3(
            SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS,
            SpreadsheetSelection.parseCellRange("B2:C5"),
            4
        );
    }

    private void applyAndCheck3(final SpreadsheetExpressionFunctionNumberColumnsOrRows function,
                                final SpreadsheetSelection selection,
                                final int expected) {
        this.applyAndCheck2(
            function,
            Lists.of(selection),
            KIND.create(expected)
        );
    }

    @Test
    public void testToStringColumn() {
        this.toStringAndCheck(SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS, "columns");
    }

    @Test
    public void testToStringRows() {
        this.toStringAndCheck(SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS, "rows");
    }

    @Override
    public SpreadsheetExpressionFunctionNumberColumnsOrRows createBiFunction() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    @Override
    public Class<SpreadsheetExpressionFunctionNumberColumnsOrRows> type() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.class;
    }
}
