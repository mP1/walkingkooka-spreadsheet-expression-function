/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

// https://exceljet.net/excel-functions/excel-formulatext-function
final class SpreadsheetExpressionFunctionStringFormulaText extends SpreadsheetExpressionFunctionString {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionStringFormulaText INSTANCE = new SpreadsheetExpressionFunctionStringFormulaText();

    private SpreadsheetExpressionFunctionStringFormulaText() {
        super("formulatext");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            REFERENCE
    );

    @Override
    public String apply(final List<Object> parameters,
                        final SpreadsheetExpressionFunctionContext context) {
        this.checkOnlyRequiredParameters(parameters);

        final SpreadsheetCellReference cell = REFERENCE.getOrFail(parameters, 0);
        return context.loadCell(cell)
                .orElseThrow(() -> new IllegalArgumentException("Formula missing"))
                .formula()
                .text();
    }
}
