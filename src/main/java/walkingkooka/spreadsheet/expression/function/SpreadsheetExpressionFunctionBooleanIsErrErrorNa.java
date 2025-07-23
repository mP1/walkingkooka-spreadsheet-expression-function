
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

import walkingkooka.Cast;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.HasSpreadsheetErrorKind;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;

import java.util.List;
import java.util.function.Predicate;

/**
 * A collection of isXXX functions that can be used to test a variety of errors.
 */
final class SpreadsheetExpressionFunctionBooleanIsErrErrorNa extends SpreadsheetExpressionFunctionBoolean {

    /**
     * ISERR Instance getter.
     * <br>
     * https://exceljet.net/excel-functions/excel-iserr-function
     */
    static SpreadsheetExpressionFunctionBooleanIsErrErrorNa isErr() {
        return Cast.to(ISERR);
    }

    private static final SpreadsheetExpressionFunctionBooleanIsErrErrorNa ISERR = new SpreadsheetExpressionFunctionBooleanIsErrErrorNa(
        "isErr",
        Predicates.not(
            Predicates.is(SpreadsheetErrorKind.NA)
        )
    );

    /**
     * ISERROR Instance getter.
     * <br>
     * https://exceljet.net/excel-functions/excel-iserror-function
     */
    static SpreadsheetExpressionFunctionBooleanIsErrErrorNa isError() {
        return Cast.to(ISERROR);
    }

    private static final SpreadsheetExpressionFunctionBooleanIsErrErrorNa ISERROR = new SpreadsheetExpressionFunctionBooleanIsErrErrorNa(
        "isError",
        Predicates.always()
    );

    /**
     * ISNA Instance getter.
     * <br>
     * https://exceljet.net/excel-functions/excel-isna-function
     */
    static SpreadsheetExpressionFunctionBooleanIsErrErrorNa isNa() {
        return Cast.to(ISNA);
    }

    private static final SpreadsheetExpressionFunctionBooleanIsErrErrorNa ISNA = new SpreadsheetExpressionFunctionBooleanIsErrErrorNa(
        "isNa",
        Predicates.is(SpreadsheetErrorKind.NA)
    );

    private SpreadsheetExpressionFunctionBooleanIsErrErrorNa(final String name,
                                                             final Predicate<SpreadsheetErrorKind> kind) {
        super(name);
        this.kind = kind;
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final Object value = VALUE.getOrFail(parameters, 0);
        boolean is = false;

        if (value instanceof HasSpreadsheetErrorKind) {
            final HasSpreadsheetErrorKind has = (HasSpreadsheetErrorKind) value;
            is = this.kind.test(has.spreadsheetErrorKind());
        }

        return is;
    }

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameter.VALUE
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    private final Predicate<SpreadsheetErrorKind> kind;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(VALUE);
}
