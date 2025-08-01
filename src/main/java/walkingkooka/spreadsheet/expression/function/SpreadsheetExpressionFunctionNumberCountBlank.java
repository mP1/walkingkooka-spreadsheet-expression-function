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
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * Counts the number of blank parameters given to this function.
 */
final class SpreadsheetExpressionFunctionNumberCountBlank extends SpreadsheetExpressionFunctionNumber {

    /**
     * Singleton
     */
    static final SpreadsheetExpressionFunctionNumberCountBlank INSTANCE = new SpreadsheetExpressionFunctionNumberCountBlank();

    private SpreadsheetExpressionFunctionNumberCountBlank() {
        super("countBlank");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.VALUE.variable(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_FLATTEN_RESOLVE_REFERENCES);

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        VALUE
    );

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final SpreadsheetExpressionEvaluationContext context) {
        return this.apply0(
            VALUE.getVariable(parameters, 0),
            context
        );
    }

    private ExpressionNumber apply0(final List<?> parameters,
                                    final SpreadsheetExpressionEvaluationContext context) {
        int count = 0;

        for (final Object value : parameters) {
            if (null == value ||
                (value instanceof SpreadsheetError &&
                    ((SpreadsheetError) value).isMissingCell()
                )) {
                count++;
            }
        }

        return context.expressionNumberKind()
            .create(count);
    }
}
