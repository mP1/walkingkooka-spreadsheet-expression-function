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

public final class SpreadsheetExpressionFunctionBooleanIsRefTest extends SpreadsheetExpressionFunctionBooleanTestCase<SpreadsheetExpressionFunctionBooleanIsRef> {

    @Test
    public void testCellRange() {
        this.isRefAndCheck(
            SpreadsheetSelection.parseCellRange("B2:C3"),
            true
        );
    }

    @Test
    public void testCellReference() {
        this.isRefAndCheck(
            SpreadsheetSelection.parseCell("B2"),
            true
        );
    }

    @Test
    public void testLabel() {
        this.isRefAndCheck(
            SpreadsheetSelection.labelName("Label123"),
            true
        );
    }

    @Test
    public void testBoolean() {
        this.isRefAndCheck(
            true,
            false
        );
    }

    @Test
    public void testNumber() {
        this.isRefAndCheck(
            EXPRESSION_NUMBER_KIND.one(),
            false
        );
    }

    @Test
    public void testString() {
        this.isRefAndCheck(
            "A",
            false
        );
    }

    private void isRefAndCheck(final Object value,
                               final boolean expected) {
        this.applyAndCheck2(
            Lists.of(value),
            expected
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "isRef"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionBooleanIsRef createBiFunction() {
        return SpreadsheetExpressionFunctionBooleanIsRef.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionBooleanIsRef> type() {
        return SpreadsheetExpressionFunctionBooleanIsRef.class;
    }
}
