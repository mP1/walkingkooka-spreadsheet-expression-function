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
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Optional;

/**
 * A {@link ExpressionFunction} which accepts a {@link SpreadsheetFormatterSelector} and a {@link Object value}.
 * A {@link SpreadsheetFormatter} identified by the {@link SpreadsheetFormatterSelector} will be fetched and used to
 * format the given value.
 */
final class SpreadsheetExpressionFunctionTextNodeFormatValue extends SpreadsheetExpressionFunctionTextNode {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionTextNodeFormatValue INSTANCE = new SpreadsheetExpressionFunctionTextNodeFormatValue();

    private SpreadsheetExpressionFunctionTextNodeFormatValue() {
        super("formatValue");
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

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        FORMATTER,
        VALUE
    );

    @Override
    public TextNode apply(final List<Object> parameters,
                          final SpreadsheetExpressionEvaluationContext context) {
        final SpreadsheetFormatterContext formatterContext = context.spreadsheetFormatterContext(
            context.cell()
        );

        final SpreadsheetFormatter formatter = formatterContext.spreadsheetFormatter(
            FORMATTER.getOrFail(
                parameters,
                0
            )
        );

        final Object value = VALUE.getOrFail(
            parameters,
            1
        );

        return formatter.format(
            Optional.ofNullable(value),
            formatterContext
        ).orElse(null);
    }
}
