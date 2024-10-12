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

public final class SpreadsheetExpressionFunctionNumberColumnOrRowTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberColumnOrRow> {

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
    public void testColumnReferenceParameterMissing() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN,
                Lists.empty(),
                KIND.create(1 + REFERENCE.column().value())
        );
    }

    @Test
    public void testRowReferenceParameterMissing() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionNumberColumnOrRow.ROW,
                Lists.empty(),
                KIND.create(1 + REFERENCE.row().value())
        );
    }

    private final static SpreadsheetCellReference B5 = SpreadsheetSelection.parseCell("B5");

    @Test
    public void testColumnReferenceParameterPresent() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN,
                Lists.of(B5),
                KIND.create(1 + B5.column().value())
        );
    }

    @Test
    public void testRowReferenceParameterPresent() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionNumberColumnOrRow.ROW,
                Lists.of(B5),
                KIND.create(1 + B5.row().value())
        );
    }

    @Test
    public void testToStringColumn() {
        this.toStringAndCheck(SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN, "column");
    }

    @Test
    public void testToStringRow() {
        this.toStringAndCheck(SpreadsheetExpressionFunctionNumberColumnOrRow.ROW, "row");
    }

    @Override
    public SpreadsheetExpressionFunctionNumberColumnOrRow createBiFunction() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN;
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
    public Class<SpreadsheetExpressionFunctionNumberColumnOrRow> type() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.class;
    }
}
