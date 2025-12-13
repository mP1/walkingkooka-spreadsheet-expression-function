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

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.value.HasSpreadsheetErrorKind;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

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

        final Object value = VALUE.getOrFail(parameters, 0);
        int type = 0;

        if (null == value || value instanceof ExpressionNumber || value instanceof Temporal || value instanceof SpreadsheetCellReference) {
            type = 1;
        } else {
            if (context.isText(value)) {
                type = 2;
            } else {
                if (value instanceof Boolean) {
                    type = 4;
                } else {
                    if (value instanceof HasSpreadsheetErrorKind) {
                        type = 16;
                    } else {
                        if (value instanceof Collection || value instanceof SpreadsheetCellRangeReference) {
                            type = 64;
                        } else {
                            type = 128;
                        }
                    }
                }
            }
        }

        return context.expressionNumberKind()
            .create(type);
    }

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameter.VALUE.setKinds(
        Sets.of(ExpressionFunctionParameterKind.EVALUATE)
    );

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(VALUE);
}
