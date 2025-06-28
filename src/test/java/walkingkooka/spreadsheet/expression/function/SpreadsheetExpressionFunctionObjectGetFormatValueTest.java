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
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionObjectGetFormatValueTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectGetFormatValue> {

    private final static Object VALUE = "abc123";

    @Test
    public void testApply() {
        this.applyAndCheck2(
                Lists.empty(),
                VALUE
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "getFormatValue"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectGetFormatValue createBiFunction() {
        return SpreadsheetExpressionFunctionObjectGetFormatValue.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public Optional<Object> formatValue() {
                return Optional.of(VALUE);
            }
        };
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectGetFormatValue> type() {
        return SpreadsheetExpressionFunctionObjectGetFormatValue.class;
    }
}
