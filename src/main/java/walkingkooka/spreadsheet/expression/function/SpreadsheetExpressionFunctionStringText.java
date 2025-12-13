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

import walkingkooka.spreadsheet.convert.SpreadsheetConverters;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * The excel text function.
 */
final class SpreadsheetExpressionFunctionStringText extends SpreadsheetExpressionFunctionString {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionStringText INSTANCE = new SpreadsheetExpressionFunctionStringText();

    private SpreadsheetExpressionFunctionStringText() {
        super("text");
    }

    @Override
    public String apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final Object value = context.prepareParameter(
            VALUE,
            VALUE.getOrFail(parameters, 0, context)
        );

        final String pattern = context.prepareParameter(
            PATTERN,
            PATTERN.getOrFail(parameters, 1, context)
        );

        return SpreadsheetConverters.formatPatternToString(pattern)
            .convertOrFail(
                value,
                String.class,
                context
            );
    }

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    private final static ExpressionFunctionParameter<String> PATTERN = ExpressionFunctionParameterName.with("format-pattern")
        .required(String.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        VALUE,
        PATTERN
    );
}
