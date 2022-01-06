/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionBooleanIsFormulaTest extends SpreadsheetExpressionFunctionBooleanTestCase<SpreadsheetExpressionFunctionBooleanIsFormula> {

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
    public void testCellReference() {
        this.applyAndCheck2(
                Lists.of(LOAD_CELL_REFERENCE),
                true
        );
    }

    @Test
    public void testCellReferenceMissingFormula() {
        this.applyAndCheck2(
                Lists.of(CELL_EMPTY_FOMRULA.reference()),
                false
        );
    }

    @Test
    public void testMissingCellReferenceFalse() {
        this.applyAndCheck2(
                Lists.of(LOAD_CELL_REFERENCE.add(1, 1)),
                false
        );
    }

    @Test
    public void testRange() {
        this.applyAndCheck2(
                Lists.of(
                        LOAD_CELL_REFERENCE.spreadsheetCellRange(
                                LOAD_CELL_REFERENCE.add(2, 3)
                        )
                ),
                true
        );
    }

    @Test
    public void testRangeTopLeftMissingFormula() {
        this.applyAndCheck2(
                Lists.of(
                        LOAD_CELL_REFERENCE.add(1, 0)
                                .spreadsheetCellRange(
                                        LOAD_CELL_REFERENCE.add(2, 3)
                                )
                ),
                false
        );
    }

    private final static SpreadsheetCellReference B5 = SpreadsheetSelection.parseCell("B5");

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "isFormula");
    }

    @Override
    public SpreadsheetExpressionFunctionBooleanIsFormula createBiFunction() {
        return SpreadsheetExpressionFunctionBooleanIsFormula.INSTANCE;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionBooleanIsFormula> type() {
        return SpreadsheetExpressionFunctionBooleanIsFormula.class;
    }
}
