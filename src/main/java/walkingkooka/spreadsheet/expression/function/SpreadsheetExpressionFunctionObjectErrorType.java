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
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

// https://exceljet.net/excel-functions/excel-errortype-function
final class SpreadsheetExpressionFunctionObjectErrorType extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectErrorType INSTANCE = new SpreadsheetExpressionFunctionObjectErrorType();

    private SpreadsheetExpressionFunctionObjectErrorType() {
        super(
                "Error.Type",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS,
                ExpressionFunctionKind.RESOLVE_REFERENCES
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    // Expecting a HasSpreadsheetErrorKind but could be anything.
    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameter.VALUE;

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            VALUE
    );

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionFunctionContext context) {
        this.checkParameterCount(parameters);

        final Object value = VALUE.getOrFail(parameters, 0);

        Object result;

        if (value instanceof HasSpreadsheetErrorKind) {
            final HasSpreadsheetErrorKind has = (HasSpreadsheetErrorKind) value;
            result = context.expressionNumberKind()
                    .create(has.spreadsheetErrorKind().value());
        } else {
            result = na(value);
        }

        return result;
    }

    static SpreadsheetError na(final Object value) {
        return SpreadsheetErrorKind.NA.setMessage(
                "Expected error got: " +
                        CharSequences.quoteIfChars(value)
        );
    }
}
