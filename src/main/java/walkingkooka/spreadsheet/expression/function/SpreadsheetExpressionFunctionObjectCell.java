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
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://exceljet.net/excel-functions/excel-cell-function
final class SpreadsheetExpressionFunctionObjectCell extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectCell INSTANCE = new SpreadsheetExpressionFunctionObjectCell();

    private SpreadsheetExpressionFunctionObjectCell() {
        super(
                "cell",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<String> TYPE_INFO = ExpressionFunctionParameterName.with("typeInfo")
            .required(String.class);

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        TYPE_INFO,
            CELL_OR_RANGE_REFERENCE_OPTIONAL
    );

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetCell cell = context.cellOrFail();

        final String typeInfo = TYPE_INFO.getOrFail(parameters, 0);
        final SpreadsheetExpressionReference selection = CELL_OR_RANGE_REFERENCE_OPTIONAL.get(parameters, 1)
                .orElseGet(cell::reference);

        return SpreadsheetExpressionFunctionObjectCellTypeInfo.typeInfo(typeInfo)
                .value(
                        selection.toCellOrFail(),
                        cell,
                        context
                );
    }
}
