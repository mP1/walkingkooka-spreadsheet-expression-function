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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class SpreadsheetExpressionFunctionBooleanIsErrErrorNaTest extends SpreadsheetExpressionFunctionBooleanTestCase<SpreadsheetExpressionFunctionBooleanIsErrErrorNa> {

    // isErr............................................................................................................

    @Test
    public void testIsErrNull() {
        this.isErrAndCheck(
            null,
            false
        );
    }

    @Test
    public void testIsErrBoolean() {
        this.isErrAndCheck(
            true,
            false
        );
    }

    @Test
    public void testisErrNumber() {
        this.isErrAndCheck(
            ExpressionNumberKind.DOUBLE.zero(),
            false
        );
    }

    @Test
    public void testIsErrString() {
        this.isErrAndCheck(
            "",
            false
        );
    }

    @Test
    public void testIsErrLocalDate() {
        this.isErrAndCheck(
            LocalDate.now(),
            false
        );
    }

    @Test
    public void testIsErrLocalDateTime() {
        this.isErrAndCheck(
            LocalDateTime.now(),
            false
        );
    }

    @Test
    public void testIsErrLocalTime() {
        this.isErrAndCheck(
            LocalTime.now(),
            false
        );
    }

    @Test
    public void testIsErrNAFale() {
        this.isErrAndCheck(
            SpreadsheetErrorKind.NA.setMessage("123456"),
            false
        );
    }

    @Test
    public void testIsErrAllExceptNATrue() {
        for (final SpreadsheetErrorKind kind : SpreadsheetErrorKind.values()) {
            if (kind == SpreadsheetErrorKind.NA) {
                continue;
            }
            this.isErrAndCheck(
                kind.setMessage("123456"),
                true
            );
        }
    }

    private void isErrAndCheck(final Object value,
                               final boolean expected) {
        this.isAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isErr(),
            value,
            expected
        );
    }

    // isError............................................................................................................

    @Test
    public void testIsErrorNull() {
        this.isErrorAndCheck(
            null,
            false
        );
    }

    @Test
    public void testIsErrorBoolean() {
        this.isErrorAndCheck(
            true,
            false
        );
    }

    @Test
    public void testisErrorNumber() {
        this.isErrorAndCheck(
            ExpressionNumberKind.DOUBLE.zero(),
            false
        );
    }

    @Test
    public void testIsErrorString() {
        this.isErrorAndCheck(
            "",
            false
        );
    }

    @Test
    public void testIsErrorLocalDate() {
        this.isErrorAndCheck(
            LocalDate.now(),
            false
        );
    }

    @Test
    public void testIsErrorLocalDateTime() {
        this.isErrorAndCheck(
            LocalDateTime.now(),
            false
        );
    }

    @Test
    public void testIsErrorLocalTime() {
        this.isErrorAndCheck(
            LocalTime.now(),
            false
        );
    }

    @Test
    public void testIsErrorAnySpreadsheetErrorKind() {
        for (final SpreadsheetErrorKind kind : SpreadsheetErrorKind.values()) {
            this.isErrorAndCheck(
                kind.setMessage("123456"),
                true
            );
        }
    }

    private void isErrorAndCheck(final Object value,
                                 final boolean expected) {
        this.isAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isError(),
            value,
            expected
        );
    }

    // isNa............................................................................................................

    @Test
    public void testIsNaNull() {
        this.isNaAndCheck(
            null,
            false
        );
    }

    @Test
    public void testIsNaBoolean() {
        this.isNaAndCheck(
            true,
            false
        );
    }

    @Test
    public void testisNaNumber() {
        this.isNaAndCheck(
            ExpressionNumberKind.DOUBLE.zero(),
            false
        );
    }

    @Test
    public void testIsNaString() {
        this.isNaAndCheck(
            "",
            false
        );
    }

    @Test
    public void testIsNaLocalDate() {
        this.isNaAndCheck(
            LocalDate.now(),
            false
        );
    }

    @Test
    public void testIsNaLocalDateTime() {
        this.isNaAndCheck(
            LocalDateTime.now(),
            false
        );
    }

    @Test
    public void testIsNaLocalTime() {
        this.isNaAndCheck(
            LocalTime.now(),
            false
        );
    }

    @Test
    public void testIsNaSpreadsheetErrorKindNa() {
        this.isNaAndCheck(
            SpreadsheetErrorKind.NA.setMessage("MustReturnTrue"),
            true
        );
    }

    @Test
    public void testIsNaAnySpreadsheetErrorKind() {
        for (final SpreadsheetErrorKind kind : SpreadsheetErrorKind.values()) {
            if (SpreadsheetErrorKind.NA == kind) {
                continue;
            }
            this.isNaAndCheck(
                kind.setMessage("MustReturnFalse"),
                false
            );
        }
    }

    private void isNaAndCheck(final Object value,
                              final boolean expected) {
        this.isAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isNa(),
            value,
            expected
        );
    }

    private void isAndCheck(final SpreadsheetExpressionFunctionBooleanIsErrErrorNa function,
                            final Object value,
                            final boolean expected) {
        this.applyAndCheck(
            function,
            Lists.of(
                value
            ),
            this.createContext(),
            expected
        );
    }

    // toString........................................................................................................

    @Test
    public void testIsErrToString() {
        this.toStringAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isErr(),
            "isErr"
        );
    }

    @Test
    public void testIsErrorToString() {
        this.toStringAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isError(),
            "isError"
        );
    }

    @Test
    public void testIsNaToString() {
        this.toStringAndCheck(
            SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isNa(),
            "isNa"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionBooleanIsErrErrorNa createBiFunction() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isErr();
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionBooleanIsErrErrorNa> type() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.class;
    }
}
