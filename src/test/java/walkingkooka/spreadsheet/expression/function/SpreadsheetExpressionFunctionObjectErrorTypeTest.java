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

public final class SpreadsheetExpressionFunctionObjectErrorTypeTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectErrorType> {

    @Test
    public void testNull() {
        this.errorTypeAndCheck(null);
    }

    @Test
    public void testBoolean() {
        this.errorTypeAndCheck(Boolean.TRUE);
    }

    @Test
    public void testNumber() {
        this.errorTypeAndCheck(ExpressionNumberKind.DEFAULT.one());
    }

    @Test
    public void testString() {
        this.errorTypeAndCheck("Hello");
    }

    @Test
    public void testError() {
        for (final SpreadsheetErrorKind kind : SpreadsheetErrorKind.values()) {
            this.errorTypeAndCheck(
                kind.setMessage("Error!"),
                this.createContext().expressionNumberKind().create(kind.value())
            );
        }
    }

    private void errorTypeAndCheck(final Object value) {
        this.errorTypeAndCheck(
            value,
            SpreadsheetExpressionFunctionObjectErrorType.na(value)
        );
    }

    private void errorTypeAndCheck(final Object value,
                                   final Object expected) {
        this.applyAndCheck2(
            Lists.of(
                value
            ),
            expected
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "Error.Type");
    }

    @Override
    public SpreadsheetExpressionFunctionObjectErrorType createBiFunction() {
        return SpreadsheetExpressionFunctionObjectErrorType.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectErrorType> type() {
        return SpreadsheetExpressionFunctionObjectErrorType.class;
    }
}
