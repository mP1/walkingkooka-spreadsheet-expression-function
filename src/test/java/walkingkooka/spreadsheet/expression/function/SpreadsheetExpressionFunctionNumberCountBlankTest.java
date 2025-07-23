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
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.time.LocalDateTime;

public final class SpreadsheetExpressionFunctionNumberCountBlankTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberCountBlank> {

    // countIf.........................................................................................................

    @Test
    public void testApplyEmptyParameters() {
        this.applyAndCheck(
            Lists.empty(),
            EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testApplyNonNullBlank() {
        this.applyAndCheck(
            Lists.of(
                11,
                22
            ),
            EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testApplyOnlyNulls() {
        this.applyAndCheck(
            Lists.of(
                null,
                null
            ),
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testApplyOnlySpreadsheetErrorNotFound() {
        this.applyAndCheck(
            Lists.of(
                SpreadsheetError.selectionNotFound(SpreadsheetSelection.A1),
                SpreadsheetError.selectionNotFound(SpreadsheetSelection.parseCell("B2"))
            ),
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testApplyMixedNullNotFoundAndOthers() {
        this.applyAndCheck(
            Lists.of(
                SpreadsheetError.selectionNotFound(SpreadsheetSelection.A1),
                null,
                EXPRESSION_NUMBER_KIND.create(2),
                LocalDateTime.now()
            ),
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Override
    public SpreadsheetExpressionFunctionNumberCountBlank createBiFunction() {
        return SpreadsheetExpressionFunctionNumberCountBlank.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionNumberCountBlank> type() {
        return SpreadsheetExpressionFunctionNumberCountBlank.class;
    }
}
