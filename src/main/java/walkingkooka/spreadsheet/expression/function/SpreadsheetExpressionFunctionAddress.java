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
import walkingkooka.spreadsheet.reference.SpreadsheetReferenceKind;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://exceljet.net/excel-functions/excel-address-function
final class SpreadsheetExpressionFunctionAddress extends SpreadsheetExpressionFunction<SpreadsheetCellReference> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionAddress INSTANCE = new SpreadsheetExpressionFunctionAddress();

    private SpreadsheetExpressionFunctionAddress() {
        super(
                "address",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                ExpressionFunctionKind.RESOLVE_REFERENCES
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<ExpressionNumber> ROW_NUM = ExpressionFunctionParameterName.with("row-num")
            .required(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<ExpressionNumber> COLUMN_NUM = ExpressionFunctionParameterName.with("col-num")
            .required(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<ExpressionNumber> ABS_NUM = ExpressionFunctionParameterName.with("abs-num")
            .required(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<Boolean> A1_STYLE = ExpressionFunctionParameterName.with("a1-style")
            .required(Boolean.class);

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            ROW_NUM,
            COLUMN_NUM,
            ABS_NUM,
            A1_STYLE
    );

    @Override
    public Class<SpreadsheetCellReference> returnType() {
        return SpreadsheetCellReference.class;
    }

    @Override
    public SpreadsheetCellReference apply(final List<Object> parameters,
                                          final SpreadsheetExpressionEvaluationContext context) {
        final int count = parameters.size();

        final int row;
        final int column;
        final int absNum;
        final boolean a1Style;

        switch (count) {
            case 2:
                row = ROW_NUM.getOrFail(parameters, 0).intValue();
                column = COLUMN_NUM.getOrFail(parameters, 1).intValue();
                absNum = 1;
                a1Style = true;
                break;
            case 3:
                row = ROW_NUM.getOrFail(parameters, 0).intValue();
                column = COLUMN_NUM.getOrFail(parameters, 1).intValue();
                absNum = ABS_NUM.getOrFail(parameters, 2).intValue();
                a1Style = true;
                break;
            case 4:
                row = ROW_NUM.getOrFail(parameters, 0).intValue();
                column = COLUMN_NUM.getOrFail(parameters, 1).intValue();
                absNum = ABS_NUM.getOrFail(parameters, 2).intValue();
                a1Style = A1_STYLE.getOrFail(parameters, 3);
                break;
            default:
                throw new IllegalArgumentException("Expected " + ROW_NUM + ", " + COLUMN_NUM + ", " + ABS_NUM + ", " + A1_STYLE + " but got " + count);
        }

        final SpreadsheetReferenceKind rowKind;
        final SpreadsheetReferenceKind columnKind;

        switch (absNum) {
            case 1:
                rowKind = SpreadsheetReferenceKind.ABSOLUTE;
                columnKind = SpreadsheetReferenceKind.ABSOLUTE;
                break;
            case 2:
                rowKind = SpreadsheetReferenceKind.ABSOLUTE;
                columnKind = SpreadsheetReferenceKind.RELATIVE;
                break;
            case 3:
                rowKind = SpreadsheetReferenceKind.RELATIVE;
                columnKind = SpreadsheetReferenceKind.ABSOLUTE;
                break;
            case 4:
                rowKind = SpreadsheetReferenceKind.RELATIVE;
                columnKind = SpreadsheetReferenceKind.RELATIVE;
                break;
            default:
                throw new IllegalArgumentException("Invalid " + ABS_NUM + " got " + absNum);
        }

        if (!a1Style) {
            throw new IllegalArgumentException("Invalid " + A1_STYLE + " must be true, got " + absNum);
        }

        return rowKind.row(row - 1)
                .setColumn(
                        columnKind.column(column - 1)
                );
    }
}
