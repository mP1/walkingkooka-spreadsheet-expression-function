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
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;

// https://exceljet.net/excel-functions/excel-errortype-function
final class SpreadsheetExpressionFunctionObjectErrorType extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectErrorType INSTANCE = new SpreadsheetExpressionFunctionObjectErrorType();

    private SpreadsheetExpressionFunctionObjectErrorType() {
        super("Error.Type");
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final Object value = VALUE.getOrFail(parameters, 0, context);

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

    // Expecting a HasSpreadsheetErrorKind but could be anything.
    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameter.VALUE
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    static SpreadsheetError na(final Object value) {
        return SpreadsheetErrorKind.NA.setMessage(
            "Expected error got: " +
                CharSequences.quoteIfChars(value)
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        VALUE
    );
}
