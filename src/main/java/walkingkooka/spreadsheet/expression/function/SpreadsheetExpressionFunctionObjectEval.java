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
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A {@link ExpressionFunction} which expects a single String which should be converted into an {@link Expression} and then
 * evaluated.
 */
final class SpreadsheetExpressionFunctionObjectEval extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectEval INSTANCE = new SpreadsheetExpressionFunctionObjectEval();

    private SpreadsheetExpressionFunctionObjectEval() {
        super("eval");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        return context.evaluateExpression(
            EXPRESSION.getOrFail(
                parameters,
                0
            )
        );
    }

    private final static ExpressionFunctionParameter<Expression> EXPRESSION = ExpressionFunctionParameterName.with("expression")
        .required(Expression.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        EXPRESSION
    );
}
