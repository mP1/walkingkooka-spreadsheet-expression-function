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
import walkingkooka.spreadsheet.value.SpreadsheetError;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberSign;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.string.StringExpressionFunctions;

import java.util.List;

/**
 * Wraps {@link StringExpressionFunctions#findCaseSensitive}
 */
final class SpreadsheetExpressionFunctionObjectFind extends SpreadsheetExpressionFunctionObject {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionObjectFind INSTANCE = new SpreadsheetExpressionFunctionObjectFind();

    /**
     * Private ctor use singleton
     */
    private SpreadsheetExpressionFunctionObjectFind() {
        super("find");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return FIND_CASE_SENSITIVE.parameters(count);
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        final ExpressionNumber result = FIND_CASE_SENSITIVE.apply(
            parameters,
            context
        );

        return ExpressionNumberSign.POSITIVE == result.sign() ?
            result :
            notFound(parameters);
    }

    private static SpreadsheetError notFound(final List<Object> parameters) {
        final StringBuilder b = new StringBuilder();

        b.append(CharSequences.quoteIfChars(parameters.get(0)));
        b.append(" not found in ");
        b.append(CharSequences.quoteIfChars(parameters.get(1)));

        if (parameters.size() > 2) {
            b.append(" start ");
            b.append(parameters.get(2));
        }

        return SpreadsheetErrorKind.VALUE
            .setMessage(b.toString());
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> FIND_CASE_SENSITIVE = StringExpressionFunctions.findCaseSensitive();
}
