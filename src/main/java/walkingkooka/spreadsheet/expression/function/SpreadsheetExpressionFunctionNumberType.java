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

import walkingkooka.spreadsheet.HasSpreadsheetErrorKind;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

// https://exceljet.net/excel-functions/excel-type-function
final class SpreadsheetExpressionFunctionNumberType extends SpreadsheetExpressionFunctionNumber {

    final static SpreadsheetExpressionFunctionNumberType INSTANCE = new SpreadsheetExpressionFunctionNumberType();

    private SpreadsheetExpressionFunctionNumberType() {
        super("type");
    }

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        int type = 0;

        do {
            final Object value = ExpressionFunctionParameter.VALUE.getOrFail(parameters, 0);

            if (null == value || value instanceof ExpressionNumber || value instanceof Temporal || value instanceof SpreadsheetCellReference) {
                type = 1;
                break;
            }
            if (context.isText(value)) {
                type = 2;
                break;
            }
            if (value instanceof Boolean) {
                type = 4;
                break;
            }
            if (value instanceof HasSpreadsheetErrorKind) {
                type = 16;
                break;
            }
            if (value instanceof Collection || value instanceof SpreadsheetCellRange) {
                type = 64;
                break;
            }
            type = 128;
            break;
        } while (false);

        return context.expressionNumberKind()
                .create(type);
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            ExpressionFunctionParameter.VALUE
    );
}
