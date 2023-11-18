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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionStringFormulaTextTest extends SpreadsheetExpressionFunctionStringTestCase<SpreadsheetExpressionFunctionStringFormulaText> {

    @Test
    public void testApply() {
        this.applyAndCheck2(
                Lists.of(LOAD_CELL_REFERENCE),
                LOAD_FORMULA_TEXT
        );
    }

    @Test
    public void testApplyMissingCellFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2(
                        SpreadsheetSelection.parseCell("Z999")
                )
        );

        this.checkEquals(
                "Formula missing from Z999",
                thrown.getMessage()
        );
    }

    @Test
    public void testCellMissingFormulaFail() {

    }

    @Override
    public SpreadsheetExpressionFunctionStringFormulaText createBiFunction() {
        return SpreadsheetExpressionFunctionStringFormulaText.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionStringFormulaText> type() {
        return SpreadsheetExpressionFunctionStringFormulaText.class;
    }
}
