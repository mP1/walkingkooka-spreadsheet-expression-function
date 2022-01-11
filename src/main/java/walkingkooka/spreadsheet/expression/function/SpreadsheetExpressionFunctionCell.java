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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://exceljet.net/excel-functions/excel-cell-function
final class SpreadsheetExpressionFunctionCell extends SpreadsheetExpressionFunction<Object> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionCell INSTANCE = new SpreadsheetExpressionFunctionCell();

    private SpreadsheetExpressionFunctionCell() {
        super("cell");
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
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionFunctionContext context) {
        final int count = parameters.size();

        final String typeInfo;
        final SpreadsheetExpressionReference selection;
        final SpreadsheetCell cell = context.cell()
                .orElseThrow(() -> new IllegalArgumentException("Missing context cell"));

        switch(count) {
            case 1:
                typeInfo = TYPE_INFO.getOrFail(parameters, 0);
                selection = cell.reference();
                break;
            case 2:
                typeInfo = TYPE_INFO.getOrFail(parameters, 0);
                selection = CELL_OR_RANGE_REFERENCE_OPTIONAL.getOrFail(parameters, 1);
                break;
            default:
                throw new IllegalArgumentException("Expected typeInfo and possibly reference but got " + count);
        }

        return SpreadsheetExpressionFunctionCellTypeInfo.typeInfo(typeInfo)
                .value(
                        selection.toCell(),
                        cell,
                        context
                );
    }
}
