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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetExpressionFunctionNextEmptyRowTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionNextEmptyRow, SpreadsheetRowReference> {

    @Test
    public void testApply() {
        this.applyAndCheck2(
            Lists.of(
                SpreadsheetSelection.parseColumn("Z")
            ),
            SpreadsheetSelection.parseRow("1")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "nextEmptyRow"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionNextEmptyRow createBiFunction() {
        return SpreadsheetExpressionFunctionNextEmptyRow.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionNextEmptyRow> type() {
        return SpreadsheetExpressionFunctionNextEmptyRow.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}
