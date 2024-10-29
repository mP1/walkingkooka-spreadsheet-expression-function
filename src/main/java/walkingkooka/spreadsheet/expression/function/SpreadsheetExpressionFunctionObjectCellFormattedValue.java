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
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A custom function that retrieves the current cell formatted value.
 * This is intended to be used to create a query expression and not actual cell formulas.
 */
final class SpreadsheetExpressionFunctionObjectCellFormattedValue extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectCellFormattedValue INSTANCE = new SpreadsheetExpressionFunctionObjectCellFormattedValue();

    private SpreadsheetExpressionFunctionObjectCellFormattedValue() {
        super("cellFormattedValue");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return ExpressionFunctionParameter.EMPTY;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        return context.cellOrFail()
                .formattedValue()
                .orElse(null);
    }
}
