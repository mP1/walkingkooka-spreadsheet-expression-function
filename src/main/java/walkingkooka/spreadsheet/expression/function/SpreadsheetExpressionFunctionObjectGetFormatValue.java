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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Optional;

/**
 * A function that should only be used with a {@link walkingkooka.spreadsheet.format.SpreadsheetFormatter#format(Optional, SpreadsheetFormatterContext)},
 * and may be used by an expression to retrieve the value being formatted via function instead of a using {@link SpreadsheetExpressionEvaluationContext#FORMAT_VALUE}.
 */
final class SpreadsheetExpressionFunctionObjectGetFormatValue extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectGetFormatValue INSTANCE = new SpreadsheetExpressionFunctionObjectGetFormatValue();

    private SpreadsheetExpressionFunctionObjectGetFormatValue() {
        super("getFormatValue");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return ExpressionFunction.NO_PARAMETERS;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);
        return context.formatValue()
            .orElse(null); //unwrap Optional
    }
}
