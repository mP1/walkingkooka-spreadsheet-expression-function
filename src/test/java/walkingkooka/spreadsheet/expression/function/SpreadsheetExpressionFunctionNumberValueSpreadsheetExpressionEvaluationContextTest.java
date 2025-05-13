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

import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContextTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.validation.SpreadsheetValidatorContext;
import walkingkooka.validation.form.FormField;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContextTest implements SpreadsheetExpressionEvaluationContextTesting<SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext> {

    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    private final static String CURRENCY_SYMBOL = "AUD";
    private final static char DECIMAL_SEPARATOR = '/';
    private final static String EXPONENT_SYMBOL = "HELLO";
    private final static char GROUP_SEPARATOR = '/';
    private final static String INFINITY_SYMBOL = "Infinity!";
    private final static char MONETARY_DECIMAL_SEPARATOR = '*';
    private final static String NAN_SYMBOL = "Nan!";
    private final static char NEGATIVE_SYMBOL = 'N';
    private final static char PERCENT_SYMBOL = 'R';
    private final static char PERMILL_SYMBOL = '^';
    private final static char POSITIVE_SYMBOL = 'P';
    private final static char ZERO_DIGIT = '0';

    @Override
    public void testLoadCellWithNullCellFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLoadCellRangeWithNullRangeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLoadLabelWithNullLabelFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIsPureNullNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseFormulaNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext createContext() {
        return SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext.with(
                DECIMAL_SEPARATOR,
                GROUP_SEPARATOR,
                new FakeSpreadsheetExpressionEvaluationContext() {

                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return Optional.empty();
                    }

                    @Override
                    public String currencySymbol() {
                        return CURRENCY_SYMBOL;
                    }

                    @Override
                    public char decimalSeparator() {
                        return DECIMAL_SEPARATOR;
                    }

                    @Override
                    public String exponentSymbol() {
                        return EXPONENT_SYMBOL;
                    }

                    @Override
                    public char groupSeparator() {
                        return GROUP_SEPARATOR;
                    }

                    @Override
                    public String infinitySymbol() {
                        return INFINITY_SYMBOL;
                    }

                    @Override
                    public MathContext mathContext() {
                        return MATH_CONTEXT;
                    }

                    @Override
                    public String nanSymbol() {
                        return NAN_SYMBOL;
                    }

                    @Override
                    public char monetaryDecimalSeparator() {
                        return MONETARY_DECIMAL_SEPARATOR;
                    }

                    @Override
                    public char negativeSign() {
                        return NEGATIVE_SYMBOL;
                    }

                    @Override
                    public char percentSymbol() {
                        return PERCENT_SYMBOL;
                    }

                    @Override
                    public char permillSymbol() {
                        return PERMILL_SYMBOL;
                    }

                    @Override
                    public char positiveSign() {
                        return POSITIVE_SYMBOL;
                    }

                    @Override
                    public char zeroDigit() {
                        return ZERO_DIGIT;
                    }

                    // FormHandlerContext...............................................................................

                    @Override
                    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
                        Objects.requireNonNull(name, "name");

                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Optional<Object> loadFormFieldValue(final SpreadsheetExpressionReference reference) {
                        Objects.requireNonNull(reference, "reference");

                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public SpreadsheetDelta saveFormFieldValues(final List<FormField<SpreadsheetExpressionReference>> fields) {
                        Objects.requireNonNull(fields, "fields");

                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Optional<EmailAddress> user() {
                        return Optional.of(SpreadsheetMetadataTesting.USER);
                    }

                    @Override
                    public SpreadsheetValidatorContext validatorContext(final SpreadsheetExpressionReference reference) {
                        Objects.requireNonNull(reference, "reference");

                        throw new UnsupportedOperationException();
                    }
                }
        );
    }

    @Override
    public String currencySymbol() {
        return CURRENCY_SYMBOL;
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL_SEPARATOR;
    }

    @Override
    public String exponentSymbol() {
        return EXPONENT_SYMBOL;
    }

    @Override
    public char groupSeparator() {
        return GROUP_SEPARATOR;
    }

    @Override
    public String infinitySymbol() {
        return INFINITY_SYMBOL;
    }

    @Override
    public MathContext mathContext() {
        return MATH_CONTEXT;
    }

    @Override
    public char monetaryDecimalSeparator() {
        return MONETARY_DECIMAL_SEPARATOR;
    }

    @Override
    public String nanSymbol() {
        return NAN_SYMBOL;
    }

    @Override
    public char negativeSign() {
        return NEGATIVE_SYMBOL;
    }

    @Override
    public char percentSymbol() {
        return PERCENT_SYMBOL;
    }

    @Override
    public char permillSymbol() {
        return PERMILL_SYMBOL;
    }

    @Override
    public char positiveSign() {
        return POSITIVE_SYMBOL;
    }

    @Override
    public char zeroDigit() {
        return ZERO_DIGIT;
    }

    @Override
    public void testSetSpreadsheetMetadataWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetSpreadsheetMetadataWithDifferentIdFails() {
        throw new UnsupportedOperationException();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext> type() {
        return SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext.class;
    }
}
