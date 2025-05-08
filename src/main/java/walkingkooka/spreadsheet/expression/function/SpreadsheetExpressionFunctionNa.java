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

import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

// https://exceljet.net/excel-functions/excel-na-function
final class SpreadsheetExpressionFunctionNa extends SpreadsheetExpressionFunction<SpreadsheetError> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionNa INSTANCE = new SpreadsheetExpressionFunctionNa();

    private SpreadsheetExpressionFunctionNa() {
        super("na");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return ExpressionFunction.NO_PARAMETERS;
    }

    @Override
    public Class<SpreadsheetError> returnType() {
        return SpreadsheetError.class;
    }

    @Override
    public SpreadsheetError apply(final List<Object> parameters,
                                  final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);
        return NA;
    }

    private final static SpreadsheetError NA = SpreadsheetErrorKind.NA.setMessage("NA()");
}
