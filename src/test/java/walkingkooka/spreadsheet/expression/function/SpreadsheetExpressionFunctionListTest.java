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

import java.util.List;

public final class SpreadsheetExpressionFunctionListTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionList, List<?>> {

    @Test
    public void testApplyZeroValues() {
        this.applyAndCheck(
            Lists.empty(),
            Lists.empty()
        );
    }

    @Test
    public void testApply() {
        final List<Object> values = Lists.of(
            "A",
            2,
            true
        );
        this.applyAndCheck2(
            values,
            values
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "list"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionList createBiFunction() {
        return SpreadsheetExpressionFunctionList.INSTANCE;
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
    public Class<SpreadsheetExpressionFunctionList> type() {
        return SpreadsheetExpressionFunctionList.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}
