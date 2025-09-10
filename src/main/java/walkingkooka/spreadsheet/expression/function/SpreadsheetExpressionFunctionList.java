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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that accepts zero or more values and returns an immutable {@link List}. Note null values will result in an error.
 * <pre>
 * list("A", 2)
 * </pre>
 */
final class SpreadsheetExpressionFunctionList extends SpreadsheetExpressionFunction<List<?>> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionList INSTANCE = new SpreadsheetExpressionFunctionList();

    private SpreadsheetExpressionFunctionList() {
        super("list");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Object> VALUES = ExpressionFunctionParameterName.VALUE.variable(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_FLATTEN_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        VALUES
    );

    @Override
    public Class<List<?>> returnType() {
        return Cast.to(List.class);
    }

    @Override
    public List<?> apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        return VALUES.getVariable(parameters, 0);
    }
}
