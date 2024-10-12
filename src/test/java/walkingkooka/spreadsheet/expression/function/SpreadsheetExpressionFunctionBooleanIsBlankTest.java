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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionBooleanIsBlankTest extends SpreadsheetExpressionFunctionBooleanTestCase<SpreadsheetExpressionFunctionBooleanIsBlank> {

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
    public void testNull() {
        this.isBlankAndCheck(
                null,
                false
        );
    }

    @Test
    public void testZero() {
        this.isBlankAndCheck(
                ExpressionNumberKind.DOUBLE.zero(),
                false
        );
    }

    @Test
    public void testEmptyString() {
        this.isBlankAndCheck(
                "",
                false
        );
    }

    @Test
    public void testExistingCellReference() {
        this.isBlankAndCheck(
                LOAD_CELL_REFERENCE,
                false
        );
    }

    @Test
    public void testMissingCellReference() {
        this.isBlankAndCheck(
                SpreadsheetSelection.parseCell("M404"),
                true
        );
    }

    @Test
    public void testRangeTopLeftMissingCellReference() {
        this.isBlankAndCheck(
                REFERENCE.cellRange(
                        REFERENCE.add(1, 1)
                ),
                false
        );
    }

    @Test
    public void testRange() {
        this.isBlankAndCheck(
                LOAD_CELL_REFERENCE.cellRange(
                        LOAD_CELL_REFERENCE.add(2, 3)
                ),
                false
        );
    }

    private void isBlankAndCheck(final Object value,
                                 final boolean expected) {
        this.applyAndCheck2(
                Lists.of(
                        value
                ),
                expected
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "isBlank");
    }

    @Override
    public SpreadsheetExpressionFunctionBooleanIsBlank createBiFunction() {
        return SpreadsheetExpressionFunctionBooleanIsBlank.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionBooleanIsBlank> type() {
        return SpreadsheetExpressionFunctionBooleanIsBlank.class;
    }
}
