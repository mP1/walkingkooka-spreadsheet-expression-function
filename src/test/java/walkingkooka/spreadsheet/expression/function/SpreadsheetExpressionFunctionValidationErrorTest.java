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
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.validation.ValidationError;

public final class SpreadsheetExpressionFunctionValidationErrorTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionValidationError, ValidationError<SpreadsheetExpressionReference>> {

    @Test
    public void testApply() {
        this.applyAndCheck2(
            Lists.of(
                SpreadsheetError.parse("#N/A Hello message 123")
            ),
            SpreadsheetForms.error(
                CELL.reference()
            ).setMessage("Hello message 123")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "validationError"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionValidationError createBiFunction() {
        return SpreadsheetExpressionFunctionValidationError.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionValidationError> type() {
        return SpreadsheetExpressionFunctionValidationError.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}
