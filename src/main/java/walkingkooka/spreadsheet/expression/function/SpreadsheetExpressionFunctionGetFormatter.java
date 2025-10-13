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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that may be used to get a {@link SpreadsheetFormatterSelector}.
 */
final class SpreadsheetExpressionFunctionGetFormatter extends SpreadsheetExpressionFunction<SpreadsheetFormatterSelector> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionGetFormatter INSTANCE = new SpreadsheetExpressionFunctionGetFormatter();

    private SpreadsheetExpressionFunctionGetFormatter() {
        super("getFormatter");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetFormatterSelector> FORMATTER = ExpressionFunctionParameterName.with("formatter")
        .required(SpreadsheetFormatterSelector.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        FORMATTER
    );

    @Override
    public Class<SpreadsheetFormatterSelector> returnType() {
        return SpreadsheetFormatterSelector.class;
    }

    @Override
    public SpreadsheetFormatterSelector apply(final List<Object> parameters,
                                              final SpreadsheetExpressionEvaluationContext context) {
        return FORMATTER.getOrFail(parameters, 0);

    }
}
