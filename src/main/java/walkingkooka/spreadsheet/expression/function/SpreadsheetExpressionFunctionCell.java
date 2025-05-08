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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A custom function that retrieves a property from the active cell in the {@link SpreadsheetExpressionEvaluationContext#cell()}.
 * If the cell is absent it will as this is probably a failure within a query expression.
 */
abstract class SpreadsheetExpressionFunctionCell<T> extends SpreadsheetExpressionFunction<T> {

    SpreadsheetExpressionFunctionCell(final String name) {
        super(name);
    }

    @Override
    public final List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return ExpressionFunction.NO_PARAMETERS;
    }

    @Override
    public final T apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        return this.extractCellPropertyOrNull(
                context.cellOrFail()
        );
    }

    abstract T extractCellPropertyOrNull(final SpreadsheetCell cell);
}
