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

import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SpreadsheetExpressionFunctionStringTestCase<F extends SpreadsheetExpressionFunctionString> extends SpreadsheetExpressionFunctionTestCase<F, String> {
    SpreadsheetExpressionFunctionStringTestCase() {
        super();
    }

    @Override
    public final void applyAndCheck2(final F function,
                                     final List<Object> parameters,
                                     final String result) {
        this.applyAndCheck2(
            function,
            parameters.stream()
                .map(i -> i instanceof Number ? EXPRESSION_NUMBER_KIND.create((Number) i) : i)
                .collect(Collectors.toList()),
            this.createContext(),
            result
        );
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    @Override
    public final String typeNamePrefix() {
        return SpreadsheetExpressionFunctionString.class.getSimpleName();
    }
}
