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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://exceljet.net/excel-functions/excel-indirect-function
final class SpreadsheetExpressionFunctionIndirect extends SpreadsheetExpressionFunction<SpreadsheetCellReference> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionIndirect INSTANCE = new SpreadsheetExpressionFunctionIndirect();

    private SpreadsheetExpressionFunctionIndirect() {
        super("indirect",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetCellReference> REFERENCE = ExpressionFunctionParameterName.REFERENCE.required(SpreadsheetCellReference.class);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(REFERENCE);

    @Override
    public Class<SpreadsheetCellReference> returnType() {
        return SpreadsheetCellReference.class;
    }

    @Override
    public SpreadsheetCellReference apply(final List<Object> parameters,
                                          final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        return REFERENCE.getOrFail(parameters, 0); // parameter will be converted during get to SpreadsheetCellReference
    }
}
