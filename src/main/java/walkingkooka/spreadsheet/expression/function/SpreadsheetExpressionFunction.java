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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

/**
 * A {@link ExpressionFunction} with an assumed {@link SpreadsheetExpressionEvaluationContext}.
 */
abstract class SpreadsheetExpressionFunction<T> implements ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> {

    final static ExpressionFunctionParameter<SpreadsheetCellReference> REFERENCE = ExpressionFunctionParameterName.with("reference")
            .required(SpreadsheetCellReference.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<SpreadsheetExpressionReference> CELL_OR_RANGE_REFERENCE = ExpressionFunctionParameterName.with("reference")
            .required(SpreadsheetExpressionReference.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<SpreadsheetExpressionReference> CELL_OR_RANGE_REFERENCE_OPTIONAL = ExpressionFunctionParameterName.with("reference")
            .optional(SpreadsheetExpressionReference.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    SpreadsheetExpressionFunction(final String name) {
        super();
        this.name = FunctionExpressionName.with(name);
    }

    @Override
    public final FunctionExpressionName name() {
        return name;
    }

    private final FunctionExpressionName name;

    /**
     * All functions are pure except for cell.
     */
    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return !(
                this instanceof SpreadsheetExpressionFunctionObjectCell ||
                        this instanceof SpreadsheetExpressionFunctionOffset
        );
    }

    @Override
    public final String toString() {
        return this.name().toString();
    }
}
