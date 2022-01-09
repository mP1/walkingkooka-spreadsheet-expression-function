/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

/**
 * A {@link ExpressionFunction} with an assumed {@Link SpreadsheetExpressionFunctionContext}.
 */
abstract class SpreadsheetExpressionFunction<T> implements ExpressionFunction<T, SpreadsheetExpressionFunctionContext> {

    final static ExpressionFunctionParameter<SpreadsheetCellReference> REFERENCE =  ExpressionFunctionParameterName.with("reference")
            .setType(SpreadsheetCellReference.class);

    final static ExpressionFunctionParameter<SpreadsheetExpressionReference> CELL_OR_RANGE_REFERENCE =  ExpressionFunctionParameterName.with("reference")
            .setType(SpreadsheetExpressionReference.class);

    SpreadsheetExpressionFunction(final String name) {
        super();
        this.name = FunctionExpressionName.with(name);
    }

    @Override
    public final FunctionExpressionName name() {
        return name;
    }

    private final FunctionExpressionName name;


    @Override
    public final boolean lsLastParameterVariable() {
        return false;
    }

    /**
     * All number functions are pure. Does not assume anything about any parameters.
     */
    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public final boolean requiresEvaluatedParameters() {
        return true;
    }

    @Override
    public final boolean resolveReferences() {
        return !(
                this instanceof SpreadsheetExpressionFunctionCell ||
                        this instanceof SpreadsheetExpressionFunctionNumberColumnOrRow ||
                        this instanceof SpreadsheetExpressionFunctionNumberColumnsOrRows
        );
    }

    @Override
    public final String toString() {
        return this.name().toString();
    }
}
